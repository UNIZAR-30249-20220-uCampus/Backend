package com.example.demo.controller;

import static org.junit.Assert.assertEquals;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;
import es.ucampus.demo.ServidorWebSpringApplication;
import es.ucampus.demo.controller.EspacioController;
import org.json.simple.JSONArray;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServidorWebSpringApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class EspacioControllerTest {

	@Autowired
	private EspacioController espaciosController;

	@Test
	public void contexLoads() throws Exception {
		assertThat(espaciosController).isNotNull();
	}

	@Test
	public void test_GET_CONEXION() throws Exception {

        ResponseEntity<String> result = espaciosController.conexion1();
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	
	@Test
    public void test_GET_ESPACIOS() throws Exception {
		
        ResponseEntity<EspacioDTO> result = espaciosController.getSpace(1, 675745.92064, 4616800.60363);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
    public void test_GET_ESPACIOS_ERROR() throws Exception {
		
        ResponseEntity<EspacioDTO> result = espaciosController.getSpace(7, 675745.92064, 4616800.60363);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}


	@Test
	@Ignore
	public void test_GET_BUSCAR_ESPACIOS() throws Exception {
		
	}
	
}