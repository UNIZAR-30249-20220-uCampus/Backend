package es.ucampus.demo.controller;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import domainObjects.request.ReservaRequest;
import es.ucampus.demo.adapter.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


@RestController
@Api(value = "Reservas", description = "Operaciones para la gestión de reservas")
public class ReservasController {

	
	private AdapterReservas adapterReservas;

	public ReservasController() throws IOException{
		adapterReservas = new AdapterReservas("WebASpringReservas","SpringAWebReservas");
	} 

	public ReservasController(AdapterReservas adapterReservas) throws IOException{
		this.adapterReservas = adapterReservas;
	}

	/*
	 * Crea una reserva dado el identificador de un espacio y los datos de reserva
	 */
	@PostMapping(value = "/api/crear-reserva/{espacio}")
	@ApiOperation(value = "Crea una reserva para un espacio", notes = "Devuelve estado de la reserva" )
	@ApiResponses(value = {
		@ApiResponse(code = 400, message = "Hay colisión con otra reserva"),
	})
	public ResponseEntity<String> crearReserva(
		@ApiParam(value = "Id del espacio que se quiere reservar", required = true) @PathVariable String espacio, 
		@ApiParam(value = "Parametros de reserva", required = true) @RequestBody ReservaRequest reserva) 
			throws Exception {

		//envia los datos
		adapterReservas.enviarReserva(espacio,reserva);
		
		//recibe la respuesta del servidor
		String res = adapterReservas.recibirReserva();
		HttpStatus codigo = HttpStatus.CREATED;
		//Si hay colisión con otras reservas
		if(res.equals("Colision")){
			codigo = HttpStatus.BAD_REQUEST;
		}
		//espacio no encontrado segun ese id
		else if(res.equals("No encontrado")){
			codigo = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(codigo).body(res);
	}

	/*
	 * Acepta una reserva dado su identificador
	 */
	@PutMapping(value = "/api/aceptar-reserva/{reserva}")
	@ApiOperation(value = "Acepta una reserva para un espacio", notes = "Devuelve estado de la reserva" )
	public ResponseEntity<String> aceptarReserva(
		@ApiParam(value = "Id de la reserva que se quiere aceptar", required = true) @PathVariable String reserva) 
			throws Exception {

		//envia los datos
		adapterReservas.enviarAccionReserva(reserva,"aceptar");

		//recibe la respuesta del servidor
		String res = adapterReservas.recibirReserva();
		HttpStatus codigo = HttpStatus.OK;

		//reserva no encontrada segun ese id
		if(res.equals("Reserva no encontrada")){
			codigo = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(codigo).body(res);
	}

	/*
	 * Paga una reserva dado su identificador
	 */
	@PutMapping(value = "/api/pagar-reserva/{reserva}")
	@ApiOperation(value = "Paga una reserva para un espacio", notes = "Devuelve estado de la reserva" )
	public ResponseEntity<String> pagarReserva(
		@ApiParam(value = "Id de la reserva que se quiere pagar", required = true) @PathVariable String reserva) 
			throws Exception {

		//envia los datos
		adapterReservas.enviarAccionReserva(reserva,"pagar");

		//recibe la respuesta del servidor
		String res = adapterReservas.recibirReserva();
		HttpStatus codigo = HttpStatus.OK;

		//reserva no encontrada segun ese id
		if(res.equals("Reserva no encontrada")){
			codigo = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(codigo).body(res);
	}

	/*
	 * Cancela una reserva dado su identificador
	 */
	@PutMapping(value = "/api/cancelar-reserva/{reserva}")
	@ApiOperation(value = "Cancela una reserva para un espacio", notes = "Devuelve estado de la reserva" )
	public ResponseEntity<String> cancelarReserva(
		@ApiParam(value = "Id de la reserva que se quiere cancelar", required = true) @PathVariable String reserva) 
			throws Exception {

		//envia los datos
		adapterReservas.enviarAccionReserva(reserva,"cancelar");

		//recibe la respuesta del servidor
		String res = adapterReservas.recibirReserva();
		HttpStatus codigo = HttpStatus.OK;

		//reserva no encontrada segun ese id
		if(res.equals("Reserva no encontrada")){
			codigo = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(codigo).body(res);
	}

	/*
	 * Dado el identificador de un espacio devuelve sus reservas
	 */
	@GetMapping(value = "/api/reservas/{espacio}")
	@ApiOperation(value = "Busqueda de reservas correspondientes a un espacio", notes = "Devuelve lista de reservas correspondientes a un espacio")
	public ResponseEntity<JSONArray> getReservas(
		@ApiParam(value = "Id del espacio", required = true) @PathVariable String espacio) 
			throws Exception {

		//envia los datos
		adapterReservas.enviarGetReservas(espacio);

		//recibe la respuesta del servidor
		String res = adapterReservas.recibirReserva();
		HttpStatus codigo = HttpStatus.OK;

		//espacio no encontrado segun ese id
		if(res.equals("No encontrado")){
			codigo = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(codigo).body(null);
		}

		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(res);
		return ResponseEntity.status(codigo).body(json);
	}

	/*
	 * Dado el identificador de un espacio y el estado de una reserva devuelve sus reservas con ese estado
	 */
	@GetMapping(value = "/api/reservas/{espacio}/{estado}")
	@ApiOperation(value = "Busqueda de reservas correspondientes a un espacio", notes = "Devuelve lista de reservas correspondientes a un espacio")
	public ResponseEntity<JSONArray> getReservas(
		@ApiParam(value = "Id del espacio", required = true) @PathVariable String espacio, 
		@ApiParam(value = "Estado de la reserva (PENDIENTE, PENDIENTEPAGO, ACEPTADA, CANCELADA)", required = true) @PathVariable String estado) 
			throws Exception {

		//envia los datos
		adapterReservas.enviarGetReservas(espacio, estado);

		//recibe la respuesta del servidor
		String res = adapterReservas.recibirReserva();
		HttpStatus codigo = HttpStatus.OK;

		//espacio no encontrado segun ese id
		if(res.equals("No encontrado")){
			codigo = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(codigo).body(null);
		}

		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(res);
		return ResponseEntity.status(codigo).body(json);
	}

	/*
	 * Dado el identificador de un usuario devuelve sus reservas
	 */
	@GetMapping(value = "/api/reservas-usuario/{usuario}")
	@ApiOperation(value = "Busqueda de reservas correspondientes a un usuario", notes = "Devuelve lista de reservas correspondientes a un usuario")
	public ResponseEntity<JSONArray> getReservasUsuario(
		@ApiParam(value = "Id del usuario", required = true) @PathVariable String usuario) 
			throws Exception {

		//envia los datos
		adapterReservas.enviarGetReservasUsuario(usuario);

		//recibe la respuesta del servidor
		String res = adapterReservas.recibirReserva();
		HttpStatus codigo = HttpStatus.OK;
		//crea el json
		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(res);

		return ResponseEntity.status(codigo).body(json);
	}

	/*
	 * Dado el identificador de un usuario y el estado de una reserva devuelve sus reservas con ese estado
	 */
	@GetMapping(value = "/api/reservas-usuario/{usuario}/{estado}")
	@ApiOperation(value = "Busqueda de reservas correspondientes a un usuario", notes = "Devuelve lista de reservas correspondientes a un usuario")
	public ResponseEntity<JSONArray> getReservasUsuarioEstado(
		@ApiParam(value = "Id del usuario", required = true) @PathVariable String usuario,
		@ApiParam(value = "Estado de la reserva (PENDIENTE, PENDIENTEPAGO, ACEPTADA, CANCELADA)", required = true) @PathVariable String estado) 
			throws Exception {

		//envia los datos
		adapterReservas.enviarGetReservasUsuario(usuario, estado);

		//recibe la respuesta del servidor
		String res = adapterReservas.recibirReserva();
		HttpStatus codigo = HttpStatus.OK;
		//crea el json
		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(res);

		return ResponseEntity.status(codigo).body(json);
	}
	
}
