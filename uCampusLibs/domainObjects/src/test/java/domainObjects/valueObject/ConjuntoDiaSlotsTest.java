package domainObjects.valueObject;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConjuntoDiaSlotsTest {

	@Test
	public void constructorConjuntoDiaSlotsT0() {
		assertNotNull(new ConjuntoDiaSlots(1, 2, 5));
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT1() {
		new ConjuntoDiaSlots(0, 2, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT2() {
		new ConjuntoDiaSlots(8, 2, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT3() {
		new ConjuntoDiaSlots(1, 10, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT4() {
		new ConjuntoDiaSlots(1, 0, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT5() {
		new ConjuntoDiaSlots(1, 1, 50);
	}
}