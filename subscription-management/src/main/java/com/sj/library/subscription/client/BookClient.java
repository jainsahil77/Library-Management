package com.sj.library.subscription.client;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

/**
 * A bean for providing web client object for sending requests to book service
 * 
 * @author Sahil Jain
 *
 */
@Component
public class BookClient {
	@Value("${book.service.url}")
	private String bookServiceUrl;

	/**
	 * Get web client for Book service with 5 seconds read and write time out and 10
	 * seconds connection time out
	 * 
	 * @return
	 */
	@Bean
	public WebClient getWebClient() {
		HttpClient httpClient = HttpClient.create().tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
				.doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS)).addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS))));

		ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

		return WebClient.builder().baseUrl(bookServiceUrl).clientConnector(connector).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}
}
