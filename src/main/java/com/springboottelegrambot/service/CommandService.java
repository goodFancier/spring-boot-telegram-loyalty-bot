package com.springboottelegrambot.service;

import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.model.enums.CommandType;

public interface CommandService
{
		Command findCommandByType(CommandType type);
}
