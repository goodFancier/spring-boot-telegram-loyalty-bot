package com.springboottelegrambot.model.dto;

import javax.persistence.*;

@Entity
public class CommandWaiting
{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long recID;

		@OneToOne(fetch = FetchType.EAGER)
		private Chat chat;

		@OneToOne(fetch = FetchType.EAGER)
		private User user;

		@OneToOne(fetch = FetchType.EAGER)
		private Command command;

		private String textMessage;

		private Boolean isFinished;

		public Long getRecID()
		{
				return recID;
		}

		public void setRecID(Long recID)
		{
				this.recID = recID;
		}

		public Chat getChat()
		{
				return chat;
		}

		public void setChat(Chat chat)
		{
				this.chat = chat;
		}

		public User getUser()
		{
				return user;
		}

		public void setUser(User user)
		{
				this.user = user;
		}

		public Command getCommand()
		{
				return command;
		}

		public void setCommand(Command command)
		{
				this.command = command;
		}

		public String getTextMessage()
		{
				return textMessage;
		}

		public void setTextMessage(String textMessage)
		{
				this.textMessage = textMessage;
		}

		public Boolean getFinished()
		{
				return isFinished;
		}

		public void setFinished(Boolean finished)
		{
				isFinished = finished;
		}
}
