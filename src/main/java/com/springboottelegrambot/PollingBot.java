package com.springboottelegrambot;

import com.springboottelegrambot.config.BotConfig;
import com.springboottelegrambot.model.commands.DefaultCommand;
import com.springboottelegrambot.model.dto.Chat;
import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.model.dto.CommandParent;
import com.springboottelegrambot.model.dto.CommandWaiting;
import com.springboottelegrambot.model.enums.AccessLevels;
import com.springboottelegrambot.model.enums.CommandType;
import com.springboottelegrambot.repository.ChatRepository;
import com.springboottelegrambot.repository.CommandWaitingRepository;
import com.springboottelegrambot.service.CommandService;
import com.springboottelegrambot.service.UserService;
import com.springboottelegrambot.utils.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
			CommandService commandService,
			Reflection reflection)
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
				Command command = null;
				boolean isCmdFound = false;
				for(CommandType commandType : CommandType.values())
				{
						if(commandType.getTitle().equals(textOfMessage))
						{
								command = commandService.findCommandByType(commandType);
								isCmdFound = true;
								break;
						}
				}
				if(!isCmdFound)
						log.warn(String.format("Неизвестная команда: %s. Выполняется базовая команда", textOfMessage));
				if(command == null)
				{
						Optional<Chat> chat = chatRepository.findByRecID(chatId);
						CommandHandler commandHandler = new CommandHandler(this, new DefaultCommand(), update);
						if(chat.isPresent())
						{
								Optional<CommandWaiting> commandWaiting = commandWaitingRepository.findByChatAndUser(chat.get(), user);
								if(commandWaiting.isEmpty())
								{
										return;
								}
								command = commandService.findCommandByType(commandWaiting.get().getCommand().getType());
								if(command != null)
								{
										Class<? extends CommandParent> foundCommand = Reflection.findCommandByCmdType(command);
										if(foundCommand == null)
										{
												log.warn(String.format("Не найден класс команды: %s", command.getType().name()));
												return;
										}
										try
										{
												commandHandler = new CommandHandler(this, foundCommand.getDeclaredConstructor().newInstance(), update);
										}
										catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
										{
												e.printStackTrace();
										}
								}
						}
						log.warn(String.format("Не найдено команд в базе данных с типом: %s", textOfMessage));
						commandHandler.handle();
				}
				else
				{
						Class<? extends CommandParent> foundCommand = Reflection.findCommandByCmdType(command);
						if(foundCommand == null)
						{
								log.warn(String.format("Не найден класс команды: %s", command.getType().name()));
								return;
						}
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
