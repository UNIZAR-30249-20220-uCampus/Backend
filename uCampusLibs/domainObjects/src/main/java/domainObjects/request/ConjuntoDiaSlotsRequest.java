package domainObjects.request;

public class ConjuntoDiaSlotsRequest {

	private int diaSemana;
	private int slotInicio;
	private int slotFinal;

	public ConjuntoDiaSlotsRequest() {}

	public int getDiaSemana() {
		return diaSemana;
	}

	public int getSlotInicio() {
		return slotInicio;
	}

	public int getSlotFinal() {
		return slotFinal;
	}

	@Override
	public String toString() {
		return "ConjuntoDiaSlotsRequest [diaSemana=" + diaSemana + ", slotFinal=" + slotFinal + ", slotInicio=" + slotInicio + "]";
	}

}