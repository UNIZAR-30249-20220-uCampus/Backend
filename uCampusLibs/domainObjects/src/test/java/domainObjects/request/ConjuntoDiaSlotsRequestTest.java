package domainObjects.request;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConjuntoDiaSlotsRequestTest {
	/* Se verifica que ConjuntoDiaSlots se construye. */
	@Test
	public void crearConjuntoDiaSlotsRequest() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest);
	}

	/* Se verifica que ConjuntoDiaSlots.getDiaSemana() no devuelve null. */
	@Test
	public void getDiaSemana() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest.getDiaSemana());
	}

	/* Se verifica que ConjuntoDiaSlots.getSlotInicio() no devuelve null. */
	@Test
	public void getSlotInicio() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest.getSlotInicio());
	}

	/* Se verifica que ConjuntoDiaSlots.getSlotFinal() no devuelve null. */
	@Test
	public void getSlotFinal() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest.getSlotFinal());
	}

	/* Se verifica que toString no devuelve null */
	@Test
	public void conjuntoDiaSlotsRequest_toString() {
		ConjuntoDiaSlotsRequest conjuntoDiaSlotsRequest = new ConjuntoDiaSlotsRequest();
		assertNotNull(conjuntoDiaSlotsRequest.toString());
	}
}