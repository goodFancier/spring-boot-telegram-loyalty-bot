package com.springboottelegrambot.utils;

public class PhoneUtils
{
		public static String processPhoneNumber(String phone)
		{
				if(phone.contains("+7"))
						phone = phone.replaceFirst("\\+7", "7");
				if(phone.toCharArray()[0] == '8')
						phone = phone.replaceFirst("8", "7");
				return phone;
		}
}
