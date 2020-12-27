package com.springboottelegrambot.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;

public class NetworkUtils
{
		public static InputStream getFileFromUrl(String url, int limitBytes) throws Exception
		{
				byte[] file = downloadFile(url);
				if(file.length > limitBytes)
				{
						throw new Exception("the file is not included in the limit");
				}
				return new ByteArrayInputStream(Objects.requireNonNull(file));
		}

		private static byte[] downloadFile(String url)
		{
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("User-Agent", "Mozilla/4.0");
				HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
				ResponseEntity<byte[]> respEntity = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
				return respEntity.getBody();
		}
}
