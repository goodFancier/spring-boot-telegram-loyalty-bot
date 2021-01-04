package com.springboottelegrambot.service;

import com.springboottelegrambot.model.dto.Command;

public interface CommandService
{
		Command findCommandInText(String textOfMessage, String botUsername);

		Command findCommandByName(String name);
}
