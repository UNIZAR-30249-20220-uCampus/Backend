package es.ucampus.demo.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;
import domainObjects.valueObject.EstadoReserva;
import domainObjects.valueObject.Horario;
import dtoObjects.entity.ReservaDTO;
import es.ucampus.demo.DemoApplication;
import es.ucampus.demo.repository.RepositorioReservas;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { DemoApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ServiciosReservaTest {

	private ServiciosReserva serviciosReserva;

	Espacio espacio;
	Reserva reserva;
	Reserva alquiler;

	@Before
	public void before() throws JsonMappingException, JsonProcessingException {
		this.espacio = new Espacio();

		String json = "{'id_espacio':'\"CRE.1200.01.050\"'}";
		this.espacio = new Gson().fromJson(json, Espacio.class);
	
		Horario horario = new Horario(new Date(), new Date(), 2);
		reserva = new Reserva(espacio, horario, "Alex", "reserva");
		String jsonReserva = "{" + "'id': 200," + "'tipo': 'alquiler'," + "'usuario' : 'Alex'," + "'horario' : {"
				+ "'fechaInicio' : '2020-05-30T08:20:38.5426521-04:00',"
				+ "'fechaFin' : '2020-05-30T08:20:38.5426521-06:00'," + "'frecuencia' : 2," + "'conjuntoDiaSlots' : [{"
				+ "'diaSemana' : 3," + "'slotInicio' : 1," + "'slotFinal': 4" + "}]" + "}" + "}";
		// Now do the magic.
		alquiler = new Gson().fromJson(jsonReserva, Reserva.class);

		List<Reserva> listaReservas = new ArrayList<Reserva>();
		listaReservas.add(this.reserva);

		RepositorioReservas repositorioReservas = Mockito.mock(RepositorioReservas.class);
		// test_HACER_RESERVA, test_GET_RESERVAS_ESPACIO, test_GET_RESERVAS_ESPACIO_ESTADO, test_GET_RESERVAS_USUARIO, test_GET_RESERVAS_USUARIO_ESTADO
		Mockito.when(repositorioReservas.findByEspacio(this.espacio)).thenReturn(listaReservas);
		Mockito.when(repositorioReservas.findByUsuario("Alex")).thenReturn(listaReservas);
		Mockito.when(repositorioReservas.findAll()).thenReturn(listaReservas);
		Mockito.when(repositorioReservas.save(this.reserva)).thenReturn(this.reserva);
		Mockito.when(repositorioReservas.findById((long) 200)).thenReturn(Optional.of(alquiler));

		serviciosReserva = new ServiciosReservaImpl(repositorioReservas);

			
	}

	@Test
	public void contexLoads() throws Exception {
		assertThat(serviciosReserva).isNotNull();
	}

	@Test
	public void test_HACER_RESERVA() throws Exception {

		boolean ok = serviciosReserva.hacerReserva(reserva);
		assertTrue(ok);
	}

	@Test
	public void test_GET_RESERVAS() throws Exception {

		List<ReservaDTO> reservas = serviciosReserva.buscarReserva();
		assertNotNull(reservas);
	}

	@Test
	public void test_GET_RESERVAS_ESTADO() throws Exception {

		List<ReservaDTO> reservas = serviciosReserva.buscarReservaEstado(EstadoReserva.PENDIENTE);
		assertNotNull(reservas);
	}

	@Test
	public void test_GET_RESERVAS_ESPACIO() throws Exception {

		List<ReservaDTO> reservas = serviciosReserva.buscarReserva(espacio);
		assertNotNull(reservas);
	}

	@Test
	public void test_GET_RESERVAS_ESPACIO_ESTADO() throws Exception {

		List<ReservaDTO> reservas = serviciosReserva.buscarReservaEstado(espacio, EstadoReserva.PENDIENTE);
		assertNotNull(reservas);
	}

	@Test
	public void test_GET_RESERVAS_USUARIO() throws Exception {

		List<ReservaDTO> reservas = serviciosReserva.buscarReservaUsuario("Alex");
		assertNotNull(reservas);
	}

	@Test
	public void test_GET_RESERVAS_USUARIO_ESTADO() throws Exception {

		List<ReservaDTO> reservas = serviciosReserva.buscarReservaUsuarioEstado("Alex",EstadoReserva.PENDIENTE);
		assertNotNull(reservas);
	}

	@Test
	public void test_ACEPTAR_RESERVA() throws Exception {
		boolean aceptada = serviciosReserva.aceptarReserva(alquiler.getId());
		assertTrue(aceptada);
	}

	@Test
	public void test_ACEPTAR_RESERVA_ERROR() throws Exception {
		boolean aceptada = serviciosReserva.aceptarReserva((long) 150);
		assertFalse(aceptada);
	}


	@Test
	public void test_CANCELAR_RESERVA() throws Exception {
		boolean cancelada = serviciosReserva.cancelarReserva(alquiler.getId());
		assertTrue(cancelada);
	}

	@Test
	public void test_CANCELAR_RESERVA_ERROR() throws Exception {
		boolean cancelada = serviciosReserva.cancelarReserva((long) 150);
		assertFalse(cancelada);
	}

	@Test
	public void test_PAGAR_RESERVA() throws Exception {
		serviciosReserva.aceptarReserva(alquiler.getId());

		boolean pagada = serviciosReserva.pagarReserva(alquiler.getId());
		assertTrue(pagada);
	}

	@Test
	public void test_PAGAR_RESERVA_ERROR() throws Exception {
		boolean pagada = serviciosReserva.pagarReserva((long) 150);
		assertFalse(pagada);
	}
	
}