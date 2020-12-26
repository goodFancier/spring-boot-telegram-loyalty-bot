package com.springboottelegrambot.repository;

import com.springboottelegrambot.model.dto.Chat;
import com.springboottelegrambot.model.dto.CommandWaiting;
import com.springboottelegrambot.model.dto.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommandWaitingRepository
{
		Optional<CommandWaiting> findByChatAndUser(Chat chat, User user);
}
