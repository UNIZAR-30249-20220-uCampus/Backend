package domainObjects.request;

/* Clase necesaria para construir Reserva a partir de un body de una petición http */
public class ReservaRequest {

	private HorarioRequest horario;

	private String usuario;

	private String tipo;

	public ReservaRequest() {
	}

	public HorarioRequest getHorario() {
		return horario;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getTipo() {
		return tipo;
	}

	@Override
	public String toString() {
		return "ReservaRequest [horario=" + horario + ", tipo=" + tipo + ", usuario=" + usuario + "]";
	}

}