package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import domainObjects.request.ReservaRequest;
import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;
import es.ucampus.demo.ServidorWebSpringApplication;
import es.ucampus.demo.controller.ReservasController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServidorWebSpringApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservasControllerTest {

	@Autowired
	private ReservasController reservasController;

	@Test
	public void contexLoads() throws Exception {
		assertThat(reservasController).isNotNull();
	}

	@Test
    public void test_GET_CREAR_RESERVA() throws Exception {

		String json = 
            "{"
                + "'tipo': 'reserva',"
                + "'usuario' : 'Alex',"
                + "'horario' : {"
                    + "'fechaInicio' : '2020-05-30T08:20:38.5426521-04:00',"
                    + "'fechaFin' : '2020-05-30T08:20:38.5426521-06:00',"
                    + "'frecuencia' : 2,"
                    + "'conjuntoDiaSlots' : [{"
                        + "'diaSemana' : 3,"
                        + "'slotInicio' : 1,"
                        + "'slotFinal': 4"
                    + "}]" 
                + "}"
            + "}";

        // Now do the magic.
		ReservaRequest reserva = new Gson().fromJson(json, ReservaRequest.class);

        ResponseEntity<String> result = reservasController.crearReserva("\"CRE.1200.01.050\"", reserva);
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}

	@Test
    public void test_GET_RESERVAS() throws Exception {
		
        ResponseEntity<JSONArray> result = reservasController.getReservas("\"CRE.1200.01.050\"");
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
    public void test_GET_RESERVAS_ESTADO() throws Exception {
		
        ResponseEntity<JSONArray> result = reservasController.getReservas("\"CRE.1200.01.050\"", "PENDIENTE");
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
    public void test_GET_RESERVAS_ERROR() throws Exception {
		
        ResponseEntity<JSONArray> result = reservasController.getReservas("CRE.1065.00.021");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	@Test
    public void test_GET_RESERVAS_ESTADO_ERROR() throws Exception {
		
        ResponseEntity<JSONArray> result = reservasController.getReservas("CRE.1065.00.021", "PENDIENTE");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	@Test
    public void test_GET_RESERVAS_USUARIO() throws Exception {
		
        ResponseEntity<JSONArray> result = reservasController.getReservasUsuario("Alex");
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
    public void test_GET_RESERVAS_USUARIO_ESTADO() throws Exception {
		
        ResponseEntity<JSONArray> result = reservasController.getReservasUsuarioEstado("Alex", "PENDIENTE");
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	@Ignore
    public void test_ACEPTAR_RESERVA() throws Exception {
		ResponseEntity<JSONArray> reservas = reservasController.getReservas("\"CRE.1200.01.050\"");
		JSONArray rs  = reservas.getBody();
		String idReserva = "";
		rs.forEach(item -> {
			JSONObject obj = (JSONObject) item;
			//idReserva = (String) obj.get("id");
		});

        //ResponseEntity<String> result = reservasController.aceptarReserva(idReserva);
		//assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
    public void test_ACEPTAR_RESERVA_ERROR() throws Exception {
        ResponseEntity<String> result = reservasController.aceptarReserva("-1");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	@Test
    public void test_CANCELAR_RESERVA_ERROR() throws Exception {
        ResponseEntity<String> result = reservasController.aceptarReserva("-1");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	@Test
    public void test_PAGAR_RESERVA_ERROR() throws Exception {
        ResponseEntity<String> result = reservasController.aceptarReserva("-1");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}
	
}