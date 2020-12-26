package com.springboottelegrambot.repository;

import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.model.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<User, Integer>
{
		Command findByCommandNameInOrDescription(String textMessage);

		Integer getAccessLevelForCommand(String className);

		List<Command> getAvailableCommandsForLevel(Integer accessLevel);

		Command findCommandByName(String name);
}
