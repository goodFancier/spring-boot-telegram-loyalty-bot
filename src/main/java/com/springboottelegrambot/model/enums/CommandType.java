package com.springboottelegrambot.model.enums;

public enum CommandType
{
		InitLoginScreen("Войти по номеру телефона", AccessLevels.NEWCOMER),
		RequestSmsCode("RequestSmsCode", AccessLevels.NEWCOMER),
		Card("Моя карта", AccessLevels.NEWCOMER),
		Profile("Профиль", AccessLevels.NEWCOMER),
		Summary("Статистика покупок", AccessLevels.NEWCOMER),
		Offers("Лента", AccessLevels.NEWCOMER);

		private final String commandName;

		private final AccessLevels accessLevel;

		CommandType(String commandName, AccessLevels accessLevel)
		{
				this.commandName = commandName;
				this.accessLevel = accessLevel;
		}

		public String getCommandName()
		{
				return commandName;
		}

		public AccessLevels getAccessLevel()
		{
				return accessLevel;
		}
}
