package com.springboottelegrambot.repository;

import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.model.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<Command, Integer>
{
		Command findByCommandNameOrDescription(String commandName, String description);

		Command findByCommandName(@Param("commandName") String commandName);
}
