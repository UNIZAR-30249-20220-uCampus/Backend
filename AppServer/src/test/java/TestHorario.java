import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import domainObjects.valueObject.ConjuntoDiaSlots;
import domainObjects.valueObject.Horario;

class HorarioTests {

	ConjuntoDiaSlots conjunto1 = new ConjuntoDiaSlots(1, 10, 14);
	ConjuntoDiaSlots conjunto2 = new ConjuntoDiaSlots(2, 10, 14);
	ConjuntoDiaSlots conjunto3 = new ConjuntoDiaSlots(3, 15, 19);
	ConjuntoDiaSlots conjunto4 = new ConjuntoDiaSlots(4, 15, 19);

	ConjuntoDiaSlots slots1 = new ConjuntoDiaSlots(1, 15, 19);
	ConjuntoDiaSlots slots2 = new ConjuntoDiaSlots(1, 8, 10);
	ConjuntoDiaSlots slots3 = new ConjuntoDiaSlots(2, 15, 19);
	ConjuntoDiaSlots slots4 = new ConjuntoDiaSlots(2, 8, 10);
	ConjuntoDiaSlots slots5 = new ConjuntoDiaSlots(1, 18, 21);
	ConjuntoDiaSlots slots6 = new ConjuntoDiaSlots(3, 15, 19);

	@Test
	void coincidenFechasTest1() {
		Date d1 = new Date(1000L);
		Date d2 = new Date(2000L);
		Date d3 = new Date(3000L);
		Date d4 = new Date(4000L);

		Horario h1 = new Horario(d1, d3, 1);
		Horario h2 = new Horario(d2, d4, 1);

		assertEquals(true, h1.coincidenFechas(h2));
	}

	@Test
	void coincidenFechasTest2() {
		Date d10 = new Date(10000L);
		Date d20 = new Date(20000L);
		Date d30 = new Date(30000L);
		Date d40 = new Date(40000L);

		Horario h1 = new Horario(d20, d40, 1);
		Horario h2 = new Horario(d10, d30, 1);

		assertEquals(true, h1.coincidenFechas(h2));
	}

	@Test
	void coincidenFechasTest4() {
		Date d10 = new Date(10000L);
		Date d20 = new Date(20000L);
		Date d30 = new Date(30000L);
		Date d40 = new Date(40000L);

		Horario h1 = new Horario(d10, d40, 1);
		Horario h2 = new Horario(d20, d30, 1);

		assertEquals(true, h1.coincidenFechas(h2));
	}

	@Test
	void coincidenFechasTest5() {
		Date d10 = new Date(10000L);
		Date d20 = new Date(20000L);
		Date d30 = new Date(30000L);
		Date d40 = new Date(40000L);

		Horario h1 = new Horario(d20, d30, 1);
		Horario h2 = new Horario(d10, d40, 1);

		assertEquals(true, h1.coincidenFechas(h2));
	}

	@Test
	void coincidenFechasTest6() {
		Date d10 = new Date(10000L);
		Date d20 = new Date(20000L);
		Date d30 = new Date(30000L);
		Date d40 = new Date(40000L);

		Horario h1 = new Horario(d10, d20, 1);
		Horario h2 = new Horario(d30, d40, 1);

		assertEquals(false, h1.coincidenFechas(h2));
	}

	@Test
	void coincidenFechasTest7() {
		Date d10 = new Date(10000L);
		Date d20 = new Date(20000L);
		Date d30 = new Date(30000L);
		Date d40 = new Date(40000L);

		Horario h1 = new Horario(d30, d40, 1);
		Horario h2 = new Horario(d10, d20, 1);

		assertEquals(false, h1.coincidenFechas(h2));
	}

	@Test
	void coincidenFechasTest8() {
		Date d10 = new Date(10000L);
		Date d20 = new Date(20000L);
		Date d30 = new Date(30000L);
		Date d40 = new Date(40000L);

		Horario h1 = new Horario(d30, d40, 1);
		Horario h2 = new Horario(d10, d30, 1);

		assertEquals(true, h1.coincidenFechas(h2));
	}

	@Test
	void coincidenSemanasTest1() throws ParseException {
		String sDate1 = "06/04/2020";
		Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		String sDate2 = "06/05/2020";
		Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
		String sDate3 = "13/04/2020";
		Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate3);
		String sDate4 = "13/05/2020";
		Date date4 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate4);

		Horario h1 = new Horario(date1, date2, 2);
		Horario h2 = new Horario(date3, date4, 2);

		assertEquals(false, h1.coincidenSemanas(h2));
	}

	@Test
	void coincidenSemanasTest2() throws ParseException {
		String sDate1 = "06/04/2020";
		Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		String sDate2 = "06/05/2020";
		Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
		String sDate3 = "13/04/2020";
		Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate3);
		String sDate4 = "13/05/2020";
		Date date4 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate4);

		Horario h1 = new Horario(date1, date2, 1);
		Horario h2 = new Horario(date3, date4, 1);

		assertEquals(true, h1.coincidenSemanas(h2));
	}

	@Test
	void coincidenSemanasTest3() throws ParseException {
		String sDate1 = "06/04/2020";
		Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		String sDate2 = "06/06/2020";
		Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
		String sDate3 = "13/04/2020";
		Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate3);
		String sDate4 = "13/06/2020";
		Date date4 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate4);

		Horario h1 = new Horario(date1, date2, 2);
		Horario h2 = new Horario(date3, date4, 3);

		assertEquals(true, h1.coincidenSemanas(h2));
	}

	@Test
	void coincidenSemanasTest4() throws ParseException {
		String sDate1 = "01/01/2020";
		Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
		String sDate2 = "31/12/2020";
		Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
		String sDate3 = "21/04/2020";
		Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate3);
		String sDate4 = "25/04/2020";
		Date date4 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate4);

		Horario h1 = new Horario(date1, date2, 1);
		Horario h2 = new Horario(date3, date4, 1);

		assertEquals(true, h1.coincidenSemanas(h2));
	}

	@Test
	void coincidenDiasTest1() throws ParseException {
		Vector<ConjuntoDiaSlots> lunesMartes = new Vector<ConjuntoDiaSlots>();
		lunesMartes.add(conjunto1);
		lunesMartes.add(conjunto2);
		Vector<ConjuntoDiaSlots> miercolesJueves = new Vector<ConjuntoDiaSlots>();
		miercolesJueves.add(conjunto3);
		miercolesJueves.add(conjunto4);
		Horario h1 = new Horario(lunesMartes);
		Horario h2 = new Horario(miercolesJueves);

		assertEquals(false, h1.coincidenDias(h2));
	}

	@Test
	void coincidenDiasTest2() throws ParseException {
		Vector<ConjuntoDiaSlots> lunesMartes = new Vector<ConjuntoDiaSlots>();
		lunesMartes.add(conjunto1);
		lunesMartes.add(conjunto2);
		Vector<ConjuntoDiaSlots> lunesJueves = new Vector<ConjuntoDiaSlots>();
		lunesJueves.add(conjunto1);
		lunesJueves.add(conjunto4);
		Horario h1 = new Horario(lunesMartes);
		Horario h2 = new Horario(lunesJueves);

		assertEquals(true, h1.coincidenDias(h2));
	}

	@Test
	void findUnionTest1() {
		Horario h = new Horario(null, null, 0);
		Vector<Integer> v1 = new Vector<>();
		Vector<Integer> v2 = new Vector<>();
		v1.add(1);
		v1.add(3);
		v1.add(3);
		v1.add(3);
		v2.add(2);
		v2.add(2);
		v2.add(3);
		v2.add(3);
		Vector<Integer> resultado = new Vector<>();
		resultado.add(3);
		assertEquals(resultado, h.findUnion(v1, v2));
	}

	@Test
	void findUnionTest2() {
		Horario h = new Horario(null, null, 0);
		Vector<Integer> v1 = new Vector<>();
		Vector<Integer> v2 = new Vector<>();
		v1.add(1);
		v1.add(3);
		v1.add(3);
		v1.add(3);
		v2.add(2);
		v2.add(2);
		v2.add(2);
		v2.add(2);
		Vector<Integer> resultado = new Vector<>();
		// resultado.add(3);
		assertEquals(resultado, h.findUnion(v1, v2));
	}

	@Test
	void coincidenSlotsTest1() throws ParseException {
		Vector<ConjuntoDiaSlots> lunes = new Vector<ConjuntoDiaSlots>();
		lunes.add(slots1);
		lunes.add(slots2);
		Vector<ConjuntoDiaSlots> martes = new Vector<ConjuntoDiaSlots>();
		martes.add(slots3);
		martes.add(slots4);
		Horario h1 = new Horario(lunes);
		Horario h2 = new Horario(martes);

		assertEquals(false, h1.coincidenSlots(h2));
	}

	@Test
	void coincidenSlotsTest2() throws ParseException {
		Vector<ConjuntoDiaSlots> lunes = new Vector<ConjuntoDiaSlots>();
		lunes.add(slots1);
		lunes.add(slots2);
		Vector<ConjuntoDiaSlots> martes = new Vector<ConjuntoDiaSlots>();
		martes.add(slots5);
		martes.add(slots6);
		Horario h1 = new Horario(lunes);
		Horario h2 = new Horario(martes);

		assertEquals(true, h1.coincidenSlots(h2));
	}

}