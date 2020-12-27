package com.springboottelegrambot;

import com.springboottelegrambot.exception.BotException;
import com.springboottelegrambot.model.dto.CommandParent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.springboottelegrambot.utils.NetworkUtils.getFileFromUrl;

public class CommandHandler
{
		private final Logger log = LoggerFactory.getLogger(CommandHandler.class);

		private final PollingBot bot;

		private final CommandParent<?> command;

		private final Update update;

		public CommandHandler(PollingBot bot, CommandParent<?> command, Update update)
		{
				this.bot = bot;
				this.command = command;
				this.update = update;
		}

		public void handle() {
				if (command == null) {
						return;
				}
				log.debug("Find a command {}", command.toString());
				Message message = update.getMessage();
				if (message == null) {
						message = update.getEditedMessage();
						if (message == null) {
								message = update.getCallbackQuery().getMessage();
						}
				}

				SendChatAction sendChatAction = new SendChatAction();
				sendChatAction.setChatId(message.getChatId().toString());
				sendChatAction.setAction(ActionType.TYPING);
				try {
						bot.execute(sendChatAction);
				} catch (TelegramApiException e) {
						log.error("Error: cannot send chat action: {}", e.getMessage());
				}

				try {
						PartialBotApiMethod<?> method = command.parse(update);
						if (method instanceof SendMessage) {
								SendMessage sendMessage = (SendMessage) method;
								log.info("To " + message.getChatId() + ": " + sendMessage.getText());
								bot.execute(sendMessage);
						} else if (method instanceof SendPhoto) {
								SendPhoto sendPhoto = (SendPhoto) method;
								log.info("To " + message.getChatId() + ": sending photo " + sendPhoto.getCaption());
								bot.execute(sendPhoto);
						} else if (method instanceof SendMediaGroup) {
								SendMediaGroup sendMediaGroup = (SendMediaGroup) method;
								log.info("To " + message.getChatId() + ": sending photos " + sendMediaGroup);
								try {
										bot.execute(sendMediaGroup);
								} catch (TelegramApiException e) {
										splitSendMediaGroup(sendMediaGroup);
								}
						} else if (method instanceof EditMessageText) {
								EditMessageText editMessageText = (EditMessageText) method;
								log.info("To " + message.getChatId() + ": edited message " + editMessageText.getText());
								bot.execute(editMessageText);
						} else if (method instanceof SendDocument) {
								SendDocument sendDocument = (SendDocument) method;
								log.info("To " + message.getChatId() + ": sending document " + sendDocument.getCaption());
								bot.execute(sendDocument);
						}
				} catch (TelegramApiException e) {
						log.error("Error: cannot send response: {}", e.getMessage());
				} catch (BotException botException) {
						try {
								SendMessage sendMessage = new SendMessage();
								sendMessage.setReplyToMessageId(message.getMessageId());
								sendMessage.setChatId(message.getChatId().toString());
								sendMessage.setText(botException.getMessage());

								bot.execute(sendMessage);
						} catch (TelegramApiException e) {
								log.error("Error: cannot send response: {}", e.getMessage());
						}
				} catch (Exception e) {
						e.printStackTrace();
				}
		}

		private void splitSendMediaGroup(SendMediaGroup sendMediaGroup) {
				sendMediaGroup.getMedias()
					.forEach(inputMedia -> {
							InputFile inputFile = new InputFile();
							inputFile.setMedia(inputMedia.getMedia());

							SendPhoto sendPhoto = new SendPhoto();
							sendPhoto.setPhoto(inputFile);
							sendPhoto.setReplyToMessageId(sendMediaGroup.getReplyToMessageId());
							sendPhoto.setChatId(sendMediaGroup.getChatId());

							try {
									bot.execute(sendPhoto);
							} catch (TelegramApiException telegramApiException) {
									try {
											sendPhoto.setPhoto(new InputFile(getFileFromUrl(inputMedia.getMedia(), 5000000), "image"));
											bot.execute(sendPhoto);
									} catch (Exception exception) {
											SendMessage sendMessage = new SendMessage();
											sendMessage.setChatId(sendMediaGroup.getChatId());
											sendMessage.setReplyToMessageId(sendMessage.getReplyToMessageId());
											sendMessage.setText("Не удалось загрузить картинку по адресу: " + inputMedia.getMedia());
											try {
													bot.execute(sendMessage);
											} catch (TelegramApiException e) {
													e.printStackTrace();
											}
									}
							}
					});
		}
}
