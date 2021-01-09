package com.springboottelegrambot.service;

import com.springboottelegrambot.model.dto.User;
import com.springboottelegrambot.model.enums.AccessLevels;

public interface UserService
{
		Boolean isUserHaveAccessForCommand(AccessLevels userAccessLevel, AccessLevels commandAccessLevel);

		User loadUser(String userCode);

		User save(User user);

		User updateUserInfo(User user, org.telegram.telegrambots.meta.api.objects.User telegramUser, String userCode);
}
