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
		public User loadUser(Long userId)
		{
				log.debug("Request to get User by userId: {} ", userId);
				Optional<User> user = userRepository.findByRecID(userId);
				return user.orElse(null);
		}

		@Override
		public User save(User user)
		{
				log.debug("Request to save User: {} ", user);
				return userRepository.save(user);
		}

		public User updateUserInfo(User userFrom)
		{
				String username = userFrom.getUsername();
				Long userId = userFrom.getRecID();
				User user = loadUser(userId);
				if(user == null)
				{
						user = new User();
						user.setRecID(userId);
						user.setUsername(username);
						user.setAccessLevel(AccessLevels.NEWCOMER);
						user = save(user);
				}
				else
						if(!user.getUsername().equals(username))
						{
								user.setUsername(username);
								user = save(user);
						}
				return user;
		}
}
