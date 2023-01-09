package br.com.attornatus.clientes.api.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.attornatus.clientes.api.request.ClienteRequest;
import br.com.attornatus.clientes.api.response.ClienteResponse;
import br.com.attornatus.clientes.api.response.ResponseBody;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class ClienteControllerTests {

	/* Componente para interagir com a API */
	@Autowired
	protected TestRestTemplate restAPI;
	
	// CLIENTE CRUD
	@Test
	void createClienteValid() {

			ClienteRequest request = new ClienteRequest("Gilberto Ramos de Oliveira", LocalDate.now());
			String path = "/cliente";
			
        	ResponseEntity<ResponseBody<ClienteResponse>> response = restAPI.postForEntity( path, request, null);

        	Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void createClienteInValid() {

			ClienteRequest request = new ClienteRequest("", LocalDate.now());

        	ResponseEntity response = restAPI.postForEntity("/cliente", request, null);

        	Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void updateClienteValid() {
		
			ClienteRequest request = new ClienteRequest("Gilberto Ramos de Oliveira", LocalDate.now());
			String path = "/cliente";
			
	    	ResponseEntity<ResponseBody> responseBody = restAPI.postForEntity( path, request, ResponseBody.class);
	    	
	    	ClienteResponse cliente =  new ClienteResponse();

	    	Map entitysHttp = (Map) responseBody.getBody().getMessage();

	    	cliente.setId( UUID.fromString( entitysHttp.get("id").toString() ));
	    		    	
			request = new ClienteRequest("Gilberto Ramos de Oliveira Santos", LocalDate.now());

			path += "/"+ cliente.getId().toString();
			
			restAPI.put( path, request);

        	responseBody = restAPI.getForEntity( path, ResponseBody.class );
        	
        	entitysHttp = (Map) responseBody.getBody().getMessage();
        	
        	cliente.setNome( (String) entitysHttp.get("nome") );
        	
        	Assertions.assertEquals( cliente.getNome() , request.getNome() );
	}

	@Test
	void deleteClienteValid() {
		
			ClienteRequest request = new ClienteRequest("Sidnei de Oliveira", LocalDate.now());
			String path = "/cliente";
			
	    	ResponseEntity<ResponseBody> responseBody = restAPI.postForEntity( path, request, ResponseBody.class);
	    	
	    	ClienteResponse cliente =  new ClienteResponse();

	    	Map entitysHttp = (Map) responseBody.getBody().getMessage();

	    	cliente.setId( UUID.fromString( entitysHttp.get("id").toString() ));
	    		    	
			path += "/"+ cliente.getId().toString();
			
			restAPI.delete( path );

        	responseBody = restAPI.getForEntity( path, ResponseBody.class );
        	
        	Assertions.assertEquals( responseBody.getStatusCode() , HttpStatus.BAD_REQUEST );
	}
	
	@Test
	void deleteClienteInValid() {

	    	String path = "/cliente/"+ UUID.randomUUID().toString();

			restAPI.delete( path );

			ResponseEntity<ResponseBody> responseBody = restAPI.getForEntity( path, ResponseBody.class );

        	Assertions.assertEquals( responseBody.getStatusCode() , HttpStatus.BAD_REQUEST );
	}
}
