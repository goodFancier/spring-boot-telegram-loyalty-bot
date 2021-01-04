package com.springboottelegrambot.service.impl;

import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.repository.CommandRepository;
import com.springboottelegrambot.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.springboottelegrambot.utils.TextUtils.getPotentialCommandInText;

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
		public Command findCommandInText(String textOfMessage, String botUsername)
		{
				log.debug("Request to find commands in text {}", textOfMessage);
				int i = textOfMessage.indexOf("@");
				if(i > 0 && textOfMessage.indexOf(botUsername) > 0)
				{
						if(!textOfMessage.substring(i + 1).equals(botUsername))
						{
								return null;
						}
						else
						{
								textOfMessage = textOfMessage.replace("@" + botUsername, "");
						}
				}
				return findCommandByName(getPotentialCommandInText(textOfMessage));
		}

		@Override
		public Command findCommandByName(String name)
		{
				log.debug("Request to get command propertiest by name {}", name);
				return commandRepository.findByCommandName(name);
		}
}
