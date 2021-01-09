package com.springboottelegrambot;

import com.springboottelegrambot.config.BotConfig;
import com.springboottelegrambot.model.commands.DefaultCommand;
import com.springboottelegrambot.model.commands.CommandParent;
import com.springboottelegrambot.model.enums.AccessLevels;
import com.springboottelegrambot.model.enums.CommandType;
import com.springboottelegrambot.repository.MessageRepository;
import com.springboottelegrambot.service.UserService;
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

import java.util.Date;
import java.util.Objects;

@Component
public class PollingBot extends TelegramLongPollingBot
{
		private final UserService userService;

		private final BotConfig botConfig;

		private final MessageRepository messageRepository;

		private final Logger log = LoggerFactory.getLogger(PollingBot.class);

		private final ApplicationContext context;

		public PollingBot(
			UserService userService,
			BotConfig botConfig,
			MessageRepository messageRepository,
			ApplicationContext context)
		{
				this.userService = userService;
				this.botConfig = botConfig;
				this.messageRepository = messageRepository;
				this.context = context;
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
				String userCode = String.valueOf(telegramUser.getId());
				log.info("From " + chatId + " (" + telegramUser.getFirstName() + " " + telegramUser.getLastName() + "-" + userCode + "): " + textOfMessage);
				com.springboottelegrambot.model.dto.User user = userService.loadUser(userCode);
				user = userService.updateUserInfo(user, telegramUser, userCode);
				if(user.getAccessLevel().equals(AccessLevels.BANNED))
				{
						log.info("Banned user. Ignoring...");
						return;
				}
				if(textOfMessage == null || textOfMessage.equals(""))
				{
						return;
				}
				CommandType foundCmd = null;
				for(CommandType commandType : CommandType.values())
				{
						if(commandType.getCommandName().equals(textOfMessage))
						{
								foundCmd = commandType;
								break;
						}
				}
				if(foundCmd == null)
				{
						com.springboottelegrambot.model.dto.Message lastMessage = Objects.requireNonNull(messageRepository.findFirstByUserOrderByDateDesc(user).orElse(null));
						if(lastMessage.getTextOfMessage().equals("RequestSmsCode"))
								foundCmd = lastMessage.getCommandType();
				}
				if(foundCmd == null)
				{
						log.warn(String.format("Неизвестная команда: %s. Выполняется базовая команда", textOfMessage));
						CommandHandler commandHandler = new CommandHandler(this, new DefaultCommand(), update);
						commandHandler.handle(user);
				}
				else
				{
						CommandParent foundCommand = (CommandParent)context.getBean(foundCmd.name());
						if(foundCommand == null)
						{
								log.warn(String.format("Не найден класс команды: %s", foundCmd.name()));
								return;
						}
						if(userService.isUserHaveAccessForCommand(user.getAccessLevel(), foundCmd.getAccessLevel()))
						{
								CommandHandler commandHandler = null;
								commandHandler = new CommandHandler(this, foundCommand, update);
								if(commandHandler != null)
										commandHandler.handle(user);
						}
				}
				com.springboottelegrambot.model.dto.Message dbMessage = new com.springboottelegrambot.model.dto.Message(textOfMessage, new Date(), foundCmd, user);
				messageRepository.save(dbMessage);
		}
}
