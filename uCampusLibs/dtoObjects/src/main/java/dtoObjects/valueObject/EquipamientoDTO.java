package dtoObjects.valueObject;

import domainObjects.valueObject.Equipamiento;
import domainObjects.valueObject.TipoEquipamiento;

public class EquipamientoDTO {

	private TipoEquipamiento tipo;
	private int cantidad;

	public EquipamientoDTO() {}

	public EquipamientoDTO(Equipamiento equipamiento) {
		this.tipo = equipamiento.getTipo();
		this.cantidad = equipamiento.getCantidad();
	}

	public TipoEquipamiento getTipo(){
		return this.tipo;
	}

	public int getCantidad(){
		return this.cantidad;
	}

	@Override
	public String toString() {
		return "EquipamientoDTO [cantidad=" + cantidad + ", tipo=" + tipo + "]";
	}

}