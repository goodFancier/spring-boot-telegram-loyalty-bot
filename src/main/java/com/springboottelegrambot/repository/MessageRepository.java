package com.springboottelegrambot.repository;

import com.springboottelegrambot.model.dto.Message;
import com.springboottelegrambot.model.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>
{
		Optional<Message> findFirstByUserOrderByDateDesc(User user);
}
