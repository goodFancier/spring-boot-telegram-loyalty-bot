package com.springboottelegrambot.service.impl;

import com.springboottelegrambot.model.dto.User;
import com.springboottelegrambot.model.enums.AccessLevels;
import com.springboottelegrambot.repository.UserRepository;
import com.springboottelegrambot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{
		private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

		private UserRepository userRepository;

		public UserServiceImpl(UserRepository userRepository)
		{
				this.userRepository = userRepository;
		}

		@Override
		public Boolean isUserHaveAccessForCommand(AccessLevels userAccessLevel, AccessLevels commandAccessLevel)
		{
				return userAccessLevel.getValue() >= commandAccessLevel.getValue();
		}

		@Override
		public User loadUser(String userCode)
		{
				log.debug("Request to get User by userId: {} ", userCode);
				Optional<User> user = userRepository.findByInternalCode(userCode);
				return user.orElse(null);
		}

		@Override
		public User save(User user)
		{
				log.debug("Request to save User: {} ", user);
				return userRepository.save(user);
		}

		public User updateUserInfo(User user, org.telegram.telegrambots.meta.api.objects.User telegramUser, String userCode)
		{
				if(user == null)
				{
						user = new User();
						user.setUsername(telegramUser.getFirstName() + " " + telegramUser.getLastName());
						user.setInternalCode(userCode);
						user.setAccessLevel(AccessLevels.NEWCOMER);
						user = save(user);
				}
				else
						if(!user.getUsername().equals(telegramUser.getFirstName() + " " + telegramUser.getLastName()))
						{
								user.setUsername(telegramUser.getFirstName() + " " + telegramUser.getLastName());
								user = save(user);
						}
				return user;
		}
}
