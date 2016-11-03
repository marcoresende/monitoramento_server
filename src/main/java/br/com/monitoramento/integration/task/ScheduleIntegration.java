package br.com.monitoramento.integration.task;

import br.com.monitoramento.domain.UserDomain;
import br.com.monitoramento.enums.PeriodEnum;
import br.com.monitoramento.integration.dto.Feed;
import br.com.monitoramento.integration.response.ChannelResponse;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;


@Component
public class ScheduleIntegration {

    Logger logger = Logger.getLogger(ScheduleIntegration.class);

    private final String URL_BASE = "https://api.thingspeak.com/channels/{0}/feeds.json?timezone=America/Sao_Paulo";
    private final String DEFAULT_PARAM = "&days=30&sum=daily";
	private final String CHANNEL_ID = "165409";//TODO: Criar configuração.
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
			logger.error("Erro ao veriicar consumo!", e);
		}

	}

	public void send() {
        String url = MessageFormat.format(URL_BASE, CHANNEL_ID) + getPeriodUrlParameter();

        logger.debug("Chamando API ThingSpeak: " + url);
		RestTemplate restTemplate = new RestTemplate();
		ChannelResponse response = restTemplate.getForObject(url,
				ChannelResponse.class);

		if(response != null && isAboveLimit(response.getFeeds())){
            logger.debug("Enviando notificação push...");
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
                    logger.error("Erro ao converter valor Field1!", ne);
				}
			}
		}

		return sum > MAX_VAL ? true : false;
	}

	private void sendNotification(){}

    private String getPeriodUrlParameter(){
        //TODO: remover
        Integer period = UserDomain.getConfig().getPeriod();

        PeriodEnum periodEnum = PeriodEnum.getById(period);
        return periodEnum != null ? periodEnum.getParam() : DEFAULT_PARAM;
    }
}
