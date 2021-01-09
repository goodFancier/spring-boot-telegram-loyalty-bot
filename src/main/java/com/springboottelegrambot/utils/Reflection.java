package com.springboottelegrambot.utils;

import com.springboottelegrambot.model.commands.CommandParent;
import com.springboottelegrambot.model.enums.CommandType;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Reflection
{
		public static Class<? extends CommandParent> findCommandByCmdType(CommandType commandType)
		{
				Reflections reflections = new Reflections("com.springboottelegrambot.model.commands", CommandParent.class, new SubTypesScanner(false));
				Set<Class<? extends CommandParent>> commands = reflections.getSubTypesOf(CommandParent.class);
				List<Class<? extends CommandParent>> foundCmds = commands.stream().filter(o -> o.getSimpleName().equals(commandType.name())).collect(Collectors.toList());
				if(foundCmds.isEmpty())
						return null;
				else
						return foundCmds.get(0);
		}
}
