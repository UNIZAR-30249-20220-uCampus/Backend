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

	public Reserva() {
		this.estado = EstadoReserva.PENDIENTE;
	}

	public Reserva(Espacio espacio, Horario horario, String usuario) {
		this.espacio = espacio;
		this.horario = horario;
		this.usuario = usuario;
		this.estado = EstadoReserva.PENDIENTE;
	}

	public Reserva(Espacio espacio, HorarioRequest horario, String usuario) {
		this.espacio = espacio;
		this.horario = new Horario(horario);
		this.usuario = usuario;
		this.estado = EstadoReserva.PENDIENTE;
	}

	public Long getId(){
		return id;
	}
	
	public String getUsuario() {
		return usuario;
	}

	public Espacio getEspacio() {
		return espacio;
	}

	public void setEspacio(Espacio espacio) {
		this.espacio = espacio;
	}

	public String getIdEspacio() {
		return espacio.getId_espacio();
	}

	public Horario getHorario() {
		return horario;
	}

	public void aceptarReserva() {
		estado = EstadoReserva.ACEPTADA;
	}

	public void cancelarReserva() {
		estado = EstadoReserva.CANCELADA;
	}

	public EstadoReserva getEstado(){
		return estado;
	}

	public boolean mismoEspacio(Reserva reserva){
		if(this.espacio.getId_espacio().equals(reserva.getIdEspacio())){
			return true;
		}
		return false;
	}

	public boolean estaAceptada(Reserva reserva){
		return this.estado.name().equals(reserva.estado.name());
	}

	public boolean hayColision(Reserva reserva){
		boolean colision = false;
		
		if(this.horario.coincidenFechas(reserva.getHorario())){
			if(this.horario.coincidenSemanas(reserva.getHorario())){
				if(this.horario.coincidenDias(reserva.getHorario())){
					if(this.horario.coincidenSlots(reserva.getHorario())){
						colision = true;
					}
				}
			}

		}
		return colision;
	}

	@Override
	public String toString() {
		return "Reserva [espacio=" + espacio + ", horario=" + horario + ", id=" + id + ", usuario=" + usuario + "]";
	}

}