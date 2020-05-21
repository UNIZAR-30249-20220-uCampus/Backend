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
	
	private final static String QUEUE_ENVIAR = "SpringAWebEspacios";
	private final static String QUEUE_RECIBIR = "WebASpringEspacios";
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

			switch (path[0]) {
				case "espacios":
					EspacioDTO espacio = funcionesEspacios.getEspacioCoordenadas(Integer.parseInt(path[1]),Double.parseDouble(path[2]),Double.parseDouble(path[3]));
					if(espacio != null){
						emisorAMQP(espacio.toJson());
					}
					else{
						emisorAMQP("No encontrado");
					}
                break;
                case "buscar-espacio":
                
                    CriteriosBusquedaDTO criterios = mapper.readValue(path[1], CriteriosBusquedaDTO.class);
                    System.out.println(criterios);
                    
                    if(criterios.busquedaPorId()){

                        EspacioDTO espacio1 = funcionesEspacios.getEspacioDTOId(criterios.getNombre());
                        String jsonEspacio = mapper.writeValueAsString(espacio1);
                        System.out.println(jsonEspacio);
                        emisorAMQP(jsonEspacio);
                    }
                    else{
                        List<EspacioDTO> espacios = new ArrayList<EspacioDTO>();
                        espacios = funcionesEspacios.buscarEspacioPorCriterios(criterios);
                        String espacios2 = new Gson().toJson(espacios);
                        System.out.println(espacios2);
                        emisorAMQP(espacios2);
                    }
                break;
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
            }
		}
	}
}