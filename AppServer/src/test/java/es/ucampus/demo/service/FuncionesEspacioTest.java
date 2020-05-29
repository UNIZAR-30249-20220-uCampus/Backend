package es.ucampus.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import domainObjects.entity.Espacio;
import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;
import es.ucampus.demo.DemoApplication;

import org.json.simple.JSONArray;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class FuncionesEspacioTest {
	
	@Autowired
	private FuncionesEspacio funcionesEspacios;

	@Test
	public void contexLoads() throws Exception {
		assertThat(funcionesEspacios).isNotNull();
	}

	@Test
	public void test_GET_ESPACIO_ID() throws Exception {

		Espacio espacio = funcionesEspacios.getEspacioId("\"CRE.1200.01.050\"");
		assertNotNull(espacio);
	}

	@Test
	public void test_GET_ESPACIO_ERROR() throws Exception {

		Espacio espacio = funcionesEspacios.getEspacioId("\"CRE.1065.00.021\"");
		assertNull(espacio);
	}

	@Test
	public void test_GET_EspacioDTO_ID() throws Exception {

		EspacioDTO espacio = funcionesEspacios.getEspacioDTOId("\"CRE.1200.01.050\"");
		assertNotNull(espacio);
	}

	@Test
	public void test_GET_EspacioDTO_ERROR() throws Exception {

		EspacioDTO espacio = funcionesEspacios.getEspacioDTOId("\"CRE.1065.00.021\"");
		assertNull(espacio);
	}

	@Test
	public void test_GET_ESPACIO_COOR() throws Exception {

		EspacioDTO espacio = funcionesEspacios.getEspacioCoordenadas(1, 675745.92064, 4616800.60363);
		assertNotNull(espacio);
	}

	@Test
	public void test_GET_ESPACIO_COOR_ERROR() throws Exception {

		EspacioDTO espacio = funcionesEspacios.getEspacioCoordenadas(7, 675745.92064, 4616800.60363);
		assertNull(espacio);
	}

	@Test
	public void test_GET_ESPACIOS_AFORO() throws Exception {

		List<EspacioDTO> espacios = funcionesEspacios.buscarEspacioPorAforo(5);
		assertNotNull(espacios);
	}

	@Test
	public void test_GET_ESPACIOS_ALQUILABLES() throws Exception {

		List<EspacioDTO> espaciosAlquilables0 = funcionesEspacios.getEspaciosAlquilables(0);
		assertNotNull(espaciosAlquilables0);

		List<EspacioDTO> espaciosAlquilables1 = funcionesEspacios.getEspaciosAlquilables(1);
		assertNotNull(espaciosAlquilables1);
	}

	@Test
	@Ignore
	public void test_GET_TARIFA_ESPACIO() throws Exception {

		double tarifa = funcionesEspacios.calcularTarifaEspacioAlquilable("\"CRE.1200.01.050\"");
		assertNotEquals(0, tarifa, "No son iguales");
	}

	@Test
	public void test_GET_TARIFA_ESPACIO_ERROR() throws Exception {

		double tarifa = funcionesEspacios.calcularTarifaEspacioAlquilable("\"CRE.1065.00.021\"");
		assertEquals(0, tarifa, 0);
	}
}