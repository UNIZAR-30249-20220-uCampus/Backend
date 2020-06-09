package es.ucampus.demo.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.ucampus.demo.adapter.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;

@RestController
@Api(value = "Espacios", description = "Operaciones para la gestión de espacios")
public class EspacioController {

	private AdapterEspacios adapterEspacios;

	public EspacioController() throws IOException {
		adapterEspacios = new AdapterEspacios("WebASpringEspacios", "SpringAWebEspacios");
	}

	public EspacioController(AdapterEspacios adapterEspacios) throws IOException{
		this.adapterEspacios = adapterEspacios;
	}

	/*
	 * Comprueba el estado del servidor
	 */
	@GetMapping(value = "/api/conexion")
	@ApiOperation(value = "Conexión con servidor", notes = "Devuelve estado del backend")
	public ResponseEntity<String> conexion1() throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}

	/*
	 * Dada la planta y las coordenadas de un espacio obtener su informacion
	 */
	@GetMapping(path = "/api/espacios/{planta}/{x}/{y}")
	@ApiOperation(value = "Busqueda de un espacio por planta y coordenadas", notes = "Devuelve información de un espacio")
	public ResponseEntity<EspacioDTO> getEspacio(
			@ApiParam(value = "Planta donde se encuentra el espacio", required = true) @PathVariable int planta,
			@ApiParam(value = "Coordenada long del espacio", required = true) @PathVariable double x,
			@ApiParam(value = "Coordenada lat del espacio", required = true) @PathVariable double y) throws Exception {

		//envia los datos
		adapterEspacios.enviarGetEspacio(planta, x, y);

		//recibe la respuesta del servidor
		String res = adapterEspacios.recibirEspacio();
		HttpStatus codigo = HttpStatus.OK;

		//espacio no encontrado segun ese id
		if (res.equals("No encontrado")) {
			codigo = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(codigo).body(null);
		}

		ObjectMapper mapper = new ObjectMapper();
		EspacioDTO espacio = mapper.readValue(res, EspacioDTO.class);

		return ResponseEntity.status(codigo).body(espacio);
	}

	/*
	 * Busqueda de espacios segun criterios
	 */
	@PostMapping(value = "/api/buscar-espacio")
	@ApiOperation(value = "Busqueda de espacios segun criterios", notes = "Devuelve lista de espacios con esas características")
	public ResponseEntity<JSONArray> buscarEspacio(
			@ApiParam(value = "Criterios de busqueda", required = true) @RequestBody CriteriosBusquedaDTO busquedaRequest)
			throws Exception {

		//envia los datos
		adapterEspacios.enviarbuscarEspacio(busquedaRequest);

		//recibe la respuesta del servidor
		String res = adapterEspacios.recibirEspacio();
		HttpStatus codigo = HttpStatus.OK;
		//crea el json
		JSONParser parser = new JSONParser();
		JSONArray json = (JSONArray) parser.parse(res);

		return ResponseEntity.status(codigo).body(json);
	}

	/*
	 * Establecer cantidad de equipamiento en un espacio determinado.
	 */
	@PostMapping(value = "/api/equipamiento")
	@ApiOperation(value = "Establece el equipamiento de un espacio")
	public ResponseEntity<String> estableceEquipamiento(HttpServletRequest request,
			@RequestBody CriteriosBusquedaDTO equipRequest) throws Exception {

		//envia los datos
		adapterEspacios.establecerEquipamiento(equipRequest);

		//recibe la respuesta del servidor
		String res = adapterEspacios.recibirEspacio();
		HttpStatus codigo = HttpStatus.OK;

		//reserva no encontrada segun ese id
		if(res.equals("FALLO")){
			codigo = HttpStatus.BAD_REQUEST;
		}
		return ResponseEntity.status(codigo).body(res);
	}

	/*
	 * Cambia un espacio a reservable o no reservable.
	 */
	@PutMapping(value = "/api/cambiar-reservable/{espacio}/{estado}")
	@ApiOperation(value = "Cambia un espacio a reservable o no reservable")
	public ResponseEntity<String> cambiarReservable(
		@ApiParam(value = "Id del espacio que se quiere cambiar", required = true) @PathVariable String espacio,
		@ApiParam(value = "Estado del espacio (0-No reservable, 1-Reservable)", required = true) @PathVariable int estado
	) throws Exception {

		//envia los datos
		adapterEspacios.cambiarAlquilable(espacio,estado,"reservable");

		//recibe la respuesta del servidor
		String res = adapterEspacios.recibirEspacio();
		HttpStatus codigo = HttpStatus.OK;

		//espacio no encontrado segun ese id
		if (res.equals("No encontrado")) {
			codigo = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(codigo).body(res);
		}

		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	/*
	 * Cambia un espacio a alquilable o no alquilable.
	 */
	@PutMapping(value = "/api/cambiar-alquilable/{espacio}/{estado}")
	@ApiOperation(value = "Cambia un espacio a alquilable o no alquilable")
	public ResponseEntity<String> cambiarAlquilable(
		@ApiParam(value = "Id del espacio que se quiere cambiar", required = true) @PathVariable String espacio,
		@ApiParam(value = "Estado del espacio (0-No alquilable, 1-Alquilable)", required = true) @PathVariable int estado
	) throws Exception {

		//envia los datos
		adapterEspacios.cambiarAlquilable(espacio,estado,"alquilable");

		//recibe la respuesta del servidor
		String res = adapterEspacios.recibirEspacio();
		HttpStatus codigo = HttpStatus.OK;

		//espacio no encontrado segun ese id
		if (res.equals("No encontrado")) {
			codigo = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(codigo).body(res);
		}

		return ResponseEntity.status(HttpStatus.OK).body(res);
	}



}
