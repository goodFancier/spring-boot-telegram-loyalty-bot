package com.springboottelegrambot.utils;

import com.springboottelegrambot.model.dto.Command;
import com.springboottelegrambot.model.dto.CommandParent;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Reflection
{
		public static Class<? extends CommandParent> findCommandByCmdType(Command command)
		{
				Reflections reflections = new Reflections("com.springboottelegrambot.model.commands", CommandParent.class, new SubTypesScanner(false));
				Set<Class<? extends CommandParent>> commands = reflections.getSubTypesOf(CommandParent.class);
				List<Class<? extends CommandParent>> foundCmds = commands.stream().filter(o -> o.getSimpleName().equals(command.getType().name())).collect(Collectors.toList());
				if(foundCmds.isEmpty())
						return null;
				else
						return foundCmds.get(0);
		}
}
