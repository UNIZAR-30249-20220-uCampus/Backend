package domainObjects.valueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "conjunto_dia_slots")
public class ConjuntoDiaSlots {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "diasemana")
	private int diaSemana;
	@Column(name = "slotinicio")
	private int slotInicio;
	@Column(name = "slotfin")
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

	@Override
	public String toString() {
		return "ConjuntoDiaSlots [diaSemana=" + diaSemana + ", slotFinal=" + slotFinal + ", slotInicio=" + slotInicio
				+ "]";
	}
}