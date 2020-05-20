package es.ucampus.demo.adapter;

import org.json.simple.JSONObject;
//import org.postgresql.core.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;
import domainObjects.request.ReservaRequest;
import domainObjects.valueObject.EstadoReserva;
import dtoObjects.entity.ReservaDTO;

import es.ucampus.demo.service.FuncionesEspacio;
import es.ucampus.demo.service.FuncionesReserva;

import com.rabbitmq.client.ConnectionFactory;
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
	private FuncionesEspacio funcionesEspacios;
	@Autowired
	private FuncionesReserva funcionesReserva;

	private final static String QUEUE_ENVIAR = "SpringAWebReservas";
	private final static String QUEUE_RECIBIR = "WebASpringReservas";
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://laxmuumj:ivRgGMHAsnl088kdlEWhskufGJSGsbkf@stingray.rmq.cloudamqp.com/laxmuumj";
	private Connection connection;
	private Channel channel;
	private QueueingConsumer consumer;

	public AdapterReservas() throws IOException {
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

	public void emisorAMQP(JSONObject obj) throws IOException {
		channel.basicPublish("", QUEUE_ENVIAR, null, obj.toJSONString().getBytes());
		System.out.println(" [x] Enviado '" + obj.toJSONString() + "'");
	}

	public void emisorAMQP(String obj) throws IOException {
		channel.basicPublish("", QUEUE_ENVIAR, null, obj.getBytes());
		System.out.println(" [x] Enviado '" + obj + "'");
	}

	public void receptorAMQP() throws Exception {
		channel.basicConsume(QUEUE_RECIBIR, true, consumer);
		while (true) {
			// bloquea hasta que llege un mensaje
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Recibido '" + message + "'");

			String[] path = message.split("/");
			ObjectMapper mapper = new ObjectMapper();

			Long idReserva;
			String reservasString;

			switch (path[0]) {
				case "crear-reserva":
					Espacio espacioReserva = funcionesEspacios.getEspacioId(path[1]);
					if (espacioReserva != null) {
						ReservaRequest reserva = mapper.readValue(path[2], ReservaRequest.class);
						System.out.println(reserva.toString());
						Reserva r = new Reserva(espacioReserva, reserva.getHorario(), reserva.getUsuario(), reserva.getTipo());
						boolean ok = funcionesReserva.hacerReserva(r);
						if (ok) {
							emisorAMQP("Reservada");
						} else {
							emisorAMQP("Colision");
						}
					} else {
						emisorAMQP("No encontrado");
					}
					break;
				case "aceptar-reserva":
					idReserva = Long.parseLong(path[1]);
					boolean aceptar = funcionesReserva.aceptarReserva(idReserva);
					if (aceptar) {
						emisorAMQP("Aceptada");
					} else {
						emisorAMQP("Reserva no encontrada");
					}
					break;
				case "pagar-reserva":
					idReserva = Long.parseLong(path[1]);
					boolean pagar = funcionesReserva.pagarReserva(idReserva);
					if (pagar) {
						emisorAMQP("Pagada");
					} else {
						emisorAMQP("Reserva no encontrada");
					}
					break;
				case "cancelar-reserva":
					idReserva = Long.parseLong(path[1]);
					boolean canceled = funcionesReserva.cancelarReserva(idReserva);
					if (canceled) {
						emisorAMQP("Cancelada");
					} else {
						emisorAMQP("Reserva no encontrada");
					}
					break;
				case "reservas":
					Espacio espacioReservas = funcionesEspacios.getEspacioId(path[1]);
					List<ReservaDTO> reservas = new ArrayList<ReservaDTO>();
					reservas = funcionesReserva.buscarReserva(espacioReservas);
					reservasString = new Gson().toJson(reservas);
					emisorAMQP(reservasString);
					break;
				case "reservas-estado":
					espacioReservas = funcionesEspacios.getEspacioId(path[1]);
					String estado = path[2];
					EstadoReserva estadoreserva = EstadoReserva.valueOf(estado);
					List<ReservaDTO> reservasEstado = new ArrayList<ReservaDTO>();
					reservasEstado = funcionesReserva.buscarReservaEstado(espacioReservas, estadoreserva);
					reservasString = new Gson().toJson(reservasEstado);
					emisorAMQP(reservasString);
					break;
				case "reservas-usuario":
					String usuario = path[1];
					List<ReservaDTO> reservasUsuario = new ArrayList<ReservaDTO>();
					reservasUsuario = funcionesReserva.buscarReservaUsuario(usuario);
					String reservasUsuarioString = new Gson().toJson(reservasUsuario);
					System.out.println(reservasUsuarioString);
					emisorAMQP(reservasUsuarioString);
					break;
			}
		}
	}
}