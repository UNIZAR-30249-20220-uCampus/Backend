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

@RestController
public class ReservasController {

	
	private AdapterReservas adapterReservas;

	public ReservasController() throws IOException{
		adapterReservas = new AdapterReservas();
	}

	/*
	 * Crea una reserva
	 */
    @PostMapping(value = "/api/crear-reserva/{espacio}")
	public ResponseEntity<String> crearReserva(@PathVariable String espacio, @RequestBody ReservaRequest reserva) throws Exception {

		adapterReservas.enviarReserva(espacio,reserva);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirReserva());
	}

	/*
	 * Acepta una reserva
	 */
	@PutMapping(value = "/api/aceptar-reserva/{reserva}")
	public ResponseEntity<String> aceptarReserva(@PathVariable String reserva) throws Exception {

		adapterReservas.enviarAceptarReserva(reserva);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirAceptarReserva());
	}

	/*
	 * Cancela una reserva
	 */
	@PutMapping(value = "/api/cancelar-reserva/{reserva}")
	public ResponseEntity<String> cancelarReserva(@PathVariable String reserva) throws Exception {

		adapterReservas.enviarCancelarReserva(reserva);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirCancelarReserva());
	}

	/*
	 * Obtiene las reservas de un espacio dado su id
	 */
	@GetMapping(value = "/api/reservas/{espacio}")
	public ResponseEntity<JSONArray> getReservas(@PathVariable String espacio) throws Exception {

		adapterReservas.enviarGetReservas(espacio);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirGetReservas());
	}

	/*
	 * Obtiene las reservas de un espacio dado su id
	 */
	@GetMapping(value = "/api/reservas/{espacio}/{estado}")
	public ResponseEntity<JSONArray> getReservas(@PathVariable String espacio, @PathVariable String estado) throws Exception {

		adapterReservas.enviarGetReservas(espacio, estado);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirGetReservas());
	}

	/*
	 * Obtiene las reservas de un usuario dado su id
	 */
	@GetMapping(value = "/api/reservas-usuario/{usuario}/{estado}")
	public ResponseEntity<JSONArray> getReservasUsuario(@PathVariable String usuario, @PathVariable String estado) throws Exception {

		adapterReservas.enviarGetReservasUsuario(usuario);
		return ResponseEntity.status(HttpStatus.OK).body(adapterReservas.recibirGetReservasUsuario());
	}
	
}
