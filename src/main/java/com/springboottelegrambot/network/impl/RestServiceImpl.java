package com.springboottelegrambot.network.impl;

import com.google.gson.Gson;
import com.springboottelegrambot.config.BotConfig;
import com.springboottelegrambot.network.ApacheHttp;
import com.springboottelegrambot.network.RestService;
import com.springboottelegrambot.payload.Buyer;
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
		public boolean requestSmsCode(SendMessage sendMessage, String retailer, String phone)
		{
				String url = String.format("%s/buyers/requestSMSCodeNewLogic?retailer=%s&phone=%s&phoneNew=%s&requestType=Restore", botConfig.getBaseUrl(), retailer, phone, phone);
				try
				{
						apacheHttp.sendGetRequest(url);
						return true;
				}
				catch(IOException e)
				{
						log.error("Ошибка при запросе смс кода {}", e.getCause().getMessage());
						return false;
				}
		}

		@Override
		public Buyer checkDisposableCode(SendMessage sendMessage, String retailer, String phone, String code) throws IOException
		{
				String url = String.format("%s/buyers/loginByPhoneNumber?retailer=%s&cardID="
					+ "&device=&phone=%s&code=%s&newLogic=true", botConfig.getBaseUrl(), retailer, phone, code);
				try
				{
						Gson gson = new Gson();
						return gson.fromJson(apacheHttp.sendGetRequest(url), Buyer.class);
				}
				catch(IOException e)
				{
						log.error("Ошибка при запросе смс кода {}", e.getCause().getMessage());
						return null;
				}
		}
}
