package com.springboottelegrambot.repository;

import com.springboottelegrambot.model.dto.Chat;
import com.springboottelegrambot.model.dto.CommandWaiting;
import com.springboottelegrambot.model.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommandWaitingRepository extends JpaRepository<CommandWaiting, Integer>
{
		Optional<CommandWaiting> findByChatAndUser(Chat chat, User user);
}
