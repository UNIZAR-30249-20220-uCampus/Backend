package com.example.demo.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import domainObjects.request.ReservaRequest;
import es.ucampus.demo.ServidorWebSpringApplication;
import es.ucampus.demo.adapter.AdapterReservas;
import es.ucampus.demo.controller.ReservasController;
import org.json.simple.JSONArray;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ServidorWebSpringApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservasControllerTest {

	private String QUEUE_ENVIAR = "TestReservasEnviarWebServer";
	private String QUEUE_RECIBIR = "TestReservasRecibirWebServer";
	private ReservasController reservasController;
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://laxmuumj:ivRgGMHAsnl088kdlEWhskufGJSGsbkf@stingray.rmq.cloudamqp.com/laxmuumj";
	private Connection connection;
	private Channel channel;

	private AdapterReservas adapterReservas;

	/*
	 * Se realiza la conexion con Rabbitmq
	 */
	@Before
	public void beforeEveryTest() throws Exception {
		adapterReservas = new AdapterReservas(QUEUE_ENVIAR, QUEUE_RECIBIR);
		reservasController = new ReservasController(adapterReservas);

		ConnectionFactory factory = new ConnectionFactory();
		String amqpURL = System.getenv().get(ENV_AMQPURL_NAME) != null ? System.getenv().get(ENV_AMQPURL_NAME)
				: CredencialCloudAMQP;
		try {
			factory.setUri(amqpURL);
		} catch (Exception e) {
			System.exit(-1);
		}
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(QUEUE_ENVIAR, false, false, false, null); // Cola donde se actuarÃ¡ de emisor
		channel.queueDeclare(QUEUE_RECIBIR, false, false, false, null); // Cola donde se actuará de receptor
	}

	/*
	 * Se cierra la conexion con Rabbitmq
	 */
	@After
	public void afterEveryTest() throws IOException {
		adapterReservas.cerrarConexionAMQP();
	}

	/*
	 *	Se verifica que la clase es no nula
	 */
	@Test
	public void contexLoads() throws Exception {
		assertThat(reservasController).isNotNull();
	}

	/*
	 *	Se verifica la correcta creacion de una reserva
	 */
	@Test
	public void test_GET_CREAR_RESERVA() throws Exception {
		String msg = "Reservada";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

		String json = "{" + "'tipo': 'reserva'," + "'usuario' : 'Alex'," + "'horario' : {"
				+ "'fechaInicio' : '2020-05-30T08:20:38.5426521-04:00',"
				+ "'fechaFin' : '2020-05-30T08:20:38.5426521-06:00'," + "'frecuencia' : 2," + "'conjuntoDiaSlots' : [{"
				+ "'diaSemana' : 3," + "'slotInicio' : 1," + "'slotFinal': 4" + "}]" + "}" + "}";

		// Now do the magic.
		ReservaRequest reserva = new Gson().fromJson(json, ReservaRequest.class);

		ResponseEntity<String> result = reservasController.crearReserva("\"CRE.1200.01.050\"", reserva);
		assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}

	/*
	 *	Se verifica la correcta obtencion de todas las reserva de un espacio
	 */
	@Test
	public void test_GET_RESERVAS() throws Exception {
		JSONArray jsonArray = new JSONArray();
		String msg = jsonArray.toJSONString();
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

		ResponseEntity<JSONArray> result = reservasController.getReservas("\"CRE.1200.01.050\"");
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	/*
	 *	Se verifica la correcta obtencion de las reserva pendientes de un espacio
	 */
	@Test
	public void test_GET_RESERVAS_ESTADO() throws Exception {
		JSONArray jsonArray = new JSONArray();
		String msg = jsonArray.toJSONString();
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

		ResponseEntity<JSONArray> result = reservasController.getReservas("\"CRE.1200.01.050\"", "PENDIENTE");
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	/*
	 *	Se verifica que dado un espacio no existente no se obtienen reservas
	 */
	@Test
	public void test_GET_RESERVAS_ERROR() throws Exception {
		String msg = "No encontrado";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

		ResponseEntity<JSONArray> result = reservasController.getReservas("CRE.1065.00.021");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	/*
	 *	Se verifica que dado un espacio no existente y un estado de reserva no se obtienen reservas
	 */
	@Test
	public void test_GET_RESERVAS_ESTADO_ERROR() throws Exception {
		String msg = "No encontrado";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

		ResponseEntity<JSONArray> result = reservasController.getReservas("CRE.1065.00.021", "PENDIENTE");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	/*
	 *	Se verifica la correcta obtencion de todas las reserva de un usuario
	 */
	@Test
	public void test_GET_RESERVAS_USUARIO() throws Exception {
		JSONArray jsonArray = new JSONArray();
		String msg = jsonArray.toJSONString();
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

		ResponseEntity<JSONArray> result = reservasController.getReservasUsuario("Alex");
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	/*
	 *	Se verifica la correcta obtencion de las reserva pendientes de un usuario
	 */
	@Test
	public void test_GET_RESERVAS_USUARIO_ESTADO() throws Exception {
		JSONArray jsonArray = new JSONArray();
		String msg = jsonArray.toJSONString();
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

		ResponseEntity<JSONArray> result = reservasController.getReservasUsuarioEstado("Alex", "PENDIENTE");
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	/*
	 *	Se verifica que dada un id de reserva no existente no se puede aceptar una reserva
	 */
	@Test
	public void test_ACEPTAR_RESERVA_ERROR() throws Exception {
		String msg = "Reserva no encontrada";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());
		ResponseEntity<String> result = reservasController.aceptarReserva("-1");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	/*
	 *	Se verifica que dada un id de reserva no existente no se puede cancelar una reserva
	 */
	@Test
	public void test_CANCELAR_RESERVA_ERROR() throws Exception {
		String msg = "Reserva no encontrada";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());
		ResponseEntity<String> result = reservasController.cancelarReserva("-1");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	/*
	 *	Se verifica que dada un id de reserva no existente no se puede pagar una reserva
	 */
	@Test
	public void test_PAGAR_RESERVA_ERROR() throws Exception {
		String msg = "Reserva no encontrada";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());
		ResponseEntity<String> result = reservasController.pagarReserva("-1");
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

}