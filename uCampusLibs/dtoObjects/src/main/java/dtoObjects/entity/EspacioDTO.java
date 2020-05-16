package dtoObjects.entity;

import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import domainObjects.entity.Espacio;
import domainObjects.valueObject.Equipamiento;

public class EspacioDTO {
	private String id_espacio;
	private String id_edificio;
	private String id_utc;
	private String superficie;
	private int reservable;
	private int alquilable;
	private double tarifa;
	private Vector<Equipamiento> equipamientos;
	private double lng_center;
	private double lat_center;

	public EspacioDTO() {
	}

	public EspacioDTO(Espacio es) {
		this.id_espacio = es.getId_espacio();
		this.id_edificio = es.getId_edificio();
		this.superficie = es.getSuperficie();
		this.reservable = es.getReservable();
		this.alquilable = es.getAlquilable();
		this.tarifa = es.getTarifa();
		this.id_utc = es.getId_utc();
		this.equipamientos = es.getEquipamientos();
		this.lng_center = es.getGeom().getCentroid().getX();
		this.lat_center = es.getGeom().getCentroid().getY();
	}

	public JSONObject toJson() {
		JSONObject j = new JSONObject();
		j.put("id_espacio", this.id_espacio);
		j.put("id_edificio", this.id_edificio);
		j.put("superficie", this.superficie);
		j.put("reservable", this.reservable);
		j.put("alquilable", this.alquilable);
		j.put("tarifa", this.tarifa);
		j.put("id_utc", this.id_utc);
		/* j.put("geom", this.geom.); */
		j.put("lng_center", this.lng_center);
		j.put("lat_center", this.lat_center);

		JSONArray equipamientosJSON = new JSONArray();
		for (int i = 0; i < this.equipamientos.size(); i++) {
			JSONObject j2 = new JSONObject();
			j2.put("tipo", equipamientos.get(i).getTipo().name());
			j2.put("cantidad", equipamientos.get(i).getCantidad());
			equipamientosJSON.add(j2);
		}
		j.put("equipamientos", equipamientosJSON);
		return j;
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

	public Vector<Equipamiento> getEquipamientos() {
		return equipamientos;
	}

	public void setEquipamientos(Vector<Equipamiento> equipamientos) {
		this.equipamientos = equipamientos;
	}

	@Override
	public String toString() {
		return "EspacioDTO [alquilable=" + alquilable + ", equipamientos=" + equipamientos + ", id_edificio="
				+ id_edificio + ", id_espacio=" + id_espacio + ", id_utc=" + id_utc + ", lat_center=" + lat_center
				+ ", lng_center=" + lng_center + ", reservable=" + reservable + ", superficie=" + superficie
				+ ", tarifa=" + tarifa + "]";
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
}