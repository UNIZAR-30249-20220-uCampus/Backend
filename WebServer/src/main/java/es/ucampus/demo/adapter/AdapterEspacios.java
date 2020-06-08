package es.ucampus.demo.adapter;

import dtoObjects.valueObject.CriteriosBusquedaDTO;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.Connection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class AdapterEspacios {

	private  String QUEUE_ENVIAR;
	private  String QUEUE_RECIBIR;
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://atyfvbtb:0k8H_vr2RIrXbzEq6dXwKNXF3Xu4J5F3@chinook.rmq.cloudamqp.com/atyfvbtb";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	public AdapterEspacios(String QUEUE_ENVIAR, String QUEUE_RECIBIR) throws IOException {
		this.QUEUE_ENVIAR = QUEUE_ENVIAR;
		this.QUEUE_RECIBIR = QUEUE_RECIBIR;
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
		boolean durable = true;
		channel.queueDeclare(QUEUE_ENVIAR, durable, false, false, null); // Cola donde se actuará de emisor
		channel.queueDeclare(QUEUE_RECIBIR, durable, false, false, null); // Cola donde se actuará de receptor

		// El objeto consumer guardará los mensajes que lleguen
		// a la cola QUEUE_RECIBIR hasta que los usemos
		consumer = new QueueingConsumer(channel);
	}

	/*
	 *	Cierra la conexion con Rabbitmq
	 */
	public void cerrarConexionAMQP() throws IOException {
		channel.close();
		connection.close();
	}

	/*
	 * Envia planta, y coordenadas de un espacio a traves de Rabbitmq
	 */
	public void enviarGetEspacio(int planta, double x, double y) throws IOException {
		String messageString = "espacios/" + planta + "/" + x + "/" + y;
		//publica mensaje en la cola, indicamos que el mensaje sea durable
		channel.basicPublish("", QUEUE_ENVIAR, MessageProperties.PERSISTENT_TEXT_PLAIN, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}

	/*
	 * Envia criterios de busqueda de un espacio a traves de Rabbitmq
	 */
	public void enviarbuscarEspacio(CriteriosBusquedaDTO criterios) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// Java object to JSON string
		String jsonString = mapper.writeValueAsString(criterios);
		String messageString = "buscar-espacio/" + jsonString;
		//publica mensaje en la cola, indicamos que el mensaje sea durable
		channel.basicPublish("", QUEUE_ENVIAR, MessageProperties.PERSISTENT_TEXT_PLAIN, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}

	/*
	 * Envia equipamientos de un espacio a traves de Rabbitmq
	 */
	public void establecerEquipamiento(CriteriosBusquedaDTO equipRequest) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// Java object to JSON string
		String jsonString = mapper.writeValueAsString(equipRequest);
		String messageString = "equipamiento/" + jsonString;
		//publica mensaje en la cola, indicamos que el mensaje sea durable
		channel.basicPublish("", QUEUE_ENVIAR, MessageProperties.PERSISTENT_TEXT_PLAIN, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}

	/*
	 * Envia id de un espacio para hacerlo reservable o alquilable a traves de Rabbitmq
	 */
	public void cambiarAlquilable(String espacio, int estado, String tipo) throws IOException {
		// Java object to JSON string
		String messageString = "cambiar-" + tipo + "/" + espacio + "/" + estado ;
		//publica mensaje en la cola, indicamos que el mensaje sea durable
		channel.basicPublish("", QUEUE_ENVIAR, MessageProperties.PERSISTENT_TEXT_PLAIN, messageString.getBytes());
		System.out.println(" [x] Enviado '" + messageString + "'");
	}

	/*
	 * Recibe la respuesta del servidor de aplicaciones
	 */
	public String recibirEspacio() throws Exception {
		boolean autoAck = false;
		channel.basicConsume(QUEUE_RECIBIR, autoAck, consumer);
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());
		System.out.println(" [x] Recibido '" + message + "'");
		//Hacemos el ACK explicito cuando hemos procesado el mensaje
		//false indica que el ACK no es multiple: solo cuenta para un mensaje concreto
		channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		return message;
	}
}