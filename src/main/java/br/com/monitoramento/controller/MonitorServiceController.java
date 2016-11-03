package br.com.monitoramento.controller;

import br.com.monitoramento.domain.UserDomain;
import br.com.monitoramento.exception.FalhaException;
import br.com.monitoramento.request.ConfigRequest;
import br.com.monitoramento.request.CreateUserRequest;
import br.com.monitoramento.request.LoginRequest;
import br.com.monitoramento.response.BaseResponse;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;

@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
public class MonitorServiceController {

    private Logger logger = Logger.getLogger(MonitorServiceController.class);
	
	@ExceptionHandler(FalhaException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody BaseResponse handleException(FalhaException e) {
        logger.error(e.getMessage(), e);
		return new BaseResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody BaseResponse handleException(Exception e) {
        logger.error("Ocorreu um erro inesperado!", e);
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

    @RequestMapping(method = RequestMethod.POST, path="config")
    @ResponseBody
    public ResponseEntity<BaseResponse> login(@RequestBody ConfigRequest request) throws FalhaException {
        if(!isValidRequest(request)){
            BaseResponse response = new BaseResponse(HttpStatus.BAD_REQUEST.value(), "Campos obrigatórios!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        UserDomain domain = new UserDomain();
        domain.config(request);

        BaseResponse response = new BaseResponse(HttpStatus.OK.value(), "Configurações alteradas com sucesso!");
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
