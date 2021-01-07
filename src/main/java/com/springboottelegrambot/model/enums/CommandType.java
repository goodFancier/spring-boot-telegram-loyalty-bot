package com.springboottelegrambot.model.enums;

public enum CommandType
{
		InitLoginScreen("Войти по номеру телефона", AccessLevels.NEWCOMER),
		RequestSmsCode("Получить смс код", AccessLevels.NEWCOMER),
		Card("Моя карта", AccessLevels.NEWCOMER),
		Profile("Профиль", AccessLevels.NEWCOMER),
		Summary("Статистика покупок", AccessLevels.NEWCOMER),
		Offers("Лента", AccessLevels.NEWCOMER);

		private final String commandName;

		private final AccessLevels accessLevels;

		CommandType(String commandName, AccessLevels accessLevels)
		{
				this.commandName = commandName;
				this.accessLevels = accessLevels;
		}

		public String getCommandName()
		{
				return commandName;
		}

		public AccessLevels getAccessLevels()
		{
				return accessLevels;
		}
}
