package com.springboottelegrambot.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class ApacheHttp
{
		public String sendGetRequest(String url) throws IOException
		{
				StringBuilder res = new StringBuilder();
				BufferedReader rd = null;
				try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.DEFAULT).build())
				{
						HttpRequestBase request = new HttpGet(url);
						HttpResponse response = httpClient.execute(request);
						if (response.getEntity() == null)
							return null;
						rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
						String line = "";
						while((line = rd.readLine()) != null)
								res.append(line);
				}
				catch(IOException e)
				{
						throw new IOException(String.format("ошибка при отправке GET запроса '%s'", url), e);
				}
				finally
				{
						if(rd != null)
								rd.close();
				}
				return res.toString();
		}
}
