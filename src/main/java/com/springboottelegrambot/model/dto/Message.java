package com.springboottelegrambot.model.dto;

import com.springboottelegrambot.model.enums.CommandType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Message
{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long recID;

		private String TextOfMessage;

		private Date date;

		@Column(columnDefinition = "enum('InitLoginScreen', 'RequestSmsCode', 'Card', 'Profile', 'Summary', 'Offers', 'CheckDisposablePassword')")
		@Enumerated(EnumType.STRING)
		private CommandType commandType;

		@JoinColumn(name = "user")
		@OneToOne(fetch = FetchType.EAGER)
		private User user;

		public Long getRecID()
		{
				return recID;
		}

		public void setRecID(Long recID)
		{
				this.recID = recID;
		}

		public String getTextOfMessage()
		{
				return TextOfMessage;
		}

		public void setTextOfMessage(String textOfMessage)
		{
				TextOfMessage = textOfMessage;
		}

		public Date getDate()
		{
				return date;
		}

		public void setDate(Date date)
		{
				this.date = date;
		}

		public CommandType getCommandType()
		{
				return commandType;
		}

		public void setCommandType(CommandType commandType)
		{
				this.commandType = commandType;
		}

		public User getUser()
		{
				return user;
		}

		public void setUser(User user)
		{
				this.user = user;
		}

		public Message()
		{
		}

		public Message(String textOfMessage, Date date, CommandType commandType, User user)
		{
				TextOfMessage = textOfMessage;
				this.date = date;
				this.commandType = commandType;
				this.user = user;
		}
}
