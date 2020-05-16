package es.ucampus.demo.dto;

public enum EstadoReserva {
	PENDIENTE("PENDIENTE"),
	PENDIENTEPAGO("PENDIENTE DE PAGO"),
	ACEPTADA("ACEPTADA"),
	CANCELADA("CANCELADA");

	private final String name;

	private EstadoReserva(String s) {
        name = s;
    }

    public String toString() {
       return this.name;
    }
}