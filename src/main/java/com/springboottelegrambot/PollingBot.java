package com.springboottelegrambot;

import com.springboottelegrambot.model.dto.Chat;
import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.model.dto.CommandParent;
import com.springboottelegrambot.model.dto.CommandWaiting;
import com.springboottelegrambot.model.enums.AccessLevels;
import com.springboottelegrambot.repository.ChatRepository;
import com.springboottelegrambot.repository.CommandRepository;
import com.springboottelegrambot.repository.CommandWaitingRepository;
import com.springboottelegrambot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Component
public class PollingBot extends TelegramLongPollingBot
{
		private final UserRepository userRepository;

		private final CommandRepository commandRepository;

		private final CommandWaitingRepository commandWaitingRepository;

		private final ChatRepository chatRepository;

		private final ApplicationContext context;

		private final Logger log = LoggerFactory.getLogger(PollingBot.class);

		public PollingBot(
			UserRepository userRepository,
			CommandRepository commandRepository,
			CommandWaitingRepository commandWaitingRepository,
			ChatRepository chatRepository,
			ApplicationContext context)
		{
				this.userRepository = userRepository;
				this.commandRepository = commandRepository;
				this.commandWaitingRepository = commandWaitingRepository;
				this.chatRepository = chatRepository;
				this.context = context;
		}

		@Override
		public String getBotUsername()
		{
				return null;
		}

		@Override
		public void onRegister()
		{
		}

		@Override
		public String getBotToken()
		{
				return null;
		}

		@Override
		public void onUpdateReceived(Update update)
		{
				Message message;
				User user;
				String textOfMessage;
				boolean editedMessage = false;
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
										editedMessage = true;
								}
								else
								{
										return;
								}
						textOfMessage = message.getText();
						user = message.getFrom();
				}
				Long chatId = message.getChatId();
				com.springboottelegrambot.model.dto.User currentUser = userRepository.findById(chatId).get();
				log.info("From " + chatId + " (" + user.getUserName() + "-" + currentUser.getRecID() + "): " + textOfMessage);
				AccessLevels userAccessLevel = userRepository.getAccessLevelByUser(currentUser);
				if(userAccessLevel.equals(AccessLevels.BANNED))
				{
						log.info("Banned user. Ignoring...");
						return;
				}
				Command command = commandRepository.findByCommandNameInOrDescription(textOfMessage);
				if(command == null)
				{
						Optional<Chat> chat = chatRepository.findById(chatId);
						if(chat.isPresent())
						{
								Optional<CommandWaiting> commandWaiting = commandWaitingRepository.findByChatAndUser(chat.get(), currentUser);
								if(!commandWaiting.isPresent())
								{
										return;
								}
								command = commandRepository.findCommandByName(commandWaiting.get().getCommand().getCommandName());
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
				if(userRepository.isUserHaveAccessForCommand(userAccessLevel, command.getAccessLevel()))
				{
						Parser parser = new Parser(this, command, update);
						parser.start();
				}
		}
}
