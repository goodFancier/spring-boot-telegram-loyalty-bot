package com.springboottelegrambot;

import com.springboottelegrambot.config.BotConfig;
import com.springboottelegrambot.model.dto.Chat;
import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.model.dto.CommandParent;
import com.springboottelegrambot.model.dto.CommandWaiting;
import com.springboottelegrambot.model.enums.AccessLevels;
import com.springboottelegrambot.repository.ChatRepository;
import com.springboottelegrambot.repository.CommandRepository;
import com.springboottelegrambot.repository.CommandWaitingRepository;
import com.springboottelegrambot.repository.UserRepository;
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

import java.util.Optional;

@Component
public class PollingBot extends TelegramLongPollingBot
{
		private final UserRepository userRepository;

		private final UserService userService;

		private final BotConfig botConfig;

		private final CommandRepository commandRepository;

		private final CommandWaitingRepository commandWaitingRepository;

		private final ChatRepository chatRepository;

		private final ApplicationContext context;

		private final Logger log = LoggerFactory.getLogger(PollingBot.class);

		public PollingBot(
			UserRepository userRepository,
			UserService userService,
			CommandRepository commandRepository,
			CommandWaitingRepository commandWaitingRepository,
			ChatRepository chatRepository,
			ApplicationContext context,
			BotConfig botConfig)
		{
				this.userRepository = userRepository;
				this.commandRepository = commandRepository;
				this.commandWaitingRepository = commandWaitingRepository;
				this.chatRepository = chatRepository;
				this.context = context;
				this.userService = userService;
				this.botConfig = botConfig;
		}

		@Override
		public String getBotUsername()
		{
				String botUserName = botConfig.getTelegramBotUsername();
				if (botUserName == null) {
						User botUser;
						try {
								botUser = this.execute(new GetMe());
								botUserName = botUser.getUserName();
								botConfig.setTelegramBotUsername(botUserName);
						} catch (TelegramApiException e) {
								botUserName = "testBot";
						}
				}

				return botUserName;
		}

		@Override
		public void onRegister()
		{
		}

		@Override
		public String getBotToken()
		{
				return "BotToken";
		}

		@Override
		public void onUpdateReceived(Update update)
		{
				Message message;
				User user;
				String textOfMessage;
				if(update.hasCallbackQuery())
				{
						CallbackQuery callbackQuery = update.getCallbackQuery();
						message = callbackQuery.getMessage();
						textOfMessage = callbackQuery.getData();
						user = callbackQuery.getFrom();
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
						user = message.getFrom();
				}
				Long chatId = message.getChatId();
				Optional<com.springboottelegrambot.model.dto.User> currentUserOptional = userRepository.findByRecID(chatId);
				if(currentUserOptional.isEmpty())
						return;
				com.springboottelegrambot.model.dto.User currentUser = currentUserOptional.get();
				log.info("From " + chatId + " (" + user.getUserName() + "-" + currentUser.getRecID() + "): " + textOfMessage);
				AccessLevels userAccessLevel = userRepository.getAccessLevelByRecID(currentUser.getRecID());
				if(userAccessLevel.equals(AccessLevels.BANNED))
				{
						log.info("Banned user. Ignoring...");
						return;
				}
				Command command = commandRepository.findByCommandNameOrDescription(textOfMessage, textOfMessage);
				if(command == null)
				{
						Optional<Chat> chat = chatRepository.findByRecID(chatId);
						if(chat.isPresent())
						{
								Optional<CommandWaiting> commandWaiting = commandWaitingRepository.findByChatAndUser(chat.get(), currentUser);
								if(commandWaiting.isEmpty())
								{
										return;
								}
								command = commandRepository.findCommandByCommandName(commandWaiting.get().getCommand().getCommandName());
								if(command == null)
								{
										return;
								}
						}
						return;
				}
				CommandParent<?> commandParent = null;
				try
				{
						commandParent = (CommandParent<?>)context.getBean(command.getClassName());
				}
				catch(Exception e)
				{
						log.error(e.getMessage());
				}
				if(userService.isUserHaveAccessForCommand(userAccessLevel, command.getAccessLevel()))
				{
						CommandHandler commandHandler = new CommandHandler(this, commandParent, update);
						commandHandler.handle();
				}
		}
}
