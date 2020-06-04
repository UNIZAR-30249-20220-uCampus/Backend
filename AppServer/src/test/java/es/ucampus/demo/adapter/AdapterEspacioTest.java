package es.ucampus.demo.adapter;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import es.ucampus.demo.DemoApplication;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdapterEspacioTest {

	private AdapterEspacios adapterEspacios;

	private String QUEUE_ENVIAR = "AppServerTestEspaciosEnviar";
	private String QUEUE_RECIBIR = "AppServerTestEspaciosRecibir";
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://laxmuumj:ivRgGMHAsnl088kdlEWhskufGJSGsbkf@stingray.rmq.cloudamqp.com/laxmuumj";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private QueueingConsumer.Delivery delivery;

	@Before
	public void before() throws IOException {
		adapterEspacios = new AdapterEspacios(QUEUE_ENVIAR, QUEUE_RECIBIR);

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

		// El objeto consumer guardará los mensajes que lleguen
		// a la cola QUEUE_RECIBIR hasta que los usemos
		consumer = new QueueingConsumer(channel);
	}

	@Test
	public void contexLoads() throws Exception {
		assertThat(adapterEspacios).isNotNull();
	}

	@Test
	public void test_ENVIAR_RESERVAS_ESPACIO() throws Exception {

		String msg = "listaEspacios";
		adapterEspacios.emisorAMQP(msg);

		JSONObject json = new JSONObject();
		adapterEspacios.emisorAMQP(json);

		boolean autoAck = false;
		channel.basicConsume(QUEUE_ENVIAR, autoAck, consumer);
		delivery = consumer.nextDelivery();
		String actual = new String(delivery.getBody());
		channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		assertEquals(msg, actual);

		delivery = consumer.nextDelivery();
		String actualJson = new String(delivery.getBody());
		channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

		assertEquals(json.toJSONString(), actualJson);
		
	}

	@Test
	@Ignore
	public void test_GET_ESPACIO_ID() throws Exception {

		String msg = "espacios/\"CRE.1200.01.050\"";
		channel.basicPublish("", QUEUE_ENVIAR, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
	}

	@After
    public void cerrarConexion() throws IOException {
        adapterEspacios.cerrarConexionAMQP();
    }
	

}