package com.springboottelegrambot.payload;

import com.springboottelegrambot.model.enums.GenderEnum;

public class Buyer
{
		private String name;

		/**
		 * Пол.
		 */
		private GenderEnum gender;

		/**
		 * Дата рождения.
		 */
		private String birthday;

		/**
		 * Электронная почта.
		 */
		private String eMail;

		/**
		 * Наличие автомобиля.
		 */
		private Boolean hasCar;

		/**
		 * Количество детей
		 */
		private Integer children;

		private String links;

		/**
		 * Телефон.
		 */
		private String phone;

		/**
		 * Наличие животных
		 */
		private Boolean hasPet;

		/**
		 * Признак верификации
		 */
		private String verified;

		/**
		 * Признак верификации email
		 */
		private String emailVerified;

		/**
		 * Токен безопасности
		 */
		private String token;

		/**
		 * Пароль
		 */
		private String password;

		public String getName()
		{
				return name;
		}

		public void setName(String name)
		{
				this.name = name;
		}

		public GenderEnum getGender()
		{
				return gender;
		}

		public void setGender(GenderEnum gender)
		{
				this.gender = gender;
		}

		public String geteMail()
		{
				return eMail;
		}

		public void seteMail(String eMail)
		{
				this.eMail = eMail;
		}

		public Boolean getHasCar()
		{
				return hasCar;
		}

		public void setHasCar(Boolean hasCar)
		{
				this.hasCar = hasCar;
		}

		public Integer getChildren()
		{
				return children;
		}

		public void setChildren(Integer children)
		{
				this.children = children;
		}

		public String getLinks()
		{
				return links;
		}

		public void setLinks(String links)
		{
				this.links = links;
		}

		public String getPhone()
		{
				return phone;
		}

		public void setPhone(String phone)
		{
				this.phone = phone;
		}

		public Boolean getHasPet()
		{
				return hasPet;
		}

		public void setHasPet(Boolean hasPet)
		{
				this.hasPet = hasPet;
		}

		public String getToken()
		{
				return token;
		}

		public void setToken(String token)
		{
				this.token = token;
		}

		public String getPassword()
		{
				return password;
		}

		public void setPassword(String password)
		{
				this.password = password;
		}

		public String getBirthday()
		{
				return birthday;
		}

		public void setBirthday(String birthday)
		{
				this.birthday = birthday;
		}

		public String getVerified()
		{
				return verified;
		}

		public void setVerified(String verified)
		{
				this.verified = verified;
		}

		public String getEmailVerified()
		{
				return emailVerified;
		}

		public void setEmailVerified(String emailVerified)
		{
				this.emailVerified = emailVerified;
		}
}
