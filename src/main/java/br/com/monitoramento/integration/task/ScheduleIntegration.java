package br.com.monitoramento.integration.task;

import java.text.MessageFormat;
import java.util.List;

import br.com.monitoramento.enums.PeriodEnum;
import br.com.monitoramento.integration.dto.Feed;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import br.com.monitoramento.integration.response.ChannelResponse;


@Component
public class ScheduleIntegration {
	
	private final String URL_BASE = "https://api.thingspeak.com/channels/{0}/feeds.json?timezone=America/Sao_Paulo";
	private final String CHANNEL_ID = "165409";
    private final Integer PERIOD = 2; //TODO: criar configuração de período
    private final Double MAX_VAL = 50.0; //TODO: Criar configuração de valor.
	
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
        String url = MessageFormat.format(URL_BASE, CHANNEL_ID) + getPeriodUrlParameter();

		RestTemplate restTemplate = new RestTemplate();
		ChannelResponse response = restTemplate.getForObject(url,
				ChannelResponse.class);

		if(response != null && isAboveLimit(response.getFeeds())){
			sendNotification();
		}
	}

	private boolean isAboveLimit(List<Feed> feeds){
		if(feeds == null || feeds.isEmpty()){
			return false;
		}
		Double sum = 0.0;

		for(Feed f : feeds){
			if(f.getField1() != null && !f.getField1().isEmpty()){
				try {
					sum += Double.valueOf(f.getField1());
				}catch (NumberFormatException ne){
					ne.printStackTrace();
				}
			}
		}

		return sum > MAX_VAL ? true : false;
	}

	private void sendNotification(){}

    private String getPeriodUrlParameter(){
        if(PeriodEnum.MINUTO.getId().equals(PERIOD)){
            return "&minutes=1";
        } else if(PeriodEnum.HORA.getId().equals(PERIOD)){
            return "&minutes=60&sum=60";
        } else if(PeriodEnum.DIA.getId().equals(PERIOD)){
            return "&days=1&sum=daily";
        } else if(PeriodEnum.SEMANA.getId().equals(PERIOD)){
            return "&days=7&sum=daily";
        } else {
            return "&days=30&sum=daily";
        }
    }
}
