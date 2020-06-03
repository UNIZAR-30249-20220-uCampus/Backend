package es.ucampus.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import es.ucampus.demo.DemoApplication;
import es.ucampus.demo.repository.RepositorioEspacios;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ServiciosEspacioTest {

	private ServiciosEspacio serviciosEspacio;

	@Before
	public void init() throws JsonMappingException, JsonProcessingException {
		RepositorioEspacios repositorioEspacios = Mockito.mock(RepositorioEspacios.class);
		Mockito.when(repositorioEspacios.findById("\"CRE.1200.01.050\"")).thenReturn(Optional.of(new Espacio()));
		Mockito.when(repositorioEspacios.findById("\"CRE.1065.00.021\"")).thenReturn(Optional.empty());

 
		String json = "{'id_espacio':'\"CRE.1200.01.050\"'}";
		Espacio espacio = new Gson().fromJson(json, Espacio.class);
		//ObjectMapper objectMapper = new ObjectMapper();
		//Espacio espacio = objectMapper.readValue(json, Espacio.class);
		Mockito.when(repositorioEspacios.findById("\"CRE.1200.01.050\"")).thenReturn(Optional.of(espacio));
		
		Mockito.when(repositorioEspacios.findById("\"CRE.1200.01.050\"")).thenReturn(Optional.of(espacio));

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
	@Ignore
	public void test_GET_EspacioDTO_ERROR() throws Exception {

		EspacioDTO espacio = serviciosEspacio.getEspacioDTOId("\"CRE.1065.00.021\"");
		assertNull(espacio);
	}

	@Test
	@Ignore
	public void test_GET_ESPACIO_COOR() throws Exception {

		EspacioDTO espacio = serviciosEspacio.getEspacioCoordenadas(1, 675745.92064, 4616800.60363);
		assertNotNull(espacio);
	}

	@Test
	@Ignore
	public void test_GET_ESPACIO_COOR_ERROR() throws Exception {

		EspacioDTO espacio = serviciosEspacio.getEspacioCoordenadas(7, 675745.92064, 4616800.60363);
		assertNull(espacio);
	}

	@Test
	@Ignore
	public void test_GET_ESPACIOS_AFORO() throws Exception {

		List<EspacioDTO> espacios = serviciosEspacio.buscarEspacioPorAforo(5);
		assertNotNull(espacios);
	}

	@Test
	@Ignore
	public void test_GET_ESPACIOS_ALQUILABLES() throws Exception {

		List<EspacioDTO> espaciosAlquilables0 = serviciosEspacio.getEspaciosAlquilables(0);
		assertNotNull(espaciosAlquilables0);

		List<EspacioDTO> espaciosAlquilables1 = serviciosEspacio.getEspaciosAlquilables(1);
		assertNotNull(espaciosAlquilables1);
	}

	@Test
	@Ignore
	public void test_GET_TARIFA_ESPACIO() throws Exception {

		double tarifa = serviciosEspacio.calcularTarifaEspacioAlquilable("\"CRE.1200.01.050\"");
		assertNotEquals(0, tarifa, "No son iguales");
	}

	@Test
	@Ignore
	public void test_GET_TARIFA_ESPACIO_ERROR() throws Exception {

		double tarifa = serviciosEspacio.calcularTarifaEspacioAlquilable("\"CRE.1065.00.021\"");
		assertEquals(0, tarifa, 0);
	}
}