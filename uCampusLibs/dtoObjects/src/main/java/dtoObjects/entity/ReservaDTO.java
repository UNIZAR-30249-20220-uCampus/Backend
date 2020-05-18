package dtoObjects.entity;

import domainObjects.entity.Reserva;
import domainObjects.valueObject.EstadoReserva;
import dtoObjects.valueObject.HorarioDTO;

public class ReservaDTO {
	
	private long id;

	private String espacio;

	private HorarioDTO horario;

	private String usuario;

	private EstadoReserva estado;

	public ReservaDTO() {}

	public ReservaDTO(Reserva reserva) {
		this.id = reserva.getId();
		this.espacio = reserva.getIdEspacio();
		this.horario = new HorarioDTO(reserva.getHorario());
		this.usuario = reserva.getUsuario();
		this.estado = reserva.getEstado();
	}

	public String getEspacio() {
		return espacio;
	}

	public HorarioDTO getHorario() {
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