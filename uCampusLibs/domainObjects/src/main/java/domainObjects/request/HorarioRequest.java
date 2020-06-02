package domainObjects.request;

import java.util.Date;
import java.util.List;

/* Clase necesaria para construir Horario a partir de un body de una petición http */
public class HorarioRequest {

	private Date fechaInicio;
	private Date fechaFin;
	private int frecuencia;

	private List<ConjuntoDiaSlotsRequest> conjuntoDiaSlots;

	public HorarioRequest() {
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

	public List<ConjuntoDiaSlotsRequest> getConjuntoDiaSlots() {
		return conjuntoDiaSlots;
	}

	@Override
	public String toString() {
		return "Horario [conjuntoDiaSlots=" + conjuntoDiaSlots + ", fechaFin=" + fechaFin + ", fechaInicio="
				+ fechaInicio + ", frecuencia=" + frecuencia + "]";
	}

}