package com.example.demo.controller;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;

public class EspacioControllerTest {

	private TestRestTemplate restTemplate = new TestRestTemplate();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private static final String BASE_URL = "http://localhost:7080/api";

	@Test
    public void test_GET_API_CONEXION() {

		ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "/conexion", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
    public void test_GET_API_ESPACIOS() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		ResponseEntity<EspacioDTO> response = restTemplate.getForEntity(BASE_URL + "/espacios/1/675745.92064/4616800.60363", EspacioDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
    public void test_GET_API_ESPACIOSID() {
		
		ResponseEntity<EspacioDTO> response = restTemplate.getForEntity(BASE_URL + "/espacios/\"CRE.1065.00.020\"", EspacioDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
    public void test_POST_API_BUSCAR_ESPACIOS() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		
		CriteriosBusquedaDTO criterios = new CriteriosBusquedaDTO();
		HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(criterios), headers);
		
        ResponseEntity response = restTemplate.postForEntity(BASE_URL + "/buscar-espacio", entity, Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
	
}