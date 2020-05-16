import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Vector;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EspacioTests {

	/*
	private final Vector<Equipamiento> maximoDeEquipamientos = new Vector<Equipamiento>();

	private static final int maxMesa = 20;
	private static final int maxPizarra = 4;
	private static final int maxSilla = 100;
	private static final int maxProyector = 1;
	
	@BeforeEach
	void definicionDeEquipamientosMaximos(){
		System.out.println(12345678);
		Equipamiento eq = new Equipamiento(TipoEquipamiento.MESA, maxMesa);
		maximoDeEquipamientos.addElement(eq);
		eq = new Equipamiento(TipoEquipamiento.SILLA, maxSilla);
		maximoDeEquipamientos.addElement(eq);
		eq = new Equipamiento(TipoEquipamiento.PIZARRA, maxPizarra);
		maximoDeEquipamientos.addElement(eq);
		eq = new Equipamiento(TipoEquipamiento.PROYECTOR, maxProyector);
		maximoDeEquipamientos.addElement(eq);
	}
	
	/*
		Se comprueba que se agrega un tipo de equipamiento que antes no había
	*/
	/*
	@Test
    void agregarEquipamientoTest1(){
		final Espacio espacio = new Espacio("A.01", null, 0, 0, null, maximoDeEquipamientos);
		final TipoEquipamiento tipo = TipoEquipamiento.PROYECTOR;
		final int cantidad = 1;
		boolean boolTipoCantidad = true;

		espacio.agregarEquipamiento(tipo, cantidad);

		if(espacio.getEquipamientos().get(0).getTipo() != TipoEquipamiento.PROYECTOR || 
			espacio.getEquipamientos().get(0).getCantidad() != 1){
			boolTipoCantidad = false;
		}

		assertEquals(true, boolTipoCantidad, "agregarEquipamientoTest1");
	}
	
	/*
		Se comprueba que se agrega un tipo de equipamiento que antes sí había
	*/
	/*
	@Test
	void agregarEquipamientoTest2(){
		final Espacio espacio = new Espacio("A.01", null, 0, 0, null, maximoDeEquipamientos);
		final TipoEquipamiento tipo = TipoEquipamiento.MESA;
		final int cantidad = 1;
		boolean boolTipoCantidad = true;

		espacio.agregarEquipamiento(tipo, cantidad);
		espacio.agregarEquipamiento(tipo, cantidad);

		if(espacio.getEquipamientos().get(0).getTipo() != TipoEquipamiento.MESA || 
			espacio.getEquipamientos().get(0).getCantidad() != 2){
			boolTipoCantidad = false;
		}

		assertEquals(true, boolTipoCantidad, "agregarEquipamientoTest2");
	}

	/*
		Se comprueba que no se puede agregar un equipamiento que no estaba en el espacio si supera su máximo
	*/
	/*
	@Test
	void agregarEquipamientoTest3(){
		final Espacio espacio = new Espacio("A.01", null, 0, 0, null, maximoDeEquipamientos);
		final TipoEquipamiento tipo = TipoEquipamiento.SILLA;
		final int cantidad = 99999;

		assertEquals(false, espacio.agregarEquipamiento(tipo, cantidad), "agregarEquipamientoTest3");
	}

	/*
		Se comprueba que no se puede agregar un equipamiento que sí estaba en el espacio si la suma de cantidades supera su máximo
	*/
	/*
	@Test
	void agregarEquipamientoTest4(){
		final Espacio espacio = new Espacio("A.01", null, 0, 0, null, maximoDeEquipamientos);
		final TipoEquipamiento tipo = TipoEquipamiento.SILLA;
		final int cantidad = 80;

		espacio.agregarEquipamiento(tipo, cantidad);

		assertEquals(false, espacio.agregarEquipamiento(tipo, cantidad), "agregarEquipamientoTest4");
	}

	//FALTA DECIDIR QUÉ HACE REALMENTE LA FUNCIÓN DE ELIMINAR EQUIPAMIENTO
	/*
		Se comprueba que dado un equipamiento existente en un espacio, se elimina correctamente
	*/
	/* @Test
	void eliminarEquipamientoTest1(){
		final Espacio espacio = new Espacio("A.01", null, 0, 0, null, maximoDeEquipamientos);
		espacio.agregarEquipamiento(TipoEquipamiento.SILLA, 80);
		assertEquals(true, espacio.eliminarEquipamiento(TipoEquipamiento.SILLA, 80), "eliminarEquipamientoTest1");
	} */

	/*
		Se comprueba que dado un equipamiento no existente en un espacio, la función no elimina
	*/
	/* @Test
	void eliminarEquipamientoTest2(){
		final Espacio espacio = new Espacio("A.01", null, 0, 0, null, maximoDeEquipamientos);
		assertEquals(false, espacio.eliminarEquipamiento(TipoEquipamiento.SILLA, 20), "eliminarEquipamientoTest2");
	} */

}