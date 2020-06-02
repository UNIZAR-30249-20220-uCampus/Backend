package es.ucampus.demo.adapter;

import org.json.simple.JSONObject;
//import org.postgresql.core.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;

import es.ucampus.demo.service.FuncionesEspacio;

import com.rabbitmq.client.ConnectionFactory;
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
	private FuncionesEspacio funcionesEspacios;
	
	private String QUEUE_ENVIAR;
	private String QUEUE_RECIBIR;
	private final static String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";
	private final static String CredencialCloudAMQP = "amqp://laxmuumj:ivRgGMHAsnl088kdlEWhskufGJSGsbkf@stingray.rmq.cloudamqp.com/laxmuumj";
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
		channel.queueDeclare(QUEUE_ENVIAR, false, false, false, null); // Cola donde se actuará de emisor
		channel.queueDeclare(QUEUE_RECIBIR, false, false, false, null); // Cola donde se actuará de receptor

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
	 * Envia mensaje a traves de Rabbitmq
	 */
	public void emisorAMQP(JSONObject obj) throws IOException {
		//publica mensaje en la cola
		channel.basicPublish("", QUEUE_ENVIAR, null, obj.toJSONString().getBytes());
		System.out.println(" [x] Enviado '" + obj.toJSONString() + "'");
	}

	/*
	 * Envia mensaje a traves de Rabbitmq
	 */
	public void emisorAMQP(String obj) throws IOException {
		//publica mensaje en la cola
		channel.basicPublish("", QUEUE_ENVIAR, null, obj.getBytes());
		System.out.println(" [x] Enviado '" + obj + "'");
	}

	/*
	 * Recibe mensajes del servidor web
	 */
	public void receptorAMQP() throws Exception {
		channel.basicConsume(QUEUE_RECIBIR, true, consumer);
		while (true) {
			// bloquea hasta que llege un mensaje
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Recibido '" + message + "'");

			String[] path = message.split("/");
			ObjectMapper mapper = new ObjectMapper();

			switch (path[0]) {
				//Obtener espacio dado planta y coordenadas
				case "espacios":
					int planta = Integer.parseInt(path[1]);
					double x = Double.parseDouble(path[2]);
					double y = Double.parseDouble(path[3]);
					EspacioDTO espacio = funcionesEspacios.getEspacioCoordenadas(planta,x,y);
					if(espacio != null){
						emisorAMQP(espacio.toJson());
					}
					else{
						emisorAMQP("No encontrado");
					}
				break;
				//Buscar espacios segun criterios
                case "buscar-espacio":
                    CriteriosBusquedaDTO criterios = mapper.readValue(path[1], CriteriosBusquedaDTO.class);	
					//Si la busqueda es dado el id de un espacio
                    if(criterios.busquedaPorId()){

                        EspacioDTO espacio1 = funcionesEspacios.getEspacioDTOId(criterios.getNombre());
                        String jsonEspacio = mapper.writeValueAsString(espacio1);
						System.out.println(jsonEspacio);
						//enviar espacio
                        emisorAMQP(jsonEspacio);
                    }
                    else{
						List<EspacioDTO> espacios = new ArrayList<EspacioDTO>();
						//Si se buscan espacios dado un horario
						if(criterios.busquedaPorHorario()){
							espacios = funcionesEspacios.buscarEspaciosPorCriteriosYHorario(criterios);
						}
						else{
							espacios = funcionesEspacios.buscarEspacioPorCriterios(criterios);
						}
						//Si se buscan los espacios alquilables
						if(criterios.busquedaAlquilables()){
							espacios = funcionesEspacios.getEspaciosAlquilables(espacios);
						}
						
                        String espacios2 = new Gson().toJson(espacios);
						System.out.println(espacios2);
						//enviar espacios
                        emisorAMQP(espacios2);
                    }
				break;
				//Gestionar equipamientos
                case "equipamiento":
                    CriteriosBusquedaDTO cambiosEquip = mapper.readValue(path[1], CriteriosBusquedaDTO.class);
					System.out.println("CAMBIO EQUIPAMIENTO: ");
					System.out.println(cambiosEquip);
                    boolean resultado = funcionesEspacios.setEquipamiento(cambiosEquip);
                    if(resultado) {
                        emisorAMQP("OK");
                    }
                    else {
                        emisorAMQP("FALLO");
                    }
				break;
				default:
					emisorAMQP("Error");
            }
		}
	}
}