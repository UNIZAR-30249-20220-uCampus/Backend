package es.ucampus.demo.controller;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dtoObjects.entity.ReservaDTO2;
import es.ucampus.demo.adapterAMQP.*;

@RestController
public class ReservasController {

	
	private AdapterEspacios AdapterEspacios;

	public ReservasController() throws IOException{
		AdapterEspacios = new AdapterEspacios();
	}

	/*
	 * Crea una reserva
	 */
    @PostMapping(value = "/api/crear-reserva/{espacio}")
	public ResponseEntity<String> crearReserva(@PathVariable String espacio, @RequestBody ReservaDTO2 reserva) throws Exception {

		AdapterEspacios.enviarReserva(espacio,reserva);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirReserva());
	}

	/*
	 * Acepta una reserva
	 */
	@PutMapping(value = "/api/aceptar-reserva/{reserva}")
	public ResponseEntity<String> aceptarReserva(@PathVariable String reserva) throws Exception {

		AdapterEspacios.enviarAceptarReserva(reserva);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirAceptarReserva());
	}

	/*
	 * Cancela una reserva
	 */
	@PutMapping(value = "/api/cancelar-reserva/{reserva}")
	public ResponseEntity<String> cancelarReserva(@PathVariable String reserva) throws Exception {

		AdapterEspacios.enviarCancelarReserva(reserva);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirCancelarReserva());
	}

	/*
	 * Obtiene las reservas de un espacio dado su id
	 */
	@GetMapping(value = "/api/reservas/{espacio}")
	public ResponseEntity<JSONArray> getReservas(@PathVariable String espacio) throws Exception {

		AdapterEspacios.enviarGetReservas(espacio);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirGetReservas());
	}

	/*
	 * Obtiene las reservas de un usuario dado su id
	 */
	@GetMapping(value = "/api/reservas-usuario/{usuario}")
	public ResponseEntity<JSONArray> getReservasUsuario(@PathVariable String usuario) throws Exception {

		AdapterEspacios.enviarGetReservasUsuario(usuario);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirGetReservasUsuario());
	}
	
}
