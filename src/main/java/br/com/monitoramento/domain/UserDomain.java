package br.com.monitoramento.domain;

import java.util.HashMap;
import java.util.Map;

import br.com.monitoramento.exception.FalhaException;
import br.com.monitoramento.request.ConfigRequest;
import br.com.monitoramento.request.CreateUserRequest;
import br.com.monitoramento.request.LoginRequest;
import org.apache.catalina.User;

public class UserDomain {
	
	//TODO: Criar persistencia
	public static Map<String, CreateUserRequest> mapUsers = new HashMap<String, CreateUserRequest>();
	private static ConfigRequest config = null;

	public static ConfigRequest getConfig(){
		if(UserDomain.config == null){
			UserDomain.config = new ConfigRequest();
			UserDomain.config.setMaxValue(1000.0);
			UserDomain.config.setPeriod(2);
		}
		return UserDomain.config;
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
		UserDomain.config = config;
	}
}
