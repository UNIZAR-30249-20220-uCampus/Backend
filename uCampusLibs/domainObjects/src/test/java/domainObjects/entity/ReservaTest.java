package domainObjects.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import domainObjects.request.HorarioRequest;
import domainObjects.valueObject.ConjuntoDiaSlots;
import domainObjects.valueObject.EstadoReserva;
import domainObjects.valueObject.Horario;

public class ReservaTest {

	Reserva reserva;
	Reserva alquiler;

	@Before
    public void before() {
		Espacio espacio = new Espacio();
		
		Date fechaInicio = new Date();
		Date fechaFin = new Date();
		ConjuntoDiaSlots slot = new ConjuntoDiaSlots(1, 2, 3);
		List<ConjuntoDiaSlots> conjuntoDiaSlots = new ArrayList<ConjuntoDiaSlots>();
		conjuntoDiaSlots.add(slot);
		Horario horario = new Horario(fechaInicio, fechaFin, 1, conjuntoDiaSlots);

		reserva = new Reserva(espacio, horario, "Pepe", "reserva");
		alquiler = new Reserva(espacio, horario, "Pepe", "alquiler");
    }

	@Test
	public void crearReserva() {
		Reserva reserva = new Reserva();
		assertNotNull(reserva);
	}

	@Test
	@Ignore
	public void crearReserva_Request() {
		Espacio espacio = new Espacio();
		HorarioRequest horarioRequest = new HorarioRequest();
		Reserva reserva = new Reserva(espacio, horarioRequest, "Pepe", "reserva");
		assertNull(reserva);
	}

	@Test
	public void crearReserva_Parameters() {
		Espacio espacio = new Espacio();
		Horario horario = new Horario();
		Reserva reserva = new Reserva(espacio, horario, "Pepe", "reserva");
		assertNotNull(reserva);
	}


	@Test
	public void getEspacio() {
		assertNotNull(reserva.getEspacio());
	}

	@Test
	public void getHorario() {
		assertNotNull(reserva.getHorario());
	}

	@Test
	public void getUsuario() {
		assertEquals(reserva.getUsuario(),"Pepe");
	}

	@Test
	public void getEstado() {
		assertEquals(reserva.getEstado(),EstadoReserva.PENDIENTE);
	}

	@Test
	public void getTipo() {
		assertEquals(reserva.getTipo(),"reserva");
	}

	@Test
	public void aceptarReserva() {
		assertTrue(reserva.aceptarReserva());
	}

	@Test
	public void aceptarAlquiler() {
		assertTrue(alquiler.aceptarReserva());

		assertFalse(alquiler.aceptarReserva());
	}

	@Test
	public void cancelarReserva() {
		assertTrue(reserva.cancelarReserva());
	}

	@Test
	public void pagarReserva() {
		assertFalse(alquiler.pagarReserva());
		alquiler.aceptarReserva();
		assertTrue(alquiler.pagarReserva());
	}

	@Test
	public void hayColision() {
		alquiler.aceptarReserva();
		assertTrue(reserva.hayColision(alquiler));
	}

	@Test
	public void nohayColision() {
		assertFalse(reserva.hayColision(alquiler));
	}

	@Test
	public void reserva_toString() {
		Reserva reserva = new Reserva();
		assertNotNull(reserva.toString());
	}
	
}