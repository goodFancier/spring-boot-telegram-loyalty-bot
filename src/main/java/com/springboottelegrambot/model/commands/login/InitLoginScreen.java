package com.springboottelegrambot.model.commands.login;

import com.springboottelegrambot.model.dto.CommandParent;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitLoginScreen implements CommandParent<PartialBotApiMethod<?>>
{
		@Override
		public PartialBotApiMethod<?> parse(Update update) throws Exception
		{
				Message message = getMessageFromUpdate(update);
				if(update.hasCallbackQuery())
				{
						return buildMainPageWithCallback(message);
				}
				else
				{
						return buildMainPage(message);
				}
		}

		private SendMessage buildMainPage(Message message)
		{
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(message.getChatId().toString());
				sendMessage.setReplyToMessageId(message.getMessageId());
				sendMessage.enableHtml(true);
				sendMessage.setText("<b>Войдите по номеру телефона</b>");
				sendMessage.setReplyMarkup(buildMainKeyboard());
				return sendMessage;
		}

		private EditMessageText buildMainPageWithCallback(Message message)
		{
				EditMessageText editMessageText = new EditMessageText();
				editMessageText.setChatId(message.getChatId().toString());
				editMessageText.setMessageId(message.getMessageId());
				editMessageText.enableHtml(true);
				editMessageText.setText("<b>Войдите по номеру телефона</b>");
				editMessageText.setReplyMarkup(buildMainKeyboard());
				return editMessageText;
		}

		private InlineKeyboardMarkup buildMainKeyboard()
		{
				InlineKeyboardButton loginByPhoneBtn = new InlineKeyboardButton();
				loginByPhoneBtn.setText("Войти по номеру телефона");
				loginByPhoneBtn.setCallbackData("Войти по номеру телефона");
				List<InlineKeyboardButton> loginRow = new ArrayList<>();
				loginRow.add(loginByPhoneBtn);
				List<List<InlineKeyboardButton>> rows = new ArrayList<>();
				rows.add(loginRow);
				InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
				inlineKeyboardMarkup.setKeyboard(rows);
				return inlineKeyboardMarkup;
		}
}
