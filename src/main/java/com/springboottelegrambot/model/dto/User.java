package com.springboottelegrambot.model.dto;

import com.springboottelegrambot.model.enums.AccessLevels;

import javax.persistence.*;

@Entity
public class User
{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long recID;

		@Column(name = "username")
		private String username;

		@Column(columnDefinition = "enum('BANNED', 'NEWCOMER', 'FAMILIAR', 'TRUSTED', 'MODERATOR', 'ADMIN')")
		@Enumerated(EnumType.STRING)
		private AccessLevels accessLevel;

		public Long getRecID()
		{
				return recID;
		}

		public void setRecID(Long recID)
		{
				this.recID = recID;
		}

		public String getUsername()
		{
				return username;
		}

		public void setUsername(String username)
		{
				this.username = username;
		}

		public AccessLevels getAccessLevel()
		{
				return accessLevel;
		}

		public void setAccessLevel(AccessLevels accessLevel)
		{
				this.accessLevel = accessLevel;
		}
}
