package com.springboottelegrambot.model.commands.restorepassword;

import com.springboottelegrambot.model.commands.CommandParent;
import com.springboottelegrambot.model.dto.User;
import com.springboottelegrambot.network.ApacheHttp;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@Component
public class RequestSmsCode implements CommandParent<SendMessage>
{
		public RequestSmsCode()
		{
				this.apacheHttp = new ApacheHttp();
		}

		ApacheHttp apacheHttp;

		public RequestSmsCode(ApacheHttp apacheHttp)
		{
				this.apacheHttp = apacheHttp;
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
						if(update.getCallbackQuery() != null)
						{
								String url = "http://localhost:8090/Shopper.Rest/buyers/requestSMSCodeNewLogic?retailer=9vjsj67qp9&phone=79991898516&phoneNew=79991898516&requestType=Restore";
								try
								{
										apacheHttp.sendGetRequest(url);
										sendMessage.setText("Смс с кодом подтверждения отправлено на номер: %s");
								}
								catch(IOException e)
								{
										sendMessage.setText("Ошибка при запросе смс кода");
								}
								sendMessage.setReplyToMessageId(message.getMessageId());
								sendMessage.setChatId(message.getChatId().toString());
						}
				return sendMessage;
		}
}
