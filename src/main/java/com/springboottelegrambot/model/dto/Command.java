package com.springboottelegrambot.model.dto;

import com.springboottelegrambot.model.enums.AccessLevels;
import com.springboottelegrambot.model.enums.CommandType;

import javax.persistence.*;

@Entity
public class Command
{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long recID;

		private String description;

		@Column(columnDefinition = "enum('BANNED', 'NEWCOMER', 'FAMILIAR', 'TRUSTED', 'MODERATOR', 'ADMIN')")
		@Enumerated(EnumType.STRING)
		private AccessLevels accessLevel;

		@Column(columnDefinition = "enum('InitLoginScreen', 'RequestSmsCode', 'Моя карта', 'Profile', 'Summary', 'Offers')")
		@Enumerated(EnumType.STRING)
		private CommandType type;

		public Long getRecID()
		{
				return recID;
		}

		public void setRecID(Long recID)
		{
				this.recID = recID;
		}

		public String getDescription()
		{
				return description;
		}

		public void setDescription(String description)
		{
				this.description = description;
		}

		public AccessLevels getAccessLevel()
		{
				return accessLevel;
		}

		public void setAccessLevel(AccessLevels accessLevel)
		{
				this.accessLevel = accessLevel;
		}

		public CommandType getType()
		{
				return type;
		}

		public void setType(CommandType type)
		{
				this.type = type;
		}
}
