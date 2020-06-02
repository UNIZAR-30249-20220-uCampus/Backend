package domainObjects.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import domainObjects.request.HorarioRequest;
import domainObjects.valueObject.EstadoReserva;
import domainObjects.valueObject.Horario;

@Entity
@Table(name = "reserva")
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne
	private Espacio espacio;
	@OneToOne(cascade = CascadeType.ALL)
	private Horario horario;
	private String usuario;
	private EstadoReserva estado;
	private String tipo;

	public Reserva() {
		this.estado = EstadoReserva.PENDIENTE;
	}

	public Reserva(Espacio espacio, Horario horario, String usuario, String tipo) {
		this.espacio = espacio;
		this.horario = horario;
		this.usuario = usuario;
		this.estado = EstadoReserva.PENDIENTE;
		this.tipo = tipo;
	}

	public Reserva(Espacio espacio, HorarioRequest horario, String usuario, String tipo) {
		this.espacio = espacio;
		this.horario = new Horario(horario);
		this.usuario = usuario;
		this.estado = EstadoReserva.PENDIENTE;
		this.tipo = tipo;
	}

	public Long getId() {
		return id;
	}

	public String getUsuario() {
		return usuario;
	}

	public Espacio getEspacio() {
		return espacio;
	}

	public String getIdEspacio() {
		return espacio.getId_espacio();
	}

	public Horario getHorario() {
		return horario;
	}

	/*
	 * Devuelve TRUE si la Reserva es aceptada. Devuelve FALSE en cualquier otro
	 * caso.
	 */
	public boolean aceptarReserva() {
		if (estado == EstadoReserva.PENDIENTE && tipo.equals("alquiler")) {
			estado = EstadoReserva.PENDIENTEPAGO;
			return true;
		} else if (estado == EstadoReserva.PENDIENTE && tipo.equals("reserva")) {
			estado = EstadoReserva.ACEPTADA;
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Devuelve TRUE si la Reserva es pagada. Devuelve FALSE en cualquier otro caso.
	 */
	public boolean pagarReserva() {
		if (estado == EstadoReserva.PENDIENTEPAGO && tipo.equals("alquiler")) {
			estado = EstadoReserva.ACEPTADA;
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Devuelve TRUE si la Reserva es cancelada. Devuelve FALSE en cualquier otro
	 * caso.
	 */
	public boolean cancelarReserva() {
		estado = EstadoReserva.CANCELADA;
		return true;
	}

	public EstadoReserva getEstado() {
		return estado;
	}

	public String getTipo() {
		return tipo;
	}

	/*
	 * Dada una Reserva como parámetro, devuelve TRUE si ambas Reserva están
	 * asociadas al mismo Espacio. Devuelve FALSE en cualquier otro caso.
	 */
	public boolean mismoEspacio(Reserva reserva) {
		if (this.espacio.getId_espacio().equals(reserva.getIdEspacio())) {
			return true;
		}
		return false;
	}

	/*
	 * Dada una Reserva como parámetro, devuelve TRUE si los Horario de ambas
	 * Reserva colisionan y la Reserva dada está aceptada o pendiente de pago.
	 * Devuelve FALSE en cualquier otro caso.
	 */
	public boolean hayColision(Reserva reserva) {
		boolean colision = false;
		if (reserva.getEstado() == EstadoReserva.ACEPTADA || reserva.getEstado() == EstadoReserva.PENDIENTEPAGO) {
			if (this.getHorario().hayColision(reserva.getHorario())) {
				colision = true;
			}
		}
		return colision;
	}

	@Override
	public String toString() {
		return "Reserva [espacio=" + espacio + ", estado=" + estado + ", horario=" + horario + ", id=" + id + ", tipo="
				+ tipo + ", usuario=" + usuario + "]";
	}

}