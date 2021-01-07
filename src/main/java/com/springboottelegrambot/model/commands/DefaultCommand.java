package com.springboottelegrambot.model.commands;

import com.springboottelegrambot.model.dto.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultCommand implements CommandParent<PartialBotApiMethod<?>>
{
		@Override
		public PartialBotApiMethod<?> parse(User user, Update update)
		{
				return buildMainPage(getMessageFromUpdate(update));
		}

		private SendMessage buildMainPage(Message message)
		{
				SendMessage sendMessage = new SendMessage();
				sendMessage.setChatId(message.getChatId().toString());
				sendMessage.setReplyToMessageId(message.getMessageId());
				sendMessage.enableHtml(true);
				sendMessage.setText("<b>Добро пожаловать!</b>");
				initMainMenuButtons(sendMessage);
				return sendMessage;
		}

		public void initMainMenuButtons(SendMessage sendMessage)
		{
				ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
				sendMessage.setReplyMarkup(replyKeyboardMarkup);
				replyKeyboardMarkup.setSelective(true);
				replyKeyboardMarkup.setResizeKeyboard(true);
				replyKeyboardMarkup.setOneTimeKeyboard(false);
				List<KeyboardRow> keyboard = new ArrayList<>();
				KeyboardRow keyboardFirstRow = new KeyboardRow();
				keyboardFirstRow.add("Лента");
				KeyboardRow keyboardSecondRow = new KeyboardRow();
				keyboardSecondRow.add("Моя карта");
				KeyboardRow keyboardThirdRow = new KeyboardRow();
				keyboardThirdRow.add("Статистика покупок");
				KeyboardRow keyboardFourthRow = new KeyboardRow();
				keyboardFourthRow.add("Профиль");
				keyboard.add(keyboardFirstRow);
				keyboard.add(keyboardSecondRow);
				keyboard.add(keyboardThirdRow);
				keyboard.add(keyboardFourthRow);
				replyKeyboardMarkup.setKeyboard(keyboard);
		}
}
