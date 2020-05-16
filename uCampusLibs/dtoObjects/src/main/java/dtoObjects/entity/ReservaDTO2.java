package dtoObjects.entity;

import domainObjects.valueObject.Horario;

public class ReservaDTO2 {
	
	private Horario horario;

	private String usuario;

	//private EstadoReserva estado;

	public ReservaDTO2() {}
/*
	public EspacioDTO getEspacio() {
		return espacio;
	}
*/

	public Horario getHorario() {
		return horario;
	}

	public String getUsuario() {
		return usuario;
	}
/*
	public EstadoReserva getEstado() {
		return estado;
	}
*/

	@Override
	public String toString() {
		return "ReservaDTO [horario=" + horario
				+ ", usuario=" + usuario + "]";
	}

}