package domainObjects.valueObject;

import javax.persistence.Embeddable;

/* Tupla que representa la cantidad de un determinado objeto dentro de un Espacio */
@Embeddable
public class Equipamiento {

	private TipoEquipamiento tipo;
	private int cantidad;

	public Equipamiento() {
	}

	public Equipamiento(TipoEquipamiento tipo, int cantidad) {
		this.tipo = tipo;
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "Equipamiento [cantidad=" + cantidad + ", tipo=" + tipo + "]";
	}

	public TipoEquipamiento getTipo() {
		return this.tipo;
	}

	public int getCantidad() {
		return this.cantidad;
	}

}
