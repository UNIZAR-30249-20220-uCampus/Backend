package domainObjects.request;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReservaRequestTest {
	/* Se verifica que ReservaRequest se construye. */
	@Test
	public void crearReservaRequest() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNotNull(reservaRequest);
	}

	/* Se verifica que ReservaRequest no tiene un Horario por defecto. */
	@Test
	public void getHorario() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNull(reservaRequest.getHorario());
	}

	/* Se verifica que ReservaRequest no tiene un usuario por defecto. */
	@Test
	public void getUsuario() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNull(reservaRequest.getUsuario());
	}

	/* Se verifica que ReservaRequest no tiene un tipo por defecto. */
	@Test
	public void getTipo() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNull(reservaRequest.getTipo());
	}

	/* Se verifica que toString no devuelve null. */
	@Test
	public void reservaRequest_toString() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNotNull(reservaRequest.toString());
	}
}