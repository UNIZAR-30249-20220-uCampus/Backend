package es.ucampus.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.ucampus.demo.adapter.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import org.json.simple.JSONArray;
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
@Api(value = "Espacios", description = "Operaciones para la gestión de espacios")
public class EspacioController {

	private AdapterEspacios adapterEspacios;

	public EspacioController() throws IOException {
		adapterEspacios = new AdapterEspacios("WebASpringEspacios", "SpringAWebEspacios");
	}

	public EspacioController(AdapterEspacios adapterEspacios) throws IOException{
		this.adapterEspacios = adapterEspacios;
	}

	@GetMapping(value = "/api/conexion")
	@ApiOperation(value = "Conexión con servidor", notes = "Devuelve estado del backend")
	public ResponseEntity<String> conexion1() throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}

	/*
	 * Dado el identificador de un espacio obtener su informacion
	 */
	/*
	 * @GetMapping(value = "/api/espacios/{id}")
	 * 
	 * @ApiOperation(value = "Busqueda de un espacio por id", notes =
	 * "Devuelve información de un espacio" ) public ResponseEntity<EspacioDTO>
	 * getEspacioId(
	 * 
	 * @ApiParam(value =
	 * "Id del espacio desde el que se recuperará el objeto espacio", required =
	 * true) @PathVariable String id) throws Exception {
	 * 
	 * AdapterEspacios.enviarGetEspacio(id);
	 * 
	 * String res = AdapterEspacios.recibirGetEspacioS(); HttpStatus codigo =
	 * HttpStatus.OK;
	 * 
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); EspacioDTO espacio =
	 * mapper.readValue(res, EspacioDTO.class);
	 * 
	 * if(res.equals("No encontrado")){ codigo = HttpStatus.NOT_FOUND; }
	 * 
	 * return ResponseEntity.status(codigo).body(espacio); }
	 */

	/*
	 * Dada la planta y las coordenadas de un espacio obtener su informacion
	 */
	@GetMapping(path = "/api/espacios/{planta}/{x}/{y}")
	@ApiOperation(value = "Busqueda de un espacio por planta y coordenadas", notes = "Devuelve información de un espacio")
	public ResponseEntity<EspacioDTO> getSpace(
			@ApiParam(value = "Planta donde se encuentra el espacio", required = true) @PathVariable int planta,
			@ApiParam(value = "Coordenada long del espacio", required = true) @PathVariable double x,
			@ApiParam(value = "Coordenada lat del espacio", required = true) @PathVariable double y) throws Exception {

		adapterEspacios.enviarGetEspacio(planta, x, y);

		String res = adapterEspacios.recibirGetEspacioS();
		HttpStatus codigo = HttpStatus.OK;

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

		adapterEspacios.enviarbuscarEspacio(busquedaRequest);
		return ResponseEntity.status(HttpStatus.OK).body(adapterEspacios.recibirBuscarEspacio());
	}

	/*
	 * Establecer cantidad de equipamiento en un espacio determinado.
	 */
	@PostMapping(value = "/api/equipamiento")
	@ApiOperation(value = "Establece el equipamiento de un espacio")
	public ResponseEntity<String> estableceEquipamiento(HttpServletRequest request,
			@RequestBody CriteriosBusquedaDTO equipRequest) throws Exception {

		adapterEspacios.establecerEquipamiento(equipRequest);
		return ResponseEntity.status(HttpStatus.OK).body(adapterEspacios.recibirEstablecerEquipamiento());
	}

	/*
	 * Dada la planta devuelve una lista con los espacios alquilables
	 */
	/*
	 * @GetMapping(path = "/api/espacios-alquilables/{planta}")
	 * 
	 * @ApiOperation(value = "Busqueda de espacios alquilables", notes =
	 * "Devuelve lista de espacios alquilables") public ResponseEntity<JSONArray>
	 * getEspaciosAlquilables(
	 * 
	 * @ApiParam(value = "Planta donde se encuentra el espacio", required =
	 * true) @PathVariable int planta) throws Exception {
	 * 
	 * AdapterEspacios.enviarGetEspaciosAlquilables(planta); return
	 * ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.
	 * recibirGetEspaciosAlquilables()); }
	 */
	/*
	 * Dada la planta devuelve una lista con los espacios alquilables
	 */
	/*
	 * @GetMapping(path = "/api/tarifa-espacio/{id}")
	 * 
	 * @ApiOperation(value = "Obtiene la tarifa de un espacio", notes =
	 * "Devuelve la tarifa de un espacios") public ResponseEntity<String>
	 * calcularTarifaEspacioAlquilable(
	 * 
	 * @ApiParam(value =
	 * "Id del espacio desde el que se recuperará el objeto espacio", required =
	 * true) @PathVariable String id) throws Exception {
	 * 
	 * AdapterEspacios.enviarCalcularTarifaEspacioAlquilable(id); return
	 * ResponseEntity.status(HttpStatus.OK).body(AdapterEspacios.
	 * recibirCalcularTarifaEspacioAlquilable()); }
	 */

}
