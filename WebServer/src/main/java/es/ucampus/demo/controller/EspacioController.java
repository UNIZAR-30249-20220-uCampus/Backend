package es.ucampus.demo.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import es.ucampus.demo.adapter.*;
import com.google.gson.JsonObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;

@RestController
public class EspacioController {

	private AdapterEspacios AdapterEspacios;

	public EspacioController() throws IOException {
		AdapterEspacios = new AdapterEspacios();
	}

	@GetMapping(value = "/api/conexion")
	public ResponseEntity<String> conexion1() throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}

	/*
	 * Dado el identificador de un espacio obtener su informacion
	 */
	@GetMapping(value = "/api/espacios/{id}")
	public ResponseEntity<EspacioDTO> getEspacioId(@PathVariable String id) throws Exception {

		AdapterEspacios.enviarGetEspacio(id);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirGetEspacio());
	}

	/*
	 * Dada la planta y las coordenadas de un espacio obtener su informacion
	 */
	@GetMapping(path = "/api/espacios/{planta}/{x}/{y}")
	public ResponseEntity<EspacioDTO> getSpace(@PathVariable int planta, @PathVariable double x, @PathVariable double y)
			throws Exception {

		AdapterEspacios.enviarGetEspacio(planta, x, y);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirGetEspacio());
	}

	/*
	 * Busqueda de espacios segun criterios
	 */
	@PostMapping(value = "/api/buscar-espacio")
	public ResponseEntity<JSONArray> buscarEspacio(HttpServletRequest request,
			@RequestBody CriteriosBusquedaDTO busquedaRequest) throws Exception {

		AdapterEspacios.enviarbuscarEspacio(busquedaRequest);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirBuscarEspacio());
	}

	/*
	 * Establecer cantidad de equipamiento en un espacio determinado.
	 */
	@PostMapping(value = "/api/equipamiento")
	public ResponseEntity<String> estableceEquipamiento(HttpServletRequest request,
			@RequestBody CriteriosBusquedaDTO equipRequest) throws Exception {

		AdapterEspacios.establecerEquipamiento(equipRequest);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirEstablecerEquipamiento());
	}

	/*
	 * Dada la planta devuelve una lista con los espacios alquilables
	 */
	@GetMapping(path = "/api/espacios-alquilables/{planta}")
	public ResponseEntity<JSONArray> getEspaciosAlquilables(@PathVariable int planta) throws Exception {

		AdapterEspacios.enviarGetEspaciosAlquilables(planta);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirGetEspaciosAlquilables());
	}

	/*
	 * Dada la planta devuelve una lista con los espacios alquilables
	 */
	@GetMapping(path = "/api/tarifa-espacio/{id}")
	public ResponseEntity<String> calcularTarifaEspacioAlquilable(@PathVariable String id) throws Exception {

		AdapterEspacios.enviarCalcularTarifaEspacioAlquilable(id);
		return ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.recibirCalcularTarifaEspacioAlquilable());
	}

}
