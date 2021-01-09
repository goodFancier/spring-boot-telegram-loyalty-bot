package com.springboottelegrambot.model.commands.restorepassword;

import com.springboottelegrambot.config.BotConfig;
import com.springboottelegrambot.model.commands.CommandParent;
import com.springboottelegrambot.model.dto.User;
import com.springboottelegrambot.network.ApacheHttp;
import com.springboottelegrambot.utils.PhoneUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@Component
public class RequestSmsCode implements CommandParent<SendMessage>
{
		private final Logger log = LoggerFactory.getLogger(RequestSmsCode.class);

		@Autowired
		ApacheHttp apacheHttp;

		@Autowired
		BotConfig botConfig;

		@Autowired
		public RequestSmsCode(
			ApacheHttp apacheHttp,
			BotConfig botConfig)
		{
				this.apacheHttp = apacheHttp;
				this.botConfig = botConfig;
		}

		public RequestSmsCode()
		{
		}

		@Override
		public SendMessage parse(User user, Update update)
		{
				SendMessage sendMessage = new SendMessage();
				Message message = getMessageFromUpdate(update);
				if(!update.hasMessage())
				{
						sendMessage.setChatId(message.getChatId().toString());
						sendMessage.setReplyToMessageId(message.getMessageId());
						sendMessage.setText("Укажите ваш номер телефона на который будет отправлено смс с кодом подтверждения");
				}
				else
				{
						String phone = PhoneUtils.processPhoneNumber(message.getText());
						if(phone.length() != 11)
						{
								// TODO: бросать исключению BotException
								sendMessage.setText("Ошибка при запросе смс кода. Неверно указан номер телефона");
								sendMessage.setChatId(message.getChatId().toString());
								sendMessage.setReplyToMessageId(message.getMessageId());
								return sendMessage;
						}
						String url = String.format("http://localhost:8090/Shopper.Rest/buyers/requestSMSCodeNewLogic?retailer=%s&phone=%s&phoneNew=%s&requestType=Restore", botConfig.getRetailer(), message.getText(), message.getText());
						try
						{
								apacheHttp.sendGetRequest(url);
								sendMessage.setText(String.format("Смс с кодом подтверждения отправлено на номер: %s", message.getText()));
						}
						catch(IOException e)
						{
								log.error("Ошибка при запросе смс кода {}", e.getCause().getMessage());
								sendMessage.setText("Ошибка при запросе смс кода");
						}
						sendMessage.setReplyToMessageId(message.getMessageId());
						sendMessage.setChatId(message.getChatId().toString());
				}
				return sendMessage;
		}
}
