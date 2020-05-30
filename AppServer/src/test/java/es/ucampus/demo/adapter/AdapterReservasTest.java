package es.ucampus.demo.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import domainObjects.entity.Espacio;
import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;
import es.ucampus.demo.DemoApplication;

import org.json.simple.JSONArray;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdapterReservasTest {
	
	@Autowired
	private AdapterReservas adapterReservas;

	private final static String QUEUE_ENVIAR = "WebASpringReservas";
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://laxmuumj:ivRgGMHAsnl088kdlEWhskufGJSGsbkf@stingray.rmq.cloudamqp.com/laxmuumj";
	private Connection connection;
	private Channel channel;

	@Before
	public void before() throws IOException {
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
	}

	@Test
	public void contexLoads() throws Exception {
		assertThat(adapterReservas).isNotNull();
	}

	@Test
	@Ignore
	public void test_GET_RESERVAS_ESPACIO() throws Exception {

		String msg = "reservas/\"CRE.1200.01.050\"";
		channel.basicPublish("", QUEUE_ENVIAR, null, msg.getBytes());
	}

	@Test
	@Ignore
	public void test_GET_RESERVAS_ESPACIO_ESTADO() throws Exception {

		String msg = "reservas-estado/\"CRE.1200.01.050\"/PENDIENTE";
		channel.basicPublish("", QUEUE_ENVIAR, null, msg.getBytes());
	}

	@Test
	@Ignore
	public void test_GET_RESERVAS_USUARIO() throws Exception {

		String msg = "reservas-usuario/Alex";
		channel.basicPublish("", QUEUE_ENVIAR, null, msg.getBytes());
	}

	@Test
	@Ignore
	public void test_GET_RESERVAS_USUARIO_ESTADO() throws Exception {

		String msg = "reservas-usuario-estado/Alex/PENDIENTE";
		channel.basicPublish("", QUEUE_ENVIAR, null, msg.getBytes());
	}
}