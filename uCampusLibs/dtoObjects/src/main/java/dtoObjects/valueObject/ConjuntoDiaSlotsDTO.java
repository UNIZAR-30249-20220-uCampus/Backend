package dtoObjects.valueObject;

import domainObjects.valueObject.ConjuntoDiaSlots;

public class ConjuntoDiaSlotsDTO {
	private int diaSemana;
	private int slotInicio;
	private int slotFinal;

	public ConjuntoDiaSlotsDTO() {}

	public ConjuntoDiaSlotsDTO(ConjuntoDiaSlots conjuntoDiaSlots) {
		this.diaSemana = conjuntoDiaSlots.getDiaSemana();
		this.slotInicio = conjuntoDiaSlots.getSlotInicio();
		this.slotFinal = conjuntoDiaSlots.getSlotFinal();
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