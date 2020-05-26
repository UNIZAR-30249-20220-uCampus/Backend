package domainObjects.request;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HorarioRequestTest {
	@Test
	public void crearHorarioRequest() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNotNull(horarioRequest);
	}

	@Test
	public void getFechaInicio() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNull(horarioRequest.getFechaInicio());
	}

	@Test
	public void getFechaFin() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNull(horarioRequest.getFechaFin());
	}

	@Test
	public void getFrecuencia() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNotNull(horarioRequest.getFrecuencia());
	}

	@Test
	public void getConjuntoDiaSlots() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNull(horarioRequest.getConjuntoDiaSlots());
	}

	@Test
	public void horarioRequest_toString() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNotNull(horarioRequest.toString());
	}
}