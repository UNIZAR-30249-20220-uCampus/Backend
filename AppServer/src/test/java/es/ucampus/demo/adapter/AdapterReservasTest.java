package es.ucampus.demo.adapter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
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
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdapterReservasTest {
	
	private AdapterReservas adapterReservas;

	private String QUEUE_ENVIAR = "AppServerTestReservasEnviar";
	private String QUEUE_RECIBIR = "AppServerTestReservasRecibir";
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://laxmuumj:ivRgGMHAsnl088kdlEWhskufGJSGsbkf@stingray.rmq.cloudamqp.com/laxmuumj";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;
	private QueueingConsumer.Delivery delivery;

	@Before
	public void before() throws IOException {

		adapterReservas = new AdapterReservas(QUEUE_ENVIAR, QUEUE_RECIBIR);

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

		consumer = new QueueingConsumer(channel);

	}

	@Test
	public void contexLoads() throws Exception {
		assertThat(adapterReservas).isNotNull();
	}

	@Test
	public void test_ENVIAR_RESERVAS_ESPACIO() throws Exception {

		String msg = "listaReservas";
		adapterReservas.emisorAMQP(msg);

		channel.basicConsume(QUEUE_ENVIAR, true, consumer);
		delivery = consumer.nextDelivery();
		String actual = new String(delivery.getBody());
		
		assertEquals(msg, actual);
	}

	@Test
	@Ignore
	public void test_GET_RESERVAS_ESPACIO() throws Exception {

		String msg = "reservas/\"CRE.1200.01.050\"";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());

	}

	@Test
	@Ignore
	public void test_GET_RESERVAS_ESPACIO_ESTADO() throws Exception {

		String msg = "reservas-estado/\"CRE.1200.01.050\"/PENDIENTE";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());
	}

	@Test
	@Ignore
	public void test_GET_RESERVAS_USUARIO() throws Exception {

		String msg = "reservas-usuario/Alex";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());
	}

	@Test
	@Ignore
	public void test_GET_RESERVAS_USUARIO_ESTADO() throws Exception {

		String msg = "reservas-usuario-estado/Alex/PENDIENTE";
		channel.basicPublish("", QUEUE_RECIBIR, null, msg.getBytes());
	}

	@After
    public void cerrarConexion() throws IOException {
        adapterReservas.cerrarConexionAMQP();
    }

}