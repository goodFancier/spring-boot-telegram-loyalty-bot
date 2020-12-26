package com.springboottelegrambot.model.dto;

import com.springboottelegrambot.model.enums.AccessLevels;

import javax.persistence.*;

@Entity
public class Chat
{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long recID;

		@Column(name = "name")
		private String name;

		@Column(name = "accesslevel")
		private AccessLevels accessLevel;
}
