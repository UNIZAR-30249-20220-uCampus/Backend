package es.ucampus.demo.dto;


public class EquipamientoDTO {

	private TipoEquipamiento tipo;
	private int cantidad;

	public EquipamientoDTO() {}

	public EquipamientoDTO(TipoEquipamiento tipo, int cantidad) {
		this.tipo = tipo;
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "Equipamiento [cantidad=" + cantidad + ", tipo=" + tipo + "]";
	}

	public TipoEquipamiento getTipo(){
		return this.tipo;
	}

	public int getCantidad(){
		return this.cantidad;
	}

}