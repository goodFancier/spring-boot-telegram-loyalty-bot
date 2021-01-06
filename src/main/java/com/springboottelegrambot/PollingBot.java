package com.springboottelegrambot;

import com.springboottelegrambot.config.BotConfig;
import com.springboottelegrambot.model.commands.login.InitLoginScreen;
import com.springboottelegrambot.model.dto.Chat;
import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.model.dto.CommandParent;
import com.springboottelegrambot.model.dto.CommandWaiting;
import com.springboottelegrambot.model.enums.AccessLevels;
import com.springboottelegrambot.repository.ChatRepository;
import com.springboottelegrambot.repository.CommandWaitingRepository;
import com.springboottelegrambot.service.CommandService;
import com.springboottelegrambot.service.UserService;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PollingBot extends TelegramLongPollingBot
{
		private final UserService userService;

		private final BotConfig botConfig;

		private final CommandService commandService;

		private final CommandWaitingRepository commandWaitingRepository;

		private final ChatRepository chatRepository;

		private final ApplicationContext context;

		private final Logger log = LoggerFactory.getLogger(PollingBot.class);

		public PollingBot(
			UserService userService,
			CommandWaitingRepository commandWaitingRepository,
			ChatRepository chatRepository,
			ApplicationContext context,
			BotConfig botConfig,
			CommandService commandService)
		{
				this.commandWaitingRepository = commandWaitingRepository;
				this.chatRepository = chatRepository;
				this.context = context;
				this.userService = userService;
				this.botConfig = botConfig;
				this.commandService = commandService;
		}

		@Override
		public String getBotUsername()
		{
				String botUserName = botConfig.getTelegramBotUsername();
				if(botUserName == null)
				{
						User botUser;
						try
						{
								botUser = this.execute(new GetMe());
								botUserName = botUser.getUserName();
								botConfig.setTelegramBotUsername(botUserName);
						}
						catch(TelegramApiException e)
						{
								botUserName = "testBot";
						}
				}
				return botUserName;
		}

		@Override
		public String getBotToken()
		{
				String telegramBotApiToken = botConfig.getTelegramBotApiToken();
				if(telegramBotApiToken.equals(""))
				{
						log.info("Can't find telegram bot api token. See the properties.properties file");
				}
				return telegramBotApiToken;
		}

		@Override
		public void onUpdateReceived(Update update)
		{
				Message message;
				User telegramUser;
				String textOfMessage;
				if(update.hasCallbackQuery())
				{
						CallbackQuery callbackQuery = update.getCallbackQuery();
						message = callbackQuery.getMessage();
						textOfMessage = callbackQuery.getData();
						telegramUser = callbackQuery.getFrom();
				}
				else
				{
						if(update.hasMessage())
						{
								message = update.getMessage();
						}
						else
								if(update.hasEditedMessage())
								{
										message = update.getEditedMessage();
								}
								else
								{
										return;
								}
						textOfMessage = message.getText();
						telegramUser = message.getFrom();
				}
				Long chatId = message.getChatId();
				Long userId = Long.valueOf(telegramUser.getId());

				log.info("From " + chatId + " (" + telegramUser.getUserName() + "-" + userId + "): " + textOfMessage);
				com.springboottelegrambot.model.dto.User user = userService.loadUser(userId);
				user = userService.updateUserInfo(user, telegramUser);
				if(user.getAccessLevel().equals(AccessLevels.BANNED))
				{
						log.info("Banned user. Ignoring...");
						return;
				}
				if(textOfMessage == null || textOfMessage.equals(""))
				{
						return;
				}
				Command command = commandService.findCommandInText(textOfMessage, this.getBotUsername());
				if(command == null)
				{
						Optional<Chat> chat = chatRepository.findByRecID(chatId);
						CommandHandler commandHandler;
						if(chat.isPresent())
						{
								Optional<CommandWaiting> commandWaiting = commandWaitingRepository.findByChatAndUser(chat.get(), user);
								if(commandWaiting.isEmpty())
								{
										return;
								}
								command = commandService.findCommandByName(commandWaiting.get().getCommand().getCommandName());
								commandHandler = new CommandHandler(this, command == null? new InitLoginScreen(): (CommandParent<?>)context.getBean(command.getClassName()), update);
						}
						else
								commandHandler = new CommandHandler(this, new InitLoginScreen(), update);
						commandHandler.handle();
				}
				else
				{
						Reflections reflections = new Reflections("com.springboottelegrambot.model.commands", CommandParent.class, new SubTypesScanner(false));
						Set<Class<? extends CommandParent>> commands = reflections.getSubTypesOf(CommandParent.class);
						Class<? extends CommandParent> foundCommand = commands.stream().filter(o -> o.getSimpleName().equals(textOfMessage)).collect(Collectors.toList()).get(0);
						if(userService.isUserHaveAccessForCommand(user.getAccessLevel(), command.getAccessLevel()))
						{
								CommandHandler commandHandler = null;
								try
								{
										commandHandler = new CommandHandler(this, foundCommand.getDeclaredConstructor().newInstance(), update);
								}
								catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
								{
										e.printStackTrace();
								}
								if(commandHandler != null)
										commandHandler.handle();
						}
				}
		}
}
