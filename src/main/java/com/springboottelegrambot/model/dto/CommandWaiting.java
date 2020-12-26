package com.springboottelegrambot.model.dto;

import javax.persistence.*;

@Entity
public class CommandWaiting
{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long recID;

		@Column(name = "chatid", nullable = false)
		private Long chatId;

		@Column(name = "userid", nullable = false)
		private Integer userId;

		@OneToOne(fetch = FetchType.EAGER)
		private Command command;

		@Column(name = "textmessage")
		private String textMessage;

		@Column(name = "isfinished")
		private Boolean isFinished;

		public Long getRecID()
		{
				return recID;
		}

		public void setRecID(Long recID)
		{
				this.recID = recID;
		}

		public Long getChatId()
		{
				return chatId;
		}

		public void setChatId(Long chatId)
		{
				this.chatId = chatId;
		}

		public Integer getUserId()
		{
				return userId;
		}

		public void setUserId(Integer userId)
		{
				this.userId = userId;
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
