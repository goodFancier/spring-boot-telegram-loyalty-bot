package com.springboottelegrambot.model.commands.restorepassword;

import com.springboottelegrambot.model.dto.CommandParent;
import com.springboottelegrambot.network.ApacheHttp;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RequestSmsCode implements CommandParent<SendMessage>
{
		ApacheHttp apacheHttp;

		public RequestSmsCode(ApacheHttp apacheHttp)
		{
				this.apacheHttp = apacheHttp;
		}

		@Override
		public SendMessage parse(Update update) throws Exception
		{
				String ret;
				String url = "http://localhost:8090/buyers/requestSMSCodeNewLogic?retailer=9vjsj67qp9&phone=79991898516&phoneNew=79991898516&requestType=Restore";
				int i = 0;
				do
				{
						ret = apacheHttp.sendGetRequest(url);
						i++;
				}
				while(ret.equals("") && i < 5);
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(update.getMessage().getChatId().toString());
				sendMessage.setText(update.getMessage().getText());
				sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
				return sendMessage;
		}
}
