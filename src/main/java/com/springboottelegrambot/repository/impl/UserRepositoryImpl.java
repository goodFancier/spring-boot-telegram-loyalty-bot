package com.springboottelegrambot.repository.impl;

import com.springboottelegrambot.model.dto.User;
import com.springboottelegrambot.model.enums.AccessLevels;
import com.springboottelegrambot.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public abstract class UserRepositoryImpl implements UserRepository
{
		@Override
		public AccessLevels getAccessLevelByUser(User user)
		{
				return null;
		}

		@Override
		public Optional<User> findById(Long id)
		{
				return Optional.empty();
		}

		@Override
		public Boolean isUserHaveAccessForCommand(AccessLevels userAccessLevel, AccessLevels commandAccessLevel)
		{
				return null;
		}
}
