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
import com.rabbitmq.client.Connection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ServidorWebSpringApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class EspacioControllerTest {

	private String QUEUE_ENVIAR = "TestEspaciosEnviarWebServer";
	private String QUEUE_RECIBIR = "TestEspaciosRecibirWebServer";
	private EspacioController espacioController;
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://laxmuumj:ivRgGMHAsnl088kdlEWhskufGJSGsbkf@stingray.rmq.cloudamqp.com/laxmuumj";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	private AdapterEspacios adapterEspacios;

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
		channel.queueDeclare(QUEUE_ENVIAR, false, false, false, null); // Cola donde se actuarÃ¡ de emisor
		channel.queueDeclare(QUEUE_RECIBIR, false, false, false, null); // Cola donde se actuará de receptor

		// El objeto consumer guardará los mensajes que lleguen
		// a la cola QUEUE_RECIBIR hasta que los usemos
		consumer = new QueueingConsumer(channel);
	}

	@After
	public void afterEveryTest() throws IOException {
		adapterEspacios.cerrarConexionAMQP();
	}

	@Test
	public void contexLoads() throws Exception {
		assertThat(espacioController).isNotNull();
	}

	@Test
	public void test_GET_CONEXION() throws Exception {
		ResponseEntity<String> result = espacioController.conexion1();
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	public void test_GET_ESPACIOS() throws Exception {
		EspacioDTO espacioDTO = new EspacioDTO();
		ObjectMapper mapper = new ObjectMapper();
		String msg = mapper.writeValueAsString(espacioDTO);
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

		ResponseEntity<EspacioDTO> result = espacioController.getSpace(1, 675745.92064, 4616800.60363);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	public void test_GET_ESPACIOS_ERROR() throws Exception {
		String msg = "No encontrado";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

		ResponseEntity<EspacioDTO> result = espacioController.getSpace(7, 675745.92064, 4616800.60363);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
	}
}