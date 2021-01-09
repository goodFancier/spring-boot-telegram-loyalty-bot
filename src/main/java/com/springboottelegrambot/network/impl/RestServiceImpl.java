package com.springboottelegrambot.network.impl;

import com.springboottelegrambot.config.BotConfig;
import com.springboottelegrambot.network.ApacheHttp;
import com.springboottelegrambot.network.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;

@Service
public class RestServiceImpl implements RestService
{
		private final Logger log = LoggerFactory.getLogger(RestServiceImpl.class);

		private final ApacheHttp apacheHttp;

		private final BotConfig botConfig;

		public RestServiceImpl(ApacheHttp apacheHttp, BotConfig botConfig)
		{
				this.apacheHttp = apacheHttp;
				this.botConfig = botConfig;
		}

		@Override
		public void requestSmsCode(SendMessage sendMessage, String retailer, String phone)
		{
				String url = String.format("%s/buyers/requestSMSCodeNewLogic?retailer=%s&phone=%s&phoneNew=%s&requestType=Restore", botConfig.getBaseUrl(), retailer, phone, phone);
				try
				{
						apacheHttp.sendGetRequest(url);
						sendMessage.setText(String.format("Смс с кодом подтверждения отправлено на номер: %s", phone));
				}
				catch(IOException e)
				{
						log.error("Ошибка при запросе смс кода {}", e.getCause().getMessage());
						sendMessage.setText("Ошибка при запросе смс кода");
				}
		}
}
