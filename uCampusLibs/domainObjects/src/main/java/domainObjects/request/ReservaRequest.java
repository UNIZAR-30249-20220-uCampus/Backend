package domainObjects.request;


public class ReservaRequest {
	
	private HorarioRequest horario;

	private String usuario;


	public ReservaRequest() {}

	public HorarioRequest getHorario() {
		return horario;
	}

	public String getUsuario() {
		return usuario;
	}

	@Override
	public String toString() {
		return "ReservaRequest [horario=" + horario + ", usuario=" + usuario + "]";
	}

}