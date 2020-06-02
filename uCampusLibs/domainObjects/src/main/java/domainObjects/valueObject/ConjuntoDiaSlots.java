package domainObjects.valueObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import domainObjects.request.ConjuntoDiaSlotsRequest;

@Entity
@Table(name = "conjunto_dia_slots")
public class ConjuntoDiaSlots {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	// Entero de 1 a 7.
	@Column(name = "diasemana")
	private int diaSemana;

	/*
	 * Cada slot representa 30 min. Desde las 00:00 a las 00:30 es el slot 1. Desde
	 * las 23:30 a las 00:00 es el slot 48.
	 */
	@Column(name = "slotinicio")
	private int slotInicio;
	@Column(name = "slotfin")
	private int slotFinal;

	public ConjuntoDiaSlots() {
	}

	public ConjuntoDiaSlots(int diaSemana, int slotInicio, int slotFinal) {
		if (slotInicio >= slotFinal)
			throw new IllegalArgumentException("slotInicio no puede ser mayor o igual que slotFinal");
		if (slotInicio < 1)
			throw new IllegalArgumentException("slotInicio no puede ser menor que 1");
		if (slotFinal > 48)
			throw new IllegalArgumentException("slotFinal no puede ser mayor que 48");
		if (diaSemana < 1)
			throw new IllegalArgumentException("diaSemana no puede ser menor que 1");
		if (diaSemana > 7)
			throw new IllegalArgumentException("diaSemana no puede ser mayor que 7");
		this.diaSemana = diaSemana;
		this.slotInicio = slotInicio;
		this.slotFinal = slotFinal;
	}

	public ConjuntoDiaSlots(ConjuntoDiaSlotsRequest conjuntoDiaSlots) {
		if (conjuntoDiaSlots.getSlotInicio() >= conjuntoDiaSlots.getSlotFinal())
			throw new IllegalArgumentException("slotInicio no puede ser mayor o igual que slotFinal");
		if (conjuntoDiaSlots.getSlotInicio() < 1)
			throw new IllegalArgumentException("slotInicio no puede ser menor que 1");
		if (conjuntoDiaSlots.getSlotFinal() > 48)
			throw new IllegalArgumentException("slotFinal no puede ser mayor que 48");
		if (conjuntoDiaSlots.getDiaSemana() < 1)
			throw new IllegalArgumentException("diaSemana no puede ser menor que 1");
		if (conjuntoDiaSlots.getDiaSemana() > 7)
			throw new IllegalArgumentException("diaSemana no puede ser mayor que 7");
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

	@Override
	public String toString() {
		return "ConjuntoDiaSlots [diaSemana=" + diaSemana + ", slotFinal=" + slotFinal + ", slotInicio=" + slotInicio
				+ "]";
	}

	/*
	 * Dado un ConjuntoDiaSlots como parámetro, devuelve TRUE si existe entre ellos
	 * algún solapamiento temporal, o sea comparten algún slot. Devuelve FALSE en
	 * cualquier otro caso.
	 */
	public boolean conflictoCon(ConjuntoDiaSlots c) {
		boolean conflicto = false;
		if (this.diaSemana == c.diaSemana) {
			if (this.slotInicio <= c.slotInicio && c.slotInicio <= this.slotFinal) {
				conflicto = true;
			} else if (c.slotInicio <= this.slotInicio && this.slotInicio <= c.slotFinal) {
				conflicto = true;
			}
		}
		return conflicto;
	}
}