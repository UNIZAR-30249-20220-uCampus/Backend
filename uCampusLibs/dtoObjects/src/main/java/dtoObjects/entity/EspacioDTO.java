package dtoObjects.entity;

import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import domainObjects.entity.Espacio;
import domainObjects.valueObject.Equipamiento;
import dtoObjects.valueObject.EquipamientoDTO;

public class EspacioDTO {
	private String id_espacio;
	private String id_edificio;
	private String id_utc;
	private String superficie;
	private int reservable;
	private int alquilable;
	private double tarifa;
	private Vector<EquipamientoDTO> equipamientos;
	private int planta;
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
		Vector<EquipamientoDTO> equi = new Vector<EquipamientoDTO>();
		for (Equipamiento equipamiento : es.getEquipamientos()) {
			equi.add(new EquipamientoDTO(equipamiento));
		}
		this.equipamientos = equi;
		this.planta = es.getPlanta();
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
		j.put("planta", this.planta);
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

	public String getId_edificio() {
		return id_edificio;
	}

	public String getId_utc() {
		return id_utc;
	}

	public String getSuperficie() {
		return superficie;
	}

	public int getReservable() {
		return reservable;
	}
	
	public Vector<EquipamientoDTO> getEquipamientos() {
		return equipamientos;
	}

	public int getAlquilable() {
		return alquilable;
	}

	public boolean esAlquilable() {
		return alquilable == 1;
	}

	public double getTarifa() {
		return tarifa;
	}

	public int getPlanta() {
		return planta;
	}

	public double getLng_center() {
		return lng_center;
	}

	public double getLat_center() {
		return lat_center;
	}

	@Override
	public String toString() {
		return "EspacioDTO [alquilable=" + alquilable + ", equipamientos=" + equipamientos + ", id_edificio="
				+ id_edificio + ", id_espacio=" + id_espacio + ", id_utc=" + id_utc + ", lat_center=" + lat_center
				+ ", lng_center=" + lng_center + ", reservable=" + reservable + ", superficie=" + superficie
				+ ", tarifa=" + tarifa + "]";
	}
}