package com.springboottelegrambot.service.impl;

import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.model.enums.CommandType;
import com.springboottelegrambot.repository.CommandRepository;
import com.springboottelegrambot.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommandServiceImpl implements CommandService
{
		private final Logger log = LoggerFactory.getLogger(CommandServiceImpl.class);

		private CommandRepository commandRepository;

		public CommandServiceImpl(CommandRepository commandRepository)
		{
				this.commandRepository = commandRepository;
		}


		@Override
		public Command findCommandByType(CommandType type)
		{
				return commandRepository.findCommandByType(type);
		}
}
