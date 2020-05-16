package es.ucampus.demo.dto;


public class ConjuntoDiaSlots {
	private int diaSemana;
	private int slotInicio;
	private int slotFinal;

	public ConjuntoDiaSlots() {}

	public ConjuntoDiaSlots(int diaSemana, int slotInicio, int slotFinal) {
		this.diaSemana = diaSemana;
		this.slotInicio = slotInicio;
		this.slotFinal = slotFinal;
	}

	public int getDiaSemana() {
		return diaSemana;
	}

	public int getSlotInicio() {
		return slotInicio;
	}

	public int getSlotFinal() {
		return slotFinal;
	}
}