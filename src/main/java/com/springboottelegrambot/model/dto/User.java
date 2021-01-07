package com.springboottelegrambot.model.dto;

import com.springboottelegrambot.model.enums.AccessLevels;

import javax.persistence.*;
import java.util.Date;

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

		private String internalCode;

		private Date verified;

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

		public String getInternalCode()
		{
				return internalCode;
		}

		public void setInternalCode(String internalCode)
		{
				this.internalCode = internalCode;
		}

		public Date getVerified()
		{
				return verified;
		}

		public void setVerified(Date verified)
		{
				this.verified = verified;
		}
}
