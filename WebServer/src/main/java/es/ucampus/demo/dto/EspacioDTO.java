package es.ucampus.demo.dto;

import java.util.Vector;

public class EspacioDTO {
	private String id_espacio;
	private String id_edificio;
	private String id_utc;
	private String superficie;
	private int reservable;
	private int alquilable;
	private double tarifa;
	private Vector<EquipamientoDTO> equipamientos;
	private double lng_center;
	private double lat_center;

	public EspacioDTO() {
	}

	public String getId_espacio() {
		return id_espacio;
	}

	public void setId_espacio(String id_espacio) {
		this.id_espacio = id_espacio;
	}

	public String getId_edificio() {
		return id_edificio;
	}

	public void setId_edificio(String id_edificio) {
		this.id_edificio = id_edificio;
	}

	public String getId_utc() {
		return id_utc;
	}

	public void setId_utc(String id_utc) {
		this.id_utc = id_utc;
	}

	public String getSuperficie() {
		return superficie;
	}

	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}

	public int getReservable() {
		return reservable;
	}

	public void setReservable(int reservable) {
		this.reservable = reservable;
	}

	public Vector<EquipamientoDTO> getEquipamientos() {
		return equipamientos;
	}

	public void setEquipamientos(Vector<EquipamientoDTO> equipamientos) {
		this.equipamientos = equipamientos;
	}

	@Override
	public String toString() {
		return "EspacioDTO [alquilable=" + alquilable + ", equipamientos=" + equipamientos + ", id_edificio="
				+ id_edificio + ", id_espacio=" + id_espacio + ", id_utc=" + id_utc + ", lat_center=" + lat_center
				+ ", lng_center=" + lng_center + ", reservable=" + reservable + ", superficie=" + superficie
				+ ", tarifa=" + tarifa + "]";
	}

	public double getLng_center() {
		return lng_center;
	}

	public void setLng_center(double lng_center) {
		this.lng_center = lng_center;
	}

	public double getLat_center() {
		return lat_center;
	}

	public void setLat_center(double lat_center) {
		this.lat_center = lat_center;
	}

	public int getAlquilable() {
		return alquilable;
	}

	public void setAlquilable(int alquilable) {
		this.alquilable = alquilable;
	}

	public double getTarifa() {
		return tarifa;
	}

	public void setTarifa(double tarifa) {
		this.tarifa = tarifa;
	}
}