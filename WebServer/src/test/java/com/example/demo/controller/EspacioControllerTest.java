package com.example.demo.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import dtoObjects.entity.EspacioDTO;
import es.ucampus.demo.ServidorWebSpringApplication;
import es.ucampus.demo.adapter.AdapterEspacios;
import es.ucampus.demo.controller.EspacioController;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.Connection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ServidorWebSpringApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class EspacioControllerTest {

	private String QUEUE_ENVIAR = "TestEspaciosEnviarWebServer";
	private String QUEUE_RECIBIR = "TestEspaciosRecibirWebServer";
	private EspacioController espacioController;
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://atyfvbtb:0k8H_vr2RIrXbzEq6dXwKNXF3Xu4J5F3@chinook.rmq.cloudamqp.com/atyfvbtb";
	private Connection connection;
	private Channel channel;

	private AdapterEspacios adapterEspacios;

	/*
	 * Se realiza la conexion con Rabbitmq
	 */
	@Before
	public void beforeEveryTest() throws Exception {
		adapterEspacios = new AdapterEspacios(QUEUE_ENVIAR, QUEUE_RECIBIR);
		espacioController = new EspacioController(adapterEspacios);

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
		boolean durable = true;
		channel.queueDeclare(QUEUE_ENVIAR, durable, false, false, null); // Cola donde se actuarÃ¡ de emisor
		channel.queueDeclare(QUEUE_RECIBIR, durable, false, false, null); // Cola donde se actuará de receptor
	}

	/*
	 * Se cierra la conexion con Rabbitmq
	 */
	@After
	public void afterEveryTest() throws IOException {
		adapterEspacios.cerrarConexionAMQP();
	}

	/*
	 *	Se verifica que la clase es no nula
	 */
	@Test
	public void contexLoads() throws Exception {
		assertThat(espacioController).isNotNull();
	}

	/*
	 *	Se verifica la conexion del servidor
	 */
	@Test
	public void test_GET_CONEXION() throws Exception {
		ResponseEntity<String> result = espacioController.conexion1();
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	/*
	 *	Se verifica la obtencion de un espacio dada una planta y sus coordenadas
	 */
	@Test
	public void test_GET_ESPACIOS() throws Exception {
		EspacioDTO espacioDTO = new EspacioDTO();
		ObjectMapper mapper = new ObjectMapper();
		String msg = mapper.writeValueAsString(espacioDTO);
		channel.basicPublish("", QUEUE_RECIBIR, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

		ResponseEntity<EspacioDTO> result = espacioController.getEspacio(1, 675745.92064, 4616800.60363);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	/*
	 *	Se verifica el error a la hora de obetener un espacio dada una planta y coordenadas erroneas
	 */
	@Test
	public void test_GET_ESPACIOS_ERROR() throws Exception {
		String msg = "No encontrado";
		channel.basicPublish("", QUEUE_RECIBIR, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

		ResponseEntity<EspacioDTO> result = espacioController.getEspacio(7, 675745.92064, 4616800.60363);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	/*
	 *	Se verifica que permite cambiar un espacio a no reservable
	 */
	@Test
	public void test_PUT_ESPACIOS_RESERVABLE() throws Exception {
		String msg = "OK";
		channel.basicPublish("", QUEUE_RECIBIR, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

		ResponseEntity<String> result = espacioController.cambiarReservable("\"CRE.1065.02.200\"", 0);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	/*
	 *	Se verifica que no permite cambiar un espacio no exisatente a no reservable
	 */
	@Test
	public void test_PUT_ESPACIOS_RESERVABLE_ERROR() throws Exception {
		String msg = "No encontrado";
		channel.basicPublish("", QUEUE_RECIBIR, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

		ResponseEntity<String> result = espacioController.cambiarReservable("\"CRE.1065.02.200\"", 0);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

	/*
	 *	Se verifica que permite cambiar un espacio a no alquilable
	 */
	@Test
	public void test_PUT_ESPACIOS_ALQUILABLE() throws Exception {
		String msg = "OK";
		channel.basicPublish("", QUEUE_RECIBIR, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

		ResponseEntity<String> result = espacioController.cambiarAlquilable("\"CRE.1065.02.200\"", 0);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	/*
	 *	Se verifica que no permite cambiar un espacio no existente a no alquilable
	 */
	@Test
	public void test_PUT_ESPACIOS_ALQUILABLE_ERROR() throws Exception {
		String msg = "No encontrado";
		channel.basicPublish("", QUEUE_RECIBIR, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

		ResponseEntity<String> result = espacioController.cambiarAlquilable("\"CRE.1065.02.200\"", 0);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}

}