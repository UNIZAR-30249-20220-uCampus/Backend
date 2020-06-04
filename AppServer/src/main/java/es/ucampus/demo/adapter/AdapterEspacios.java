package es.ucampus.demo.adapter;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;

import es.ucampus.demo.service.ServiciosEspacio;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.Connection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterEspacios {

	@Autowired
	private ServiciosEspacio serviciosEspacio;

	// Declaración de las colas utilizadas para la comunicación mediante Broker
	private String QUEUE_ENVIAR;
	private String QUEUE_RECIBIR;
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	// Credenciales para la conexión a CloudAMQP
	private final static String CredencialCloudAMQP = "amqp://laxmuumj:ivRgGMHAsnl088kdlEWhskufGJSGsbkf@stingray.rmq.cloudamqp.com/laxmuumj";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	/**
	 * Método constructor para gestionar la comunicación con el broker dedicada a
	 * los espacios.
	 * 
	 * @param QUEUE_ENVIAR
	 * @param QUEUE_RECIBIR
	 * @throws IOException
	 */
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
	 * Se cierra el canal y la conexión con el broker.
	 */
	public void cerrarConexionAMQP() throws IOException {
		channel.close();
		connection.close();
	}

	/*
	 * Dado un JSONObject se publica su contenido en formato String en la cola del
	 * broker "QUEUE_ENVIAR", destinada para enviar desde el AppServer al WebServer
	 */
	public void emisorAMQP(JSONObject obj) throws IOException {
		//publica mensaje en la cola
		channel.basicPublish("", QUEUE_ENVIAR, MessageProperties.PERSISTENT_TEXT_PLAIN, obj.toJSONString().getBytes());
		System.out.println(" [x] Enviado '" + obj.toJSONString() + "'");
	}

	/*
	 * Dado un String se publica en la cola del broker "QUEUE_ENVIAR", destinada
	 * para enviar desde el AppServer al WebServer
	 */
	public void emisorAMQP(String obj) throws IOException {
		//publica mensaje en la cola
		channel.basicPublish("", QUEUE_ENVIAR, MessageProperties.PERSISTENT_TEXT_PLAIN, obj.getBytes());
		System.out.println(" [x] Enviado '" + obj + "'");
	}

	/*
	 * Cuando haya disponible un mensaje en QUEUE_RECIBIR, cola destinada a enviar
	 * Espacios desde el WebServer al AppServer, se consumirá y se tratará su
	 * contenido. Las opciones posibles son "espacios", donde se buscará un espacio
	 * por coordenadas, "buscar-espacios", donde se buscarán los espacios que
	 * cumplan los criterios, ya sea de equipamiento o de identificador y
	 * "equipamiento", donde se modificará la información referente a un espacio.
	 */
	public void receptorAMQP() throws Exception {
		boolean autoAck = false;
		channel.basicConsume(QUEUE_RECIBIR, autoAck, consumer);
		while (true) {
			try {
				// bloquea hasta que llege un mensaje
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println(" [x] Recibido '" + message + "'");
				//Hacemos el ACK explicito cuando hemos procesado el mensaje
				//false indica que el ACK no es multiple: solo cuenta para un mensaje concreto
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

				String[] path = message.split("/");
				ObjectMapper mapper = new ObjectMapper();
				// Se analiza el mensaje recibido y se implementan los posibles casos.
				switch (path[0]) {
					// Buscar espacio por coordenadas (planta, x, y)
					case "espacios":
						int planta = Integer.parseInt(path[1]);
						double x = Double.parseDouble(path[2]);
						double y = Double.parseDouble(path[3]);
						EspacioDTO espacio = serviciosEspacio.getEspacioCoordenadas(planta, x, y);
						if (espacio != null) {
							// Se transforma el espacio a JSON y se envía al broker.
							emisorAMQP(espacio.toJson());
						} else {
							// La búsqueda no ha dado resultados y se notifica.
							emisorAMQP("No encontrado");
						}
						break;
					// Dentro de esta opción se divide en posibilidad de búsqueda por identificador
					// de espacio o por
					// criterios de búsqueda
					case "buscar-espacio":
						CriteriosBusquedaDTO criterios = mapper.readValue(path[1], CriteriosBusquedaDTO.class);
						// Si la busqueda es dado el id de un espacio
						if (criterios.busquedaPorId()) {

							EspacioDTO espacio1 = serviciosEspacio.getEspacioDTOId(criterios.getNombre());
							String jsonEspacio = mapper.writeValueAsString(espacio1);
							// enviar espacio
							emisorAMQP(jsonEspacio);
						} else {
							List<EspacioDTO> espacios = new ArrayList<EspacioDTO>();
							// Búsqueda según criterios elegidos y horarios.
							if (criterios.busquedaPorHorario()) {
								espacios = serviciosEspacio.buscarEspaciosPorCriteriosYHorario(criterios);
							}
							// Búsqueda según los criterios elegidos sin horario
							else {
								espacios = serviciosEspacio.buscarEspacioPorCriterios(criterios);
							}
							// Búsqueda de los espacios que cumplen los criterios y además son alguilables.
							if (criterios.busquedaAlquilables()) {
								espacios = serviciosEspacio.getEspaciosAlquilables(espacios);
							}
							// Se publica en el broker el resultado de la búsqueda.
							String espacios2 = new Gson().toJson(espacios);
							emisorAMQP(espacios2);
						}
						break;
					// Equipamiento es donde se modificará el equipamiento de un espacio. Los
					// criterios a cambiar
					// se envían de la misma forma que se envían los criterios de búsqueda.
					case "equipamiento":
						CriteriosBusquedaDTO cambiosEquip = mapper.readValue(path[1], CriteriosBusquedaDTO.class);
						boolean resultado = serviciosEspacio.setEquipamiento(cambiosEquip);
						if (resultado) {
							// Notifica que el resultado ha sido satisfactorio
							emisorAMQP("OK");
						} else {
							// Ocurrió algún error
							emisorAMQP("FALLO");
						}
						break;
					// El mensaje recibido no corresponde con ninguna operación del servidor.
					default:
						emisorAMQP("Error");
				}
			} catch (IllegalArgumentException exception) {
				emisorAMQP("Argumentos no validos");
			}
		}
	}
}