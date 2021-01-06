package com.springboottelegrambot.model.enums;

public enum CommandType
{
		InitLoginScreen("Войти по номеру телефона"),
		RequestSmsCode("Получить смс код"),
		Card("Моя карта"),
		Profile("Профиль"),
		Summary("Статистика покупок"),
		Offers("Акции");

		private final String title;

		CommandType(String title)
		{
				this.title = title;
		}

		public String getTitle()
		{
				return title;
		}
}
