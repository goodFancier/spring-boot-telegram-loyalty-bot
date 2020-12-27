package com.springboottelegrambot.service;

import com.springboottelegrambot.model.enums.AccessLevels;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
		public Boolean isUserHaveAccessForCommand(AccessLevels userAccessLevel, AccessLevels commandAccessLevel)
		{
				return userAccessLevel.getValue() >= commandAccessLevel.getValue();
		}
}
