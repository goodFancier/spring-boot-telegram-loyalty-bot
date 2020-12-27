package com.springboottelegrambot.repository;

import com.springboottelegrambot.model.dto.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer>
{
		Optional<Chat> findByRecID(Long id);
}
