package com.springboottelegrambot.model.enums;

import java.util.Arrays;

public enum AccessLevels
{
		BANNED(-1),
		NEWCOMER(0),
		FAMILIAR(1),
		TRUSTED(2),
		MODERATOR(3),
		ADMIN(4);

		private final Integer value;

		AccessLevels(Integer value)
		{
				this.value = value;
		}

		public Integer getValue()
		{
				return value;
		}

		public static AccessLevels getUserLevelByValue(Integer value)
		{
				return Arrays.stream(AccessLevels.values())
					.filter(accessLevels -> accessLevels.value.equals(value))
					.findFirst()
					.orElse(AccessLevels.NEWCOMER);
		}
}
