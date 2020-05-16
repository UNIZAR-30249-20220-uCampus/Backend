package dtoObjects.entity;

import domainObjects.entity.Reserva;
import domainObjects.valueObject.EstadoReserva;
import domainObjects.valueObject.Horario;

public class ReservaDTO {
	
	private long id;

	private EspacioDTO espacio;

	private Horario horario;

	private String usuario;

	private EstadoReserva estado;

	public ReservaDTO() {}

	public ReservaDTO(Reserva reserva) {
		this.id = reserva.getId();
		this.espacio = new EspacioDTO(reserva.getEspacio());
		this.horario = reserva.getHorario();
		this.usuario = reserva.getUsuario();
		this.estado = reserva.getEstado();
	}

	public EspacioDTO getEspacio() {
		return espacio;
	}

	public Horario getHorario() {
		return horario;
	}

	public String getUsuario() {
		return usuario;
	}

	public EstadoReserva getEstado() {
		return estado;
	}


	@Override
	public String toString() {
		return "ReservaDTO [espacio=" + espacio + ", estado=" + estado + ", horario=" + horario + ", id=" + id
				+ ", usuario=" + usuario + "]";
	}

}