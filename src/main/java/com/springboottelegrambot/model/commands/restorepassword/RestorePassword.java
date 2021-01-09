package com.springboottelegrambot.model.commands.restorepassword;

import com.springboottelegrambot.model.commands.CommandParent;
import com.springboottelegrambot.model.dto.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RestorePassword  implements CommandParent<SendMessage>
{
		@Override
		public SendMessage parse(User user, Update update) throws Exception
		{
				return null;
		}
}
