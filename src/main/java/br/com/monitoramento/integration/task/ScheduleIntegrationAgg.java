package br.com.monitoramento.integration.task;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.monitoramento.util.HttpHandler;
import br.com.monitoramento.util.NotificacaoUtil;
@Component
public class ScheduleIntegrationAgg {
	
	private static final String URL_THINGSPEAK = "https://thingspeak.com/channels/173362/feeds.json?api_key=7IFEE5OYI37ZX8GR&results=5";
	private static final String FIELD_VALOR = "field1";
	private static String FIELD_TEMPORALIDADE = "field2";
	
	Logger logger = Logger.getLogger(ScheduleIntegrationAgg.class);
	
	@Scheduled(initialDelay = 3000, fixedRate = 50000)
	public void checkFlow(){
		final String[] temporalidadeArray = {"filler","Minuto","Hora","Dia","Semana","M�s"};
		
		HttpHandler sh = new HttpHandler();
		// Making a request to url and getting response
		String jsonStr = sh.makeHttpGetCall(URL_THINGSPEAK);
		if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray feeds = jsonObj.getJSONArray("feeds");
                for (int i = 0; i < feeds.length(); i++) {
                	JSONObject feed = feeds.getJSONObject(i);
					String temporalidade = feed.getString(FIELD_TEMPORALIDADE );
					String valor = feed.getString(FIELD_VALOR);
					System.out.println(temporalidadeArray[Integer.parseInt(temporalidade)]+" "+valor);
				}
                
            }catch (final JSONException e) {
                System.out.println("Json parsing error: " + e.getMessage());

            }
		}
		//new NotificacaoUtil().sendPushNotification("Alerta de consumo", "O consumo mensal ultrapassou o limite configurado");
	}
}
