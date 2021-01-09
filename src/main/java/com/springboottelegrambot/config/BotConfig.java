package com.springboottelegrambot.config;

import com.springboottelegrambot.model.commands.card.Card;
import com.springboottelegrambot.model.commands.restorepassword.RequestSmsCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "file:properties.properties", ignoreResourceNotFound = true)
public class BotConfig
{
		@Value("${telegram.telegramBotApiToken}")
		private String telegramBotApiToken;

		@Value("${telegram.adminId}")
		private String adminId;

		@Value("${telegram.screenshotMachineToken}")
		private String screenshotMachineToken;

		@Value("${telegram.googleToken}")
		private String googleToken;

		@Value("${telegram.telegramBotUsername}")
		private String telegramBotUsername;

		@Value("${retailer}")
		private String retailer;

		@Bean(name = "Card")
		public Card card()
		{
				return new Card();
		}

		@Bean(name = "RequestSmsCode")
		public RequestSmsCode requestSmsCode()
		{
				return new RequestSmsCode();
		}

		public String getTelegramBotApiToken()
		{
				return telegramBotApiToken;
		}

		public void setTelegramBotApiToken(String telegramBotApiToken)
		{
				this.telegramBotApiToken = telegramBotApiToken;
		}

		public String getAdminId()
		{
				return adminId;
		}

		public void setAdminId(String adminId)
		{
				this.adminId = adminId;
		}

		public String getScreenshotMachineToken()
		{
				return screenshotMachineToken;
		}

		public void setScreenshotMachineToken(String screenshotMachineToken)
		{
				this.screenshotMachineToken = screenshotMachineToken;
		}

		public String getGoogleToken()
		{
				return googleToken;
		}

		public void setGoogleToken(String googleToken)
		{
				this.googleToken = googleToken;
		}

		public String getTelegramBotUsername()
		{
				return telegramBotUsername;
		}

		public void setTelegramBotUsername(String telegramBotUsername)
		{
				this.telegramBotUsername = telegramBotUsername;
		}

		public String getRetailer()
		{
				return retailer;
		}

		public void setRetailer(String retailer)
		{
				this.retailer = retailer;
		}
}
