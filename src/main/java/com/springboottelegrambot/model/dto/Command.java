package com.springboottelegrambot.model.dto;

import com.springboottelegrambot.model.enums.AccessLevels;

import javax.persistence.*;

@Entity
public class Command
{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long recID;

		private String commandName;

		private String description;

		private String className;

		@Column(columnDefinition = "enum('BANNED', 'NEWCOMER', 'FAMILIAR', 'TRUSTED', 'MODERATOR', 'ADMIN')")
		@Enumerated(EnumType.STRING)
		private AccessLevels accessLevel;

		private String help;

		public Long getRecID()
		{
				return recID;
		}

		public void setRecID(Long recID)
		{
				this.recID = recID;
		}

		public String getCommandName()
		{
				return commandName;
		}

		public void setCommandName(String commandName)
		{
				this.commandName = commandName;
		}

		public String getDescription()
		{
				return description;
		}

		public void setDescription(String description)
		{
				this.description = description;
		}

		public String getClassName()
		{
				return className;
		}

		public void setClassName(String className)
		{
				this.className = className;
		}

		public AccessLevels getAccessLevel()
		{
				return accessLevel;
		}

		public void setAccessLevel(AccessLevels accessLevel)
		{
				this.accessLevel = accessLevel;
		}

		public String getHelp()
		{
				return help;
		}

		public void setHelp(String help)
		{
				this.help = help;
		}
}
