package es.ucampus.demo.controller;

import java.io.IOException;

import org.json.simple.JSONArray;
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
		adapterReservas = new AdapterReservas();
	}

	/*
	 * Crea una reserva
	 */
	@PostMapping(value = "/api/crear-reserva/{espacio}")
	@ApiOperation(value = "Crea una reserva para un espacio", notes = "Devuelve estado de la reserva" )
	public ResponseEntity<String> crearReserva(
		@ApiParam(value = "Id del espacio que se quiere reservar", required = true) @PathVariable String espacio, 
		@ApiParam(value = "Parametros de reserva", required = true) @RequestBody ReservaRequest reserva) 
			throws Exception {

		adapterReservas.enviarReserva(espacio,reserva);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirReserva());
	}

	/*
	 * Acepta una reserva
	 */
	@PutMapping(value = "/api/aceptar-reserva/{reserva}")
	@ApiOperation(value = "Acepta una reserva para un espacio", notes = "Devuelve estado de la reserva" )
	public ResponseEntity<String> aceptarReserva(
		@ApiParam(value = "Id de la reserva que se quiere aceptar", required = true) @PathVariable String reserva) 
			throws Exception {

		adapterReservas.enviarAceptarReserva(reserva);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirAceptarReserva());
	}

	/*
	 * Cancela una reserva
	 */
	@PutMapping(value = "/api/cancelar-reserva/{reserva}")
	@ApiOperation(value = "Cancela una reserva para un espacio", notes = "Devuelve estado de la reserva" )
	public ResponseEntity<String> cancelarReserva(
		@ApiParam(value = "Id de la reserva que se quiere cancelar", required = true) @PathVariable String reserva) 
			throws Exception {

		adapterReservas.enviarCancelarReserva(reserva);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirCancelarReserva());
	}

	/*
	 * Obtiene las reservas de un espacio dado su id
	 */
	@GetMapping(value = "/api/reservas/{espacio}")
	@ApiOperation(value = "Busqueda de reservas correspondientes a un espacio", notes = "Devuelve lista de reservas correspondientes a un espacio")
	public ResponseEntity<JSONArray> getReservas(
		@ApiParam(value = "Id del espacio", required = true) @PathVariable String espacio) 
			throws Exception {

		adapterReservas.enviarGetReservas(espacio);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirGetReservas());
	}

	/*
	 * Obtiene las reservas de un espacio dado su id
	 */
	@GetMapping(value = "/api/reservas/{espacio}/{estado}")
	@ApiOperation(value = "Busqueda de reservas correspondientes a un espacio", notes = "Devuelve lista de reservas correspondientes a un espacio")
	public ResponseEntity<JSONArray> getReservas(
		@ApiParam(value = "Id del espacio", required = true) @PathVariable String espacio, 
		@ApiParam(value = "Estado de la reserva (PENDIENTE, PENDIENTEPAGO, ACEPTADA, CANCELADA)", required = true) @PathVariable String estado) 
			throws Exception {

		adapterReservas.enviarGetReservas(espacio, estado);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirGetReservas());
	}

	/*
	 * Obtiene las reservas de un usuario dado su id
	 */
	@GetMapping(value = "/api/reservas-usuario/{usuario}")
	@ApiOperation(value = "Busqueda de reservas correspondientes a un usuario", notes = "Devuelve lista de reservas correspondientes a un usuario")
	public ResponseEntity<JSONArray> getReservasUsuario(
		@ApiParam(value = "Id del usuario", required = true) @PathVariable String usuario) 
			throws Exception {

		adapterReservas.enviarGetReservasUsuario(usuario);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirGetReservasUsuario());
	}
	
}
