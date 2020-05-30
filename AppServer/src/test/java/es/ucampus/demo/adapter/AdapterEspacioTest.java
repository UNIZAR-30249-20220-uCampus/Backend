package es.ucampus.demo.adapter;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import es.ucampus.demo.DemoApplication;


import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdapterEspacioTest {

	@Autowired
	private AdapterEspacios adapterEspacios;

	private final static String QUEUE_ENVIAR = "WebASpringEspacios";
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
		assertThat(adapterEspacios).isNotNull();
	}

	@Test
	@Ignore
	public void test_GET_ESPACIO_ID() throws Exception {

		String msg = "espacios/\"CRE.1200.01.050\"";
		channel.basicPublish("", QUEUE_ENVIAR, null, msg.getBytes());
	}
	
}