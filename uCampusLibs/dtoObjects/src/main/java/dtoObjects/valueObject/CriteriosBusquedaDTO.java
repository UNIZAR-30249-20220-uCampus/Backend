package dtoObjects.valueObject;

import java.util.ArrayList;
import java.util.List;

import domainObjects.valueObject.Equipamiento;
import domainObjects.valueObject.TipoEquipamiento;

public class CriteriosBusquedaDTO {

	private String nombre;
	private int aforo;
	private List<Equipamiento> equipamientos;
	private List<String> filtrosActivos;
	private List<Equipamiento> totalEquipamientos;

	public CriteriosBusquedaDTO() {
		totalEquipamientos = new ArrayList<Equipamiento>();
		// Incluye todos los equipamientos disponibles en los equipamientos del Espacio
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.CANYON_FIJO, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.PANTALLA_PROYECTOR, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.EQUIPO_DE_SONIDO, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.TV, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.VIDEO, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.DVD, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.FOTOCOPIADORAS, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.IMPRESORAS, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.ORDENADORES, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.FAXES, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.TELEFONOS, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.PIZARRA, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.NMRO_EXTINTORES_POLVO, 0));
		totalEquipamientos.add(new Equipamiento(TipoEquipamiento.NMRO_EXTINTORES_CO2, 0));
	}

	public String getNombre() {
		return nombre;
	}

	public void setId(String nombre) {
		this.nombre = nombre;
	}

	public int getAforo() {
		return aforo;
	}

	public void setAforo(int aforo) {
		this.aforo = aforo;
	}

	public List<Equipamiento> getEquipamientos() {
		return equipamientos;
	}

	public void setEquipamientos(List<Equipamiento> equipamientos) {
		this.equipamientos = equipamientos;
	}

	public List<String> getFiltrosActivos() {
		return filtrosActivos;
	}

	public void setFiltrosActivos(List<String> filtrosActivos) {
		this.filtrosActivos = filtrosActivos;
	}

	public boolean busquedaPorId() {
		for (String filtro : filtrosActivos) {
			if (filtro.equals("NOMBRE")) {
				return true;
			}
		}
		return false;
	}

	public boolean busquedaPorAforo() {
		for (String filtro : filtrosActivos) {
			if (filtro.equals("AFORO")) {
				return true;
			}
		}
		return false;
	}

	public boolean busquedaPorEquipamientos() {
		return !equipamientos.isEmpty();
	}

	public boolean equipamientoActivo(String equipamiento) {
		for (String filtro : filtrosActivos) {
			if (filtro.equals(equipamiento)) {
				return true;
			}
		}
		return false;
	}

	public int cantidad(String equipamiento) {
		for (Equipamiento equi : equipamientos) {
			if (equi.getTipo().name().equals(equipamiento)) {
				return equi.getCantidad();
			}
		}
		return 0;
	}

	public List<Integer> cantidadEquipamientos() {
		List<Integer> cantidad = new ArrayList<Integer>();
		for (Equipamiento equip : totalEquipamientos) {
			if (equipamientoActivo(equip.getTipo().name())) {
				cantidad.add(cantidad(equip.getTipo().name()));
			} else {
				cantidad.add(0);
			}
		}
		return cantidad;
	}

	@Override
	public String toString() {
		return "CriteriosBusquedaDTO [aforo=" + aforo + ", equipamientos=" + equipamientos + ", filtrosActivos="
				+ filtrosActivos + ", nombre=" + nombre + ", totalEquipamientos=" + totalEquipamientos + "]";
	}

}
