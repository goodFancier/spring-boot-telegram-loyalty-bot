package com.springboottelegrambot.model.commands.restorepassword;

import com.springboottelegrambot.config.BotConfig;
import com.springboottelegrambot.model.commands.CommandParent;
import com.springboottelegrambot.model.dto.User;
import com.springboottelegrambot.network.RestService;
import com.springboottelegrambot.utils.PhoneUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RequestSmsCode implements CommandParent<SendMessage>
{
		private final Logger log = LoggerFactory.getLogger(RequestSmsCode.class);

		private BotConfig botConfig;

		private RestService restService;

		public RequestSmsCode(BotConfig botConfig, RestService restService)
		{
				this.botConfig = botConfig;
				this.restService = restService;
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
						restService.requestSmsCode(sendMessage, botConfig.getRetailer(), message.getText());
						sendMessage.setReplyToMessageId(message.getMessageId());
						sendMessage.setChatId(message.getChatId().toString());
				}
				return sendMessage;
		}
}
