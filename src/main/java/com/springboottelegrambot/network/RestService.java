package com.springboottelegrambot.network;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface RestService
{
		void requestSmsCode(SendMessage sendMessage, String retailer, String phone);
}
