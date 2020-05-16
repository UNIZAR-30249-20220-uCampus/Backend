package es.ucampus.demo.dto;


public class ReservaDTO {
	
	private Horario horario;

	private String usuario;

	//private EstadoReserva estado;

	public ReservaDTO() {}
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