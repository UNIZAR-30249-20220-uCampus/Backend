package es.ucampus.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import domainObjects.entity.Espacio;
import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;
import es.ucampus.demo.DemoApplication;
import es.ucampus.demo.repository.RepositorioEspacios;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ServiciosEspacioTest {

	private ServiciosEspacio serviciosEspacio;

	private CriteriosBusquedaDTO criteriosBusquedaDTO;

	@Before
	public void init() throws JsonMappingException, JsonProcessingException {
		
		String json = "{'alquilable':0,'tarifa':0.0,'id_espacio':'\"CRE.1200.01.050\"','id_edificio':'\"CRE.1200.\"','superficie':'\"118,9\"','reservable':0," +
		"'equipamientos':[{'tipo':'CANYON_FIJO','cantidad':1},{'tipo':'NMRO_PLAZAS','cantidad':80}],'id_utc':'01.050'}";
		Espacio espacio = new Gson().fromJson(json, Espacio.class);

		List<Espacio> espacios = new ArrayList<Espacio>();
		espacios.add(espacio);

		String criteriosJson = "{'nombre':'\"CRE.1200.01.050\"','aforo':5, 'filtrosActivos': ['AFORO','PANTALLA_PROYECTOR','PIZARRA']," +
		"'equipamientos':[{'tipo':'CANYON_FIJO','cantidad':1},{'tipo':'NMRO_PLAZAS','cantidad':80}], " + "'horarioRequest': { 'horario' : {"
		+ "'fechaInicio' : '2020-05-30T08:20:38.5426521-04:00',"
		+ "'fechaFin' : '2020-05-30T08:20:38.5426521-06:00'," + "'frecuencia' : 2," + "'conjuntoDiaSlots' : [{"
		+ "'diaSemana' : 3," + "'slotInicio' : 1," + "'slotFinal': 4" + "}]" + "}" +"}}";
		criteriosBusquedaDTO = new Gson().fromJson(criteriosJson, CriteriosBusquedaDTO.class);
		
		List<Integer> numEq = criteriosBusquedaDTO.cantidadEquipamientos();
		String nombreEspacio = "\"" + criteriosBusquedaDTO.getNombre() + "\"";

		RepositorioEspacios repositorioEspacios = Mockito.mock(RepositorioEspacios.class);

		Mockito.when(repositorioEspacios.findById("\"CRE.1065.00.021\"")).thenReturn(Optional.empty());
		Mockito.when(repositorioEspacios.findById("\"CRE.1200.01.050\"")).thenReturn(Optional.of(espacio));
		Mockito.when(repositorioEspacios.findByCoordenadas(1, 675745.92064, 4616800.60363)).thenReturn("\"CRE.1200.01.050\"");
		Mockito.when(repositorioEspacios.findByCoordenadas(7, 675745.92064, 4616800.60363)).thenReturn(null);
		Mockito.when(repositorioEspacios.establecerEquipamiento(nombreEspacio, numEq.get(0), numEq.get(1), numEq.get(2),
			Integer.toString(numEq.get(3)), numEq.get(4), numEq.get(5), numEq.get(6), numEq.get(7), numEq.get(8),
			numEq.get(9), numEq.get(10), numEq.get(11), numEq.get(12), numEq.get(13))).thenReturn(5);
		Mockito.when(repositorioEspacios
					.findByPlazasGreaterThanEqualAndCanyonGreaterThanEqualAndProyectorGreaterThanEqualAndSonidoGreaterThanEqualAndTvGreaterThanEqualAndVideoGreaterThanEqualAndDvdGreaterThanEqualAndFotocopiadorasGreaterThanEqualAndImpresorasGreaterThanEqualAndOrdenadoresGreaterThanEqualAndFaxesGreaterThanEqualAndTelefonosGreaterThanEqualAndPizarraGreaterThanEqualAndExtpolvoGreaterThanEqualAndExtco2(
						criteriosBusquedaDTO.getAforo(), numEq.get(0), numEq.get(1), numEq.get(2), Integer.toString(numEq.get(3)),
							numEq.get(4), numEq.get(5), numEq.get(6), numEq.get(7), numEq.get(8), numEq.get(9),
							numEq.get(10), numEq.get(11), numEq.get(12), numEq.get(13))).thenReturn(espacios);

		serviciosEspacio = new ServiciosEspacioImpl(repositorioEspacios);


	}
	
	@Test
	@Ignore
	public void contexLoads() throws Exception {
		assertThat(serviciosEspacio).isNotNull();
	}

	@Test
	public void test_GET_ESPACIO_ID() throws Exception {

		Espacio espacio = serviciosEspacio.getEspacioId("\"CRE.1200.01.050\"");
		assertNotNull(espacio);
	}

	@Test
	public void test_GET_ESPACIO_ERROR() throws Exception {

		Espacio espacio = serviciosEspacio.getEspacioId("\"CRE.1065.00.021\"");
		assertNull(espacio);
	}

	@Test
	public void test_GET_EspacioDTO_ID() throws Exception {

		EspacioDTO espacio = serviciosEspacio.getEspacioDTOId("\"CRE.1200.01.050\"");
		assertNotNull(espacio);
	}

	@Test
	public void test_GET_EspacioDTO_ERROR() throws Exception {

		EspacioDTO espacio = serviciosEspacio.getEspacioDTOId("\"CRE.1065.00.021\"");
		assertNull(espacio);
	}

	@Test
	public void test_GET_ESPACIO_COOR() throws Exception {

		EspacioDTO espacio = serviciosEspacio.getEspacioCoordenadas(1, 675745.92064, 4616800.60363);
		assertNotNull(espacio);
	}

	@Test
	public void test_GET_ESPACIO_COOR_ERROR() throws Exception {

		EspacioDTO espacio = serviciosEspacio.getEspacioCoordenadas(7, 675745.92064, 4616800.60363);
		assertNull(espacio);
	}

	@Test
	public void test_GET_ESPACIOS_AFORO() throws Exception {

		List<EspacioDTO> espacios = serviciosEspacio.buscarEspacioPorAforo(5);
		assertNotNull(espacios);
	}

	@Test
	public void test_GET_ESPACIOS_ALQUILABLES() throws Exception {

		List<EspacioDTO> espaciosAlquilables0 = serviciosEspacio.getEspaciosAlquilables(0);
		assertNotNull(espaciosAlquilables0);

		List<EspacioDTO> espaciosAlquilables1 = serviciosEspacio.getEspaciosAlquilables(1);
		assertNotNull(espaciosAlquilables1);
	}

	@Test
	public void test_GET_TARIFA_ESPACIO_ERROR() throws Exception {

		double tarifa = serviciosEspacio.calcularTarifaEspacioAlquilable("\"CRE.1065.00.021\"");
		assertEquals(0, tarifa, 0);
	}


	@Test
	public void test_BUSQUEDA_CRITERIOS() throws Exception {

		List<EspacioDTO> espacios = serviciosEspacio.buscarEspacioPorCriterios(criteriosBusquedaDTO);
		assertNotNull(espacios);
	}

}