package br.com.monitoramento.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.monitoramento.exception.FalhaException;
import br.com.monitoramento.request.ConfigRequest;
import br.com.monitoramento.request.CreateUserRequest;
import br.com.monitoramento.request.LoginRequest;

public class UserDomain {
	
	//TODO: Criar persistencia
	public static Map<String, CreateUserRequest> mapUsers = new HashMap<String, CreateUserRequest>();
	private static Map<String, List<ConfigRequest>> mapConfig = new HashMap<String, List<ConfigRequest>>();

	public static List<ConfigRequest> getConfig(String user){
		if(UserDomain.mapConfig == null){
			UserDomain.mapConfig =  new HashMap<String, List<ConfigRequest>>();
		}	
//			UserDomain.config.setMaxValue(1000.0);
//			UserDomain.config.setPeriod(2);
//		}
		return UserDomain.mapConfig.get(user);
	}
	
	public void save(CreateUserRequest user) throws FalhaException{
		if(mapUsers.containsKey(user.getLogin())){
			throw new FalhaException("Usuário já cadastrado!");
		}
		
		mapUsers.put(user.getLogin(), user);
	}
	
	public void login(LoginRequest login) throws FalhaException{
		
		if(!mapUsers.containsKey(login.getLogin()) 
				|| !mapUsers.get(login.getLogin()).getPassword().equals(login.getPassword())){
			throw new FalhaException("Usuário ou senha inválidos!");
		}
	}

	public void config(ConfigRequest config){
		
		if(mapConfig.containsKey(config.getLogin())){
			
			List<ConfigRequest> configs = mapConfig.get(config.getLogin()); 
			if (config.getPeriod().intValue() > 0 && config.getPeriod().intValue() <= 5){
				if (configs == null){
					configs = new ArrayList<ConfigRequest>(5);
				}
				configs.set(config.getPeriod().intValue() -1, config);
				mapConfig.put(config.getLogin(), configs);
			}
		}
	}
	
	public List<ConfigRequest> getConfigs(String user){
		return mapConfig.get(user);
	}
	
	public CreateUserRequest get(String user){
		return mapUsers.get(user);
	}
}
