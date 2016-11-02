package br.com.monitoramento.controller;

import java.lang.reflect.Field;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.monitoramento.domain.UserDomain;
import br.com.monitoramento.exception.FalhaException;
import br.com.monitoramento.request.CreateUserRequest;
import br.com.monitoramento.request.LoginRequest;
import br.com.monitoramento.response.BaseResponse;

@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
public class MonitorServiceController {	
	
	@ExceptionHandler(FalhaException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody BaseResponse handleException(FalhaException e) {
		return new BaseResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody BaseResponse handleException(Exception e) {
		return new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocorreu um erro inesperado!");
	}
	
   
    
    @RequestMapping(method = RequestMethod.POST, path="create")
    @ResponseBody
    public ResponseEntity<BaseResponse> createUser(@RequestBody CreateUserRequest request) throws FalhaException {
    	if(!isValidRequest(request)){
    		BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(), "Campos obrigatórios!");
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}
    	UserDomain domain = new UserDomain();
    	domain.save(request);
    	
		BaseResponse response = new BaseResponse(HttpStatus.OK.value(), "Criado com sucesso!");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
    
    @RequestMapping(method = RequestMethod.POST, path="login")
    @ResponseBody
    public ResponseEntity<BaseResponse> login(@RequestBody LoginRequest request) throws FalhaException {
    	if(!isValidRequest(request)){
    		BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(), "Campos obrigatórios!");
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    	}
    	
    	UserDomain domain = new UserDomain();
    	domain.login(request);
    	
		BaseResponse response = new BaseResponse(HttpStatus.OK.value(), "Usuário autenticado com sucesso!");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
    
    private boolean isValidRequest(Object obj){
    	if(obj == null)
    		return false;
    	
    	try{
			for (Field f : obj.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.get(obj) == null || f.get(obj).toString().isEmpty()) {
					return false;
				}
			}
    	} catch(Exception e){
			return false;
		}
		
		return true;
    }
}
