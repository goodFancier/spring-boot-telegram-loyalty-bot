package com.springboottelegrambot.repository;

import com.springboottelegrambot.model.dto.User;
import com.springboottelegrambot.model.enums.AccessLevels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserRepository extends JpaRepository<User, Integer>
{
		AccessLevels getAccessLevelByRecID(Long id);

		Optional<User> findByRecID(Long id);
}
