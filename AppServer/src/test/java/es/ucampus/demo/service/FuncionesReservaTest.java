package es.ucampus.demo.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
public class FuncionesReservaTest {

	
	private FuncionesReserva funcionesReserva;

	private Espacio espacio;

	private Reserva reserva;

	@Before
    public void before() {
		//this.espacio = 
		Horario horario = new Horario(new Date(), new Date(), 2);
		this.reserva = new Reserva(new Espacio(), horario, "Alex", "reserva");
		List<Reserva> listaReservas = new ArrayList<Reserva>();
		listaReservas.add(this.reserva);
		final RepositorioReservas repositorioReservas = Mockito.mock(RepositorioReservas.class);
		// test_HACER_RESERVA, test_GET_RESERVAS_ESPACIO, test_GET_RESERVAS_ESPACIO_ESTADO, test_GET_RESERVAS_USUARIO, test_GET_RESERVAS_USUARIO_ESTADO
		Mockito.when(repositorioReservas.findByEspacio(this.espacio)).thenReturn(listaReservas);
		Mockito.when(repositorioReservas.findByUsuario("Alex")).thenReturn(listaReservas);
		Mockito.when(repositorioReservas.findAll()).thenReturn(listaReservas);
		Mockito.when(repositorioReservas.save(this.reserva)).thenReturn(this.reserva);


		funcionesReserva = new FuncionesReservaImpl(repositorioReservas);
		
			
	}

	@Test
	@Ignore
	public void contexLoads() throws Exception {
		assertThat(funcionesReserva).isNotNull();
	}

	@Test
	@Ignore
	public void test_HACER_RESERVA() throws Exception {

		boolean ok = funcionesReserva.hacerReserva(reserva);
		assertTrue(ok);
	}

	@Test
	
	public void test_GET_RESERVAS_ESPACIO() throws Exception {

		List<ReservaDTO> reservas = funcionesReserva.buscarReserva(espacio);
		assertNotNull(reservas);
	}

	@Test
	
	public void test_GET_RESERVAS_ESPACIO_ESTADO() throws Exception {

		List<ReservaDTO> reservas = funcionesReserva.buscarReservaEstado(espacio, EstadoReserva.CANCELADA);
		assertNotNull(reservas);
	}

	@Test
	
	public void test_GET_RESERVAS_USUARIO() throws Exception {

		List<ReservaDTO> reservas = funcionesReserva.buscarReservaUsuario("Alex");
		assertNotNull(reservas);
	}

	@Test

	public void test_GET_RESERVAS_USUARIO_ESTADO() throws Exception {

		List<ReservaDTO> reservas = funcionesReserva.buscarReservaUsuarioEstado("Alex",EstadoReserva.CANCELADA);
		assertNotNull(reservas);
	}
	
}