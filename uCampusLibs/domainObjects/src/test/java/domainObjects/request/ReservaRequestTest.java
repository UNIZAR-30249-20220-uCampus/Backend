package domainObjects.request;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReservaRequestTest {
	@Test
	public void crearHorarioRequest() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNotNull(reservaRequest);
	}

	@Test
	public void getHorario() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNull(reservaRequest.getHorario());
	}

	@Test
	public void getUsuario() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNull(reservaRequest.getUsuario());
	}

	@Test
	public void getTipo() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNull(reservaRequest.getTipo());
	}

	@Test
	public void reservaRequest_toString() {
		ReservaRequest reservaRequest = new ReservaRequest();
		assertNotNull(reservaRequest.toString());
	}
}