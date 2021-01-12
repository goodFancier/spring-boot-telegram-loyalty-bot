package com.springboottelegrambot.network;

import com.springboottelegrambot.payload.Buyer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;

public interface RestService
{
		boolean requestSmsCode(SendMessage sendMessage, String retailer, String phone);

		Buyer checkDisposableCode(SendMessage sendMessage, String retailer, String phone, String code) throws IOException;
}
