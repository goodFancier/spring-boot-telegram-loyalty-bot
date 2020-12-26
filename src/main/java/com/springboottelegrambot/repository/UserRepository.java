package com.springboottelegrambot.repository;

import com.springboottelegrambot.model.dto.User;
import com.springboottelegrambot.model.enums.AccessLevels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserRepository extends JpaRepository<User, Integer>
{
		AccessLevels getAccessLevelByUser(User user);

		Optional<User> findById(Long id);

		Boolean isUserHaveAccessForCommand(AccessLevels userAccessLevel, AccessLevels commandAccessLevel);
}
