package domainObjects.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import domainObjects.valueObject.Equipamiento;
import domainObjects.valueObject.TipoEquipamiento;

public class EspacioTest {

	Espacio espacio;

	/*
	 * Se define un Espacio con equipamientos y equipamientos máximos permitidos
	 * para los posteriores test.
	 */
	@Before
	public void before() {
		Vector<Equipamiento> equipamientos = new Vector<Equipamiento>();
		Vector<Equipamiento> equipamientoMaximos = new Vector<Equipamiento>();

		equipamientos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 5));
		equipamientos.add(new Equipamiento(TipoEquipamiento.CANYON_FIJO, 1));
		equipamientoMaximos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 10));
		equipamientoMaximos.add(new Equipamiento(TipoEquipamiento.IMPRESORAS, 10));
		equipamientoMaximos.add(new Equipamiento(TipoEquipamiento.CANYON_FIJO, 2));

		espacio = new Espacio(equipamientos, equipamientoMaximos);
	}

	/* Se verifica que un Espacio se construye. */
	@Test
	public void crearEspacio() {
		Espacio espacio = new Espacio();
		assertNotNull(espacio);
	}

	/* Se verifica que un Espacio tiene por defecto un máximo de equipamientos. */
	@Test
	public void crearEspacioConEquipamientosMaximos() {
		Espacio espacio = new Espacio();
		assertNotNull(espacio);
		assertNotNull(espacio.getMaximoDeEquipamientos());
	}

	/* Se verifica que un Espacio tiene equipamientos. */
	@Test
	public void getEquipamientos() {
		assertNotNull(espacio.getEquipamientos());
	}

	/*
	 * Se verifica que a un Espacio se le puede agregar un Equipamiento, que ya
	 * tiene, dentro de sus máximos permitidos.
	 */
	@Test
	public void agregarEquipamiento() {
		assertTrue(espacio.agregarEquipamiento(TipoEquipamiento.PIZARRA, 1));
	}

	/*
	 * Se verifica que a un Espacio se le puede agregar un Equipamiento, que no
	 * tiene previamente, dentro de sus máximos permitidos.
	 */
	@Test
	public void agregarEquipamientoNuevo() {
		assertTrue(espacio.agregarEquipamiento(TipoEquipamiento.IMPRESORAS, 2));
	}

	/*
	 * Se verifica que a un Espacio no se le puede agregar un Equipamiento por
	 * encima de sus máximos permitidos.
	 */
	@Test
	public void agregarMaxEquipamiento() {
		assertFalse(espacio.agregarEquipamiento(TipoEquipamiento.PIZARRA, 10));

	}

	/*
	 * Se verifica que a un Espacio se le puede agregar un Equipamiento con cantidad
	 * 0.
	 */
	@Test
	public void agregarCeroEquipamiento() {
		assertTrue(espacio.agregarEquipamiento(TipoEquipamiento.IMPRESORAS, 0));
	}

	/*
	 * Se verifica que a un Espacio no se le puede agregar un Equipamiento con
	 * cantidad negativa.
	 */
	@Test
	public void no_agregarEquipamiento() {
		assertFalse(espacio.agregarEquipamiento(TipoEquipamiento.PIZARRA, -1));
	}

	/*
	 * Se verifica que "comprobarEquipamientoMinimoNecesario" devuelve TRUE si el
	 * Espacio cumple con los requisitos.
	 */
	@Test
	public void comprobarEquipamientoMinimoNecesario_OK() {
		Vector<Equipamiento> equipamientos = new Vector<Equipamiento>();
		equipamientos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 3));
		assertTrue(espacio.comprobarEquipamientoMinimoNecesario(equipamientos));
	}

	/*
	 * Se verifica que "comprobarEquipamientoMinimoNecesario" devuelve FALSE si el
	 * Espacio no cumple con los requisitos.
	 */
	@Test
	public void comprobarEquipamientoMinimoNecesario_Error() {
		Vector<Equipamiento> equipamientos = new Vector<Equipamiento>();
		equipamientos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 3));
		equipamientos.add(new Equipamiento(TipoEquipamiento.CANYON_FIJO, 2));
		assertFalse(espacio.comprobarEquipamientoMinimoNecesario(equipamientos));
	}

	/*
	 * Se verifica que "comprobarEquipamientoMinimoNecesario" devuelve FALSE si el
	 * Espacio no cumple con los requisitos.
	 */
	@Test
	public void comprobarEquipamientoMinimoNecesario_Error2() {
		Vector<Equipamiento> equipamientos = new Vector<Equipamiento>();
		equipamientos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 20));
		assertFalse(espacio.comprobarEquipamientoMinimoNecesario(equipamientos));
	}

	/*
	 * Se verifica que a un Espacio se le puede editar un Equipamiento, que ya
	 * tiene, dentro de sus máximos permitidos.
	 */
	@Test
	public void editarEquipamiento_OK() {
		assertTrue(espacio.editarEquipamiento(TipoEquipamiento.PIZARRA, 3));
	}

	/*
	 * Se verifica que a un Espacio no se le puede editar un Equipamiento que no
	 * tiene.
	 */
	@Test
	public void editarEquipamiento_Error() {
		assertFalse(espacio.editarEquipamiento(TipoEquipamiento.ORDENADORES, 3));
	}

	/*
	 * Se verifica que a un Espacio se le puede eliminar un Equipamiento que ya
	 * tiene.
	 */
	@Test
	public void eliminarEquipamiento_OK() {
		assertTrue(espacio.eliminarEquipamiento(TipoEquipamiento.PIZARRA, 5));
	}

	/*
	 * Se verifica que a un Espacio no se le puede eliminar un Equipamiento que no
	 * tiene.
	 */
	@Test
	public void eliminarEquipamiento_Error() {
		assertFalse(espacio.eliminarEquipamiento(TipoEquipamiento.PIZARRA, 3));
	}

	/* Se verifica que la función calcularTarifa no devuelve null */
	@Test
	public void calcularTarifa_OK() {
		assertEquals(espacio.calcularTarifa(), 0, 1);
	}

	/* Se verifica que la función toString de Espacio no devuelve null */
	@Test
	public void espacio_toString() {
		assertNotNull(espacio.toString());
	}

	/* Se verifica que la función toString de Equipamiento no devuelve null */
	@Test
	public void equipamiento_toString() {
		assertNotNull(espacio.equi());
	}

}