package com.springboottelegrambot.model.commands.card;

import com.springboottelegrambot.model.commands.CommandParent;
import com.springboottelegrambot.model.dto.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class Card implements CommandParent<SendMessage>
{
		@Override
		public SendMessage parse(User user, Update update)
		{
				SendMessage sendMessage = new SendMessage();
				Message message = getMessageFromUpdate(update);
				if(update.hasMessage())
				{
						sendMessage.setChatId(message.getChatId().toString());
						sendMessage.setReplyToMessageId(message.getMessageId());
						if(user.getVerified() == null)
								sendMessage.setText("Укажите ваш номер телефона на который будет отправлено смс с кодом подтверждения");
						sendMessage.setReplyMarkup(buildMainKeyboard());
				}
				return sendMessage;
		}

		private InlineKeyboardMarkup buildMainKeyboard()
		{
				InlineKeyboardButton loginByPhoneBtn = new InlineKeyboardButton();
				loginByPhoneBtn.setText("Ввести номер телефона");
				loginByPhoneBtn.setCallbackData("RequestSmsCode");
				List<InlineKeyboardButton> loginRow = new ArrayList<>();
				loginRow.add(loginByPhoneBtn);
				List<List<InlineKeyboardButton>> rows = new ArrayList<>();
				rows.add(loginRow);
				InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
				inlineKeyboardMarkup.setKeyboard(rows);
				return inlineKeyboardMarkup;
		}
}
