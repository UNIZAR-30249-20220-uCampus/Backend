package domainObjects.request;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HorarioRequestTest {
	/* Se verifica que HorarioRequest se construye. */
	@Test
	public void crearHorarioRequest() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNotNull(horarioRequest);
	}

	/* Se verifica que HorarioRequest no tiene fechaInicio por defecto. */
	@Test
	public void getFechaInicio() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNull(horarioRequest.getFechaInicio());
	}

	/* Se verifica que HorarioRequest no tiene fechaFin por defecto. */
	@Test
	public void getFechaFin() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNull(horarioRequest.getFechaFin());
	}

	/* Se verifica que HorarioRequest no tiene frecuencia por defecto. */
	@Test
	public void getFrecuencia() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNotNull(horarioRequest.getFrecuencia());
	}

	/* Se verifica que HorarioRequest no tiene ConjuntoDiaSlots por defecto. */
	@Test
	public void getConjuntoDiaSlots() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNull(horarioRequest.getConjuntoDiaSlots());
	}

	/* Se verifica que la función toString no devuelve null */
	@Test
	public void horarioRequest_toString() {
		HorarioRequest horarioRequest = new HorarioRequest();
		assertNotNull(horarioRequest.toString());
	}
}