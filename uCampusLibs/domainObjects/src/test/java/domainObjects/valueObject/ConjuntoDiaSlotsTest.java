package domainObjects.valueObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConjuntoDiaSlotsTest {

	/* Se verifica que ConjuntoDiaSlots se construye. */
	@Test
	public void constructorConjuntoDiaSlotsT0() {
		assertNotNull(new ConjuntoDiaSlots(1, 2, 5));
	}

	/*
	 * Se verifica que ConjuntoDiaSlots no se construye por inconsistencia en sus
	 * campos.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT1() {
		new ConjuntoDiaSlots(0, 2, 5);
	}

	/*
	 * Se verifica que ConjuntoDiaSlots no se construye por inconsistencia en sus
	 * campos.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT2() {
		new ConjuntoDiaSlots(8, 2, 5);
	}

	/*
	 * Se verifica que ConjuntoDiaSlots no se construye por inconsistencia en sus
	 * campos.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT3() {
		new ConjuntoDiaSlots(1, 10, 5);
	}

	/*
	 * Se verifica que ConjuntoDiaSlots no se construye por inconsistencia en sus
	 * campos.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT4() {
		new ConjuntoDiaSlots(1, 0, 5);
	}

	/*
	 * Se verifica que ConjuntoDiaSlots no se construye por inconsistencia en sus
	 * campos.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructorConjuntoDiaSlotsT5() {
		new ConjuntoDiaSlots(1, 1, 50);
	}

	/*
	 * Se verifica que "conflictoCon" devuelve FALSE dados dos ConjuntoDiaSlots que
	 * no tienen conflicto.
	 */
	@Test
	public void conflictoT0() {
		ConjuntoDiaSlots c1 = new ConjuntoDiaSlots(1, 10, 15);
		ConjuntoDiaSlots c2 = new ConjuntoDiaSlots(3, 16, 17);
		assertEquals(false, c1.conflictoCon(c2));
	}

	/*
	 * Se verifica que "conflictoCon" devuelve TRUE dados dos ConjuntoDiaSlots que
	 * tienen conflicto.
	 */
	@Test
	public void conflictoT1() {
		ConjuntoDiaSlots c1 = new ConjuntoDiaSlots(1, 10, 15);
		ConjuntoDiaSlots c2 = new ConjuntoDiaSlots(1, 15, 17);
		assertEquals(true, c1.conflictoCon(c2));
	}

	/*
	 * Se verifica que "conflictoCon" devuelve TRUE dados dos ConjuntoDiaSlots que
	 * tienen conflicto.
	 */
	@Test
	public void conflictoT2() {
		ConjuntoDiaSlots c1 = new ConjuntoDiaSlots(1, 10, 15);
		ConjuntoDiaSlots c2 = new ConjuntoDiaSlots(1, 4, 12);
		assertEquals(true, c1.conflictoCon(c2));
	}
}