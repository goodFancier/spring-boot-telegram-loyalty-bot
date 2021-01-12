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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RequestSmsCode implements CommandParent<SendMessage>
{
		private final Logger log = LoggerFactory.getLogger(RequestSmsCode.class);

		private BotConfig botConfig;

		private RestService restService;

		private String phone;

		public RequestSmsCode(BotConfig botConfig, RestService restService)
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
						sendMessage.setText("Укажите ваш номер телефона на который будет отправлено смс с кодом подтверждения");
				}
				else
				{
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
								if(restService.requestSmsCode(sendMessage, botConfig.getRetailer(), message.getText()))
								{
										sendMessage.setText(String.format("Смс с кодом подтверждения отправлено на номер: %s", phone));
										sendMessage.setReplyMarkup(buildMainKeyboard());
								}
								else
								{
										sendMessage.setText("Ошибка при запросе смс кода");
								}
								sendMessage.setReplyToMessageId(message.getMessageId());
								sendMessage.setChatId(message.getChatId().toString());
						}
				}
				return sendMessage;
		}

		private InlineKeyboardMarkup buildMainKeyboard()
		{
				InlineKeyboardButton smsCodeBtn = new InlineKeyboardButton();
				smsCodeBtn.setText("Ввести код подтверждения");
				smsCodeBtn.setCallbackData("CheckDisposablePassword");
				List<InlineKeyboardButton> smsCodeRow = new ArrayList<>();
				smsCodeRow.add(smsCodeBtn);
				List<List<InlineKeyboardButton>> rows = new ArrayList<>();
				rows.add(smsCodeRow);
				InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
				inlineKeyboardMarkup.setKeyboard(rows);
				return inlineKeyboardMarkup;
		}
}
