package es.ucampus.demo.adapter;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;
import domainObjects.request.ReservaRequest;
import domainObjects.valueObject.EstadoReserva;
import dtoObjects.entity.ReservaDTO;
import es.ucampus.demo.service.ServiciosEspacio;
import es.ucampus.demo.service.ServiciosReserva;
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

public class AdapterReservas {

	@Autowired
	private ServiciosEspacio serviciosEspacio;
	@Autowired
	private ServiciosReserva serviciosReserva;

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
	 * las reservas.
	 * 
	 * @param QUEUE_ENVIAR
	 * @param QUEUE_RECIBIR
	 * @throws IOException
	 */
	public AdapterReservas(String QUEUE_ENVIAR, String QUEUE_RECIBIR) throws IOException {
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
		channel.basicPublish("", QUEUE_ENVIAR, MessageProperties.PERSISTENT_TEXT_PLAIN, obj.toJSONString().getBytes());
		System.out.println(" [x] Enviado '" + obj.toJSONString() + "'");
	}

	/*
	 * Dado un String se publica en la cola del broker "QUEUE_ENVIAR", destinada
	 * para enviar desde el AppServer al WebServer
	 */
	public void emisorAMQP(String obj) throws IOException {
		channel.basicPublish("", QUEUE_ENVIAR, MessageProperties.PERSISTENT_TEXT_PLAIN, obj.getBytes());
		System.out.println(" [x] Enviado '" + obj + "'");
	}

	/*
	 * Cuando haya disponible un mensaje en QUEUE_RECIBIR, cola destinada a enviar
	 * Reservas desde el WebServer al AppServer, se consumirá y se tratará su
	 * contenido. Las opciones posibles son "crear-reservas", "aceptar-reserva",
	 * "cancelar-reserva", "pagar-reserva", "reservas", "reservas-estado",
	 * "reservas-usuario", "reservas-usuario-estado"
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

				Long idReserva;
				String reservasString;
				// Se analiza el mensaje recibido y se implementan los posibles casos.
				switch (path[0]) {
					// Se analiza la solicitud de reserva, si no existe colisión se crea. Si no,
					// notifica del error.
					// Existe la posibilidad de no encontrar el espacio que se solicita reservar.
					case "crear-reserva":
						Espacio espacioReserva = serviciosEspacio.getEspacioId(path[1]);
						// si id de espacio es valido
						if (espacioReserva != null) {
							ReservaRequest reserva = mapper.readValue(path[2], ReservaRequest.class);
							Reserva r = new Reserva(espacioReserva, reserva.getHorario(), reserva.getUsuario(),
									reserva.getTipo());
							boolean ok = serviciosReserva.hacerReserva(r);
							if (ok) {
								emisorAMQP("Reservada");
							} else {
								emisorAMQP("Colision");
							}
						} else {
							emisorAMQP("No encontrado");
						}
						break;
					// Dado el identificador de una reserva, se acepta la solicitud.
					// Existe la posibilidad de no encontrar la reserva.
					case "aceptar-reserva":
						idReserva = Long.parseLong(path[1]);
						boolean aceptar = serviciosReserva.aceptarReserva(idReserva);
						if (aceptar) {
							emisorAMQP("Aceptada");
						} else {
							emisorAMQP("Reserva no encontrada");
						}
						break;
					// Dado el identificador de una reserva, se paga cambiando asi el estado
					// de la reserva
					// Existe la posibilidad de no encontrar la reserva solicitada

					case "pagar-reserva":
						idReserva = Long.parseLong(path[1]);
						boolean pagar = serviciosReserva.pagarReserva(idReserva);
						if (pagar) {
							emisorAMQP("Pagada");
						} else {
							emisorAMQP("Reserva no encontrada");
						}
						break;
					// Dado el identificador de una reserva, se cancela cambiando asi el estado
					// de la reserva
					// Existe la posibilidad de no encontrar la reserva solicitada
					case "cancelar-reserva":
						idReserva = Long.parseLong(path[1]);
						boolean canceled = serviciosReserva.cancelarReserva(idReserva);
						if (canceled) {
							emisorAMQP("Cancelada");
						} else {
							emisorAMQP("Reserva no encontrada");
						}
						break;
					// Dado el identificador de un espacio, devuelve las reservas correspondientes
					// Existe la posibilidad de no encontrar el espacio solicitado
					case "reservas":
						Espacio espacioReservas = serviciosEspacio.getEspacioId(path[1]);
						if (espacioReservas != null) {
							List<ReservaDTO> reservas = new ArrayList<ReservaDTO>();
							reservas = serviciosReserva.buscarReserva(espacioReservas);
							reservasString = new Gson().toJson(reservas);
							emisorAMQP(reservasString);
						} else {
							emisorAMQP("No encontrado");
						}
						break;
					// Dados el identificador de un espacio y un estado, devuelve las reservas
					// correspondientes
					// con el estado solicitado.
					// Existe la posibilidad de no encontrar el espacio solicitado
					case "reservas-estado":
						espacioReservas = serviciosEspacio.getEspacioId(path[1]);
						if (espacioReservas != null) {
							String estado = path[2];
							EstadoReserva estadoreserva = EstadoReserva.valueOf(estado);
							List<ReservaDTO> reservasEstado = new ArrayList<ReservaDTO>();
							reservasEstado = serviciosReserva.buscarReservaEstado(espacioReservas, estadoreserva);
							reservasString = new Gson().toJson(reservasEstado);
							emisorAMQP(reservasString);
						} else {
							emisorAMQP("No encontrado");
						}
						break;
					// Dado el identificador de un usuario, devuelve las reservas correspondientes
					// a ese usuario.
					case "reservas-usuario":
						String usuario = path[1];
						List<ReservaDTO> reservasUsuario = new ArrayList<ReservaDTO>();
						reservasUsuario = serviciosReserva.buscarReservaUsuario(usuario);
						String reservasUsuarioString = new Gson().toJson(reservasUsuario);
						emisorAMQP(reservasUsuarioString);
						break;
					// Dado un identificador de usuario y un estado, devuelve las reservas
					// correspondientes
					// a ese usuario con ese estado concreto.
					case "reservas-usuario-estado":
						String usuarioEstado = path[1];
						String estado = path[2];
						EstadoReserva estadoreserva = EstadoReserva.valueOf(estado);
						List<ReservaDTO> reservasUsuarioEstado = new ArrayList<ReservaDTO>();
						reservasUsuarioEstado = serviciosReserva.buscarReservaUsuarioEstado(usuarioEstado,
								estadoreserva);
						String reservasUsuarioEstadoString = new Gson().toJson(reservasUsuarioEstado);
						emisorAMQP(reservasUsuarioEstadoString);
						break;
				}
			} catch (IllegalArgumentException exception) {
				emisorAMQP("Argumentos no validos");
			}
		}
	}
}