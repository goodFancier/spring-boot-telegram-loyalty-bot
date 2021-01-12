package com.springboottelegrambot.model.commands.restorepassword;

import com.springboottelegrambot.config.BotConfig;
import com.springboottelegrambot.model.commands.CommandParent;
import com.springboottelegrambot.model.dto.User;
import com.springboottelegrambot.model.enums.CommandType;
import com.springboottelegrambot.network.RestService;
import com.springboottelegrambot.payload.Buyer;
import com.springboottelegrambot.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@Component
public class CheckDisposablePassword implements CommandParent<SendMessage>
{
		private final Logger log = LoggerFactory.getLogger(CheckDisposablePassword.class);

		private BotConfig botConfig;

		private RestService restService;

		@Autowired
		private MessageRepository messageRepository;

		public CheckDisposablePassword(BotConfig botConfig, RestService restService)
		{
				this.botConfig = botConfig;
				this.restService = restService;
		}

		@Override
		public SendMessage parse(User user, Update update) throws IOException
		{
				SendMessage sendMessage = new SendMessage();
				Message message = getMessageFromUpdate(update);
				if(!update.hasMessage())
				{
						sendMessage.setChatId(message.getChatId().toString());
						sendMessage.setReplyToMessageId(message.getMessageId());
						sendMessage.setText("Введите код подтверждения");
				}
				else
				{
						if(update.hasMessage())
						{
								String phone = null;
								com.springboottelegrambot.model.dto.Message dbMessage = messageRepository.findFirstByUserAndCommandTypeOrderByDateDesc(user, CommandType.RequestSmsCode).orElse(null);
								if(dbMessage != null)
								{
										phone = dbMessage.getTextOfMessage();
										Buyer buyer = restService.checkDisposableCode(sendMessage, botConfig.getRetailer(), phone, message.getText());
										if(buyer != null)
										{
												log.info("Код подтверждения введен верно");
												sendMessage.setText(buyer.getName() != null? String.format("Вы успешно авторизировались, %s!", buyer.getName()): "Вы успешно авторизировались!");
										}
										else
										{
												sendMessage.setText("Одноразовый код введен неправильно");
										}
								}
								else
										sendMessage.setText("Ошибка проверки кода. Попробуйте еще раз");
						}
						sendMessage.setChatId(message.getChatId().toString());
						return sendMessage;
				}
				return sendMessage;
		}
}
