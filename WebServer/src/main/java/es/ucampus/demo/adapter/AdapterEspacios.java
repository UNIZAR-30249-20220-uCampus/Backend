package es.ucampus.demo.adapter;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
//import org.postgresql.core.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
public class AdapterEspacios {

	@Autowired
	private final static String QUEUE_ENVIAR = "WebASpringEspacios";
	private final static String QUEUE_RECIBIR = "SpringAWebEspacios";
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://laxmuumj:ivRgGMHAsnl088kdlEWhskufGJSGsbkf@stingray.rmq.cloudamqp.com/laxmuumj";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	public AdapterEspacios() throws IOException {
		// Conexión al broker RabbitMQ broker (prueba en la URL de
		// la variable de entorno que se llame como diga ENV_AMQPURL_NAME
		// o sino en localhost)
		ConnectionFactory factory = new ConnectionFactory();
		String amqpURL = System.getenv().get(ENV_AMQPURL_NAME) != null ? System.getenv().get(ENV_AMQPURL_NAME)
				: CredencialCloudAMQP;
		try {
			factory.setUri(amqpURL);
		} catch (Exception e) {
			System.out.println(" [*] AQMP broker not found in " + amqpURL);
			System.exit(-1);
		}
		System.out.println(" [*] AQMP broker found in " + amqpURL);
		connection = factory.newConnection();
		// Con un solo canal
		channel = connection.createChannel();

		// Declaramos dos colas en el broker a través del canal
		// recién creado llamada QUEUE_ENVIAR y QUEUE_RECIBIR
		// idempotente: solo se creará si no existe ya)
		// Se crea tanto en el servidorWeb como en spring, porque no
		// sabemos cuál se lanzará antes.
		channel.queueDeclare(QUEUE_ENVIAR, false, false, false, null); // Cola donde se actuará de emisor
		channel.queueDeclare(QUEUE_RECIBIR, false, false, false, null); // Cola donde se actuará de receptor

		// El objeto consumer guardará los mensajes que lleguen
		// a la cola QUEUE_RECIBIR hasta que los usemos
		consumer = new QueueingConsumer(channel);
	}

	public void cerrarConexionAMQP() throws IOException {
		channel.close();
		connection.close();
	}

	////////////
	public void enviarGetEspacio(String id) throws IOException {
		String messageString = "espacios/" + id;
		channel.basicPublish("", QUEUE_ENVIAR, null, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}

	public void enviarGetEspacio(int planta, double x, double y) throws IOException {
		String messageString = "espacios/" + planta + "/" + x + "/" + y;
		channel.basicPublish("", QUEUE_ENVIAR, null, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}

	public void enviarbuscarEspacio(CriteriosBusquedaDTO criterios) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// Java object to JSON string
		String jsonString = mapper.writeValueAsString(criterios);
		String messageString = "buscar-espacio/" + jsonString;
		channel.basicPublish("", QUEUE_ENVIAR, null, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}



	////////////////// RECIBIR ///////////////////////////
	public EspacioDTO recibirGetEspacio() throws Exception {
		channel.basicConsume(QUEUE_RECIBIR, true, consumer);
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());

		ObjectMapper mapper = new ObjectMapper();
		EspacioDTO espacio = mapper.readValue(message, EspacioDTO.class);

		System.out.println(" [x] Recibido '" + message + "'");
		return espacio;
	}

	public String recibirGetEspacioS() throws Exception {
		channel.basicConsume(QUEUE_RECIBIR, true, consumer);
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());

		System.out.println(" [x] Recibido '" + message + "'");
		return message;
	}

	public JSONArray recibirBuscarEspacio() throws Exception {
		channel.basicConsume(QUEUE_RECIBIR, true, consumer);
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());

		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(message);

		System.out.println(" [x] Recibido '" + message + "'");
		return json;
	}
/*
	public void establecerEquipamiento(String id, TipoEquipamiento tipo, int cantidad) throws IOException {
		String messageString = "equipamiento/" + id + "/" + tipo + "/" + cantidad;
		channel.basicPublish("", QUEUE_ENVIAR, null, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}
*/
	public void establecerEquipamiento(CriteriosBusquedaDTO equipRequest) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// Java object to JSON string
		String jsonString = mapper.writeValueAsString(equipRequest);
		String messageString = "equipamiento/" + jsonString;
		channel.basicPublish("", QUEUE_ENVIAR, null, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}
	public String recibirEstablecerEquipamiento() throws Exception {
		channel.basicConsume(QUEUE_RECIBIR, true, consumer);
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());
		return message;
	}

	public void enviarGetEspaciosAlquilables(int planta) throws IOException {
		String messageString = "espacios-alquilables/" + planta;
		channel.basicPublish("", QUEUE_ENVIAR, null, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}

	public JSONArray recibirGetEspaciosAlquilables() throws Exception {
		channel.basicConsume(QUEUE_RECIBIR, true, consumer);
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());
		
		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(message);

		System.out.println(" [x] Recibido '" + message + "'");
		return json;
	}

	public void enviarCalcularTarifaEspacioAlquilable(String id) throws IOException {
		String messageString = "tarifa-espacio/" + id;
		channel.basicPublish("", QUEUE_ENVIAR, null, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}

	public String recibirCalcularTarifaEspacioAlquilable() throws Exception {
		channel.basicConsume(QUEUE_RECIBIR, true, consumer);
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());

		System.out.println(" [x] Recibido '" + message + "'");
		return message;
	}

}