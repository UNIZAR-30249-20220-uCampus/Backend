package domainObjects.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import domainObjects.valueObject.ConjuntoDiaSlots;
import domainObjects.valueObject.EstadoReserva;
import domainObjects.valueObject.Horario;

public class ReservaTest {

	Reserva reserva;
	Reserva alquiler;

	/*
	 * Se define una Reserva para los posteriores test.
	 */
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

	/* Se verifica que una Reserva se construye. */
	@Test
	public void crearReserva() {
		Reserva reserva = new Reserva();
		assertNotNull(reserva);
	}

	/* Se verifica que una Reserva se construye. */
	@Test
	public void crearReserva_Parameters() {
		Espacio espacio = new Espacio();
		Horario horario = new Horario();
		Reserva reserva = new Reserva(espacio, horario, "Pepe", "reserva");
		assertNotNull(reserva);
	}

	/* Se verifica que una Reserva tiene un Espacio. */
	@Test
	public void getEspacio() {
		assertNotNull(reserva.getEspacio());
	}

	/* Se verifica que una Reserva tiene un Horario. */
	@Test
	public void getHorario() {
		assertNotNull(reserva.getHorario());
	}

	/* Se verifica que una Reserva tiene un Usuario. */
	@Test
	public void getUsuario() {
		assertEquals(reserva.getUsuario(), "Pepe");
	}

	/* Se verifica que una Reserva tiene estado PENDIENTE por defecto. */
	@Test
	public void getEstado() {
		assertEquals(reserva.getEstado(), EstadoReserva.PENDIENTE);
	}

	/* Se verifica que una Reserva tiene tipo de reserva. */
	@Test
	public void getTipo() {
		assertEquals(reserva.getTipo(), "reserva");
	}

	/* Se verifica que una reserva PENDIENTE se puede aceptar. */
	@Test
	public void aceptarReserva() {
		assertTrue(reserva.aceptarReserva());
	}

	/*
	 * Se verifica que un alquiler PENDIENTE se puede aceptar una vez, pero no dos
	 * porque el estado es PENDIENTEPAGO.
	 */
	@Test
	public void aceptarAlquiler() {
		assertTrue(alquiler.aceptarReserva());

		assertFalse(alquiler.aceptarReserva());
	}

	/* Se verifica que una reserva se puede cancelar. */
	@Test
	public void cancelarReserva() {
		assertTrue(reserva.cancelarReserva());
	}

	/*
	 * Se verifica que un alquiler PENDIENTE no se puede pagar, pero una vez en
	 * estado PENDIENTEPAGO sí.
	 */
	@Test
	public void pagarReserva() {
		assertFalse(alquiler.pagarReserva());
		alquiler.aceptarReserva();
		assertTrue(alquiler.pagarReserva());
	}

	/*
	 * Se verifica que la función hayColisión devuelve TRUE si entre dos Reserva hay
	 * colisión.
	 */
	@Test
	public void hayColision() {
		alquiler.aceptarReserva();
		assertTrue(reserva.hayColision(alquiler));
	}

	/*
	 * Se verifica que la función hayColisión devuelve FALSE si entre dos Reserva no
	 * hay colisión porque alguna de ellas no ha sido aceptada.
	 */
	@Test
	public void nohayColision() {
		assertFalse(reserva.hayColision(alquiler));
	}

	/* Se verifica que la función toString de Reserva no devuelve null */
	@Test
	public void reserva_toString() {
		Reserva reserva = new Reserva();
		assertNotNull(reserva.toString());
	}

}