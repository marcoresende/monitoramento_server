package br.com.monitoramento.integration.task;

import java.text.MessageFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import br.com.monitoramento.integration.response.ChannelResponse;


@Component
public class ScheduleIntegration {
	
	private final String URL_BASE = "https://api.thingspeak.com/channels/{0}/feeds.json?timezone=America/Sao_Paulo";
	private final String CHANNEL_ID = "165409";
	
	/**
	 * Verifica a cada determinado periodo(fixedRate) se o consumo ultrapassou o
	 * limite definido por usuario
	 */
	@Scheduled(initialDelay = 3000, fixedRate = 50000)
	public void checkFlow() {
		try {
			send();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void send() {
		RestTemplate restTemplate = new RestTemplate();
		ChannelResponse response = restTemplate.getForObject(MessageFormat.format(URL_BASE, CHANNEL_ID),
				ChannelResponse.class);
		System.out.println(response);
	}

}
