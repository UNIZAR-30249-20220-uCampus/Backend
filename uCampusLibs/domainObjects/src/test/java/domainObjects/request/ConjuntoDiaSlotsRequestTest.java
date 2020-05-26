package domainObjects.request;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConjuntoDiaSlotsRequestTest {
	@Test
	public void crearConjuntoDiaSlotsRequest() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest);
	}

	@Test
	public void getDiaSemana() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest.getDiaSemana());
	}

	@Test
	public void getSlotInicio() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest.getSlotInicio());
	}

	@Test
	public void getSlotFinal() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest.getSlotFinal());
	}

	@Test
	public void conjuntoDiaSlotsRequest_toString() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest.toString());
	}
}