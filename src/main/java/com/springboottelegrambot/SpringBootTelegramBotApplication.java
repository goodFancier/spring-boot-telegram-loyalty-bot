package com.springboottelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.springboottelegrambot.utils.FileUtils.checkPropertiesFileExists;

@SpringBootApplication
@EnableScheduling
public class SpringBootTelegramBotApplication
{
		public static void main(String[] args)
		{
				checkPropertiesFileExists();
				SpringApplication.run(SpringBootTelegramBotApplication.class, args);
		}
}
