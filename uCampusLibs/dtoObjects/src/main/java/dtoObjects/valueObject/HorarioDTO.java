package dtoObjects.valueObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import domainObjects.valueObject.ConjuntoDiaSlots;
import domainObjects.valueObject.Horario;


public class HorarioDTO {

	private Date fechaInicio;
	private Date fechaFin;
	private int frecuencia;

	private List<ConjuntoDiaSlotsDTO> conjuntoDiaSlots;

	public HorarioDTO(){}

	public HorarioDTO(Horario horario){
		this.fechaInicio = horario.getFechaInicio();
		this.fechaFin = horario.getFechaFin();
		this.frecuencia = horario.getFrecuencia();
		List<ConjuntoDiaSlotsDTO> c = new ArrayList<ConjuntoDiaSlotsDTO>();
		for (ConjuntoDiaSlots conjuntoDiaSlots : horario.getconjuntoDiaSlots()) {
			c.add(new ConjuntoDiaSlotsDTO(conjuntoDiaSlots));
		}
		this.conjuntoDiaSlots = c;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public int getFrecuencia() {
		return frecuencia;
	}

	public List<ConjuntoDiaSlotsDTO> getConjuntoDiasSlots() {
		return conjuntoDiaSlots;
	}
}