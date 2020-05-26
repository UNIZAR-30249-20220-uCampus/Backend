package domainObjects.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import domainObjects.valueObject.Equipamiento;
import domainObjects.valueObject.TipoEquipamiento;

public class EspacioTest {

	Espacio espacio;

	@Before
    public void before() {
		Vector<Equipamiento> equipamientos = new Vector<Equipamiento>();
		Vector<Equipamiento> equipamientoMaximos = new Vector<Equipamiento>();

		//definirEquipamientosExistentes(equipamientos);
		equipamientos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 5));
		equipamientoMaximos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 10));
		equipamientoMaximos.add(new Equipamiento(TipoEquipamiento.IMPRESORAS, 10));


		espacio = new Espacio(equipamientos, equipamientoMaximos);
			
	}
	
	@Test
	public void crearEspacio() {
		Espacio espacio = new Espacio();
		assertNotNull(espacio);
	}

	@Test
	public void crearEspacioConEquipamientosMaximos() {
		Espacio espacio = new Espacio();
		assertNotNull(espacio);
		assertNotNull(espacio.getMaximoDeEquipamientos());
	}

	@Test
	public void getEquipamientos() {
		Vector<Equipamiento> equipamientos = new Vector<Equipamiento>();
		equipamientos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 5));
		assertNotSame(espacio.getEquipamientos(),equipamientos);
	}

	@Test
	public void agregarEquipamiento() {
		assertTrue(espacio.agregarEquipamiento(TipoEquipamiento.PIZARRA, 1));

	}

	@Test
	public void agregarEquipamientoNuevo() {
		assertTrue(espacio.agregarEquipamiento(TipoEquipamiento.IMPRESORAS, 2));
	}

	@Test
	public void agregarMaxEquipamiento() {
		assertFalse(espacio.agregarEquipamiento(TipoEquipamiento.PIZARRA, 10));

	}

	@Test
	public void agregarCeroEquipamiento() {
		assertTrue(espacio.agregarEquipamiento(TipoEquipamiento.IMPRESORAS, 0));
	}

	@Test
	public void no_agregarEquipamiento() {
		assertFalse(espacio.agregarEquipamiento(TipoEquipamiento.PIZARRA, -1));
	}

	@Test
	public void comprobarEquipamientoMinimoNecesario_OK() {
		Vector<Equipamiento> equipamientos = new Vector<Equipamiento>();
		equipamientos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 3));
		assertTrue(espacio.comprobarEquipamientoMinimoNecesario(equipamientos));
	}

	@Test
	public void comprobarEquipamientoMinimoNecesario_Error() {
		Vector<Equipamiento> equipamientos = new Vector<Equipamiento>();
		equipamientos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 20));
		assertFalse(espacio.comprobarEquipamientoMinimoNecesario(equipamientos));
	}

	@Test
	public void editarEquipamiento_OK() {
		assertTrue(espacio.editarEquipamiento(TipoEquipamiento.PIZARRA, 3));
	}

	@Test
	public void editarEquipamiento_Error() {
		assertFalse(espacio.editarEquipamiento(TipoEquipamiento.ORDENADORES, 3));
	}

	@Test
	public void eliminarEquipamiento_OK() {
		assertTrue(espacio.editarEquipamiento(TipoEquipamiento.PIZARRA, 5));
	}

	@Test
	public void eliminarEquipamiento_Error() {
		assertFalse(espacio.eliminarEquipamiento(TipoEquipamiento.PIZARRA, 3));
	}

	@Test
	public void calcularTarifa_OK() {
		assertEquals(espacio.calcularTarifa(), 0, 1);
	}

	@Test
	public void espacio_toString() {
		assertNotNull(espacio.toString());
	}

	@Test
	public void equipamiento_toString() {
		assertNotNull(espacio.equi());
	}

}