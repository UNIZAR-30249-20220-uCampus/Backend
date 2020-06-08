package domainObjects.entity;

import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;

import domainObjects.valueObject.Equipamiento;
import domainObjects.valueObject.TipoEquipamiento;

@Entity
@Table(name = "espaciosgeneral")
public class Espacio {

	@Id
	private String id_espacio;
	private String id_edificio;
	private String id_utc;
	private String id_centro;
	private int tipo_de_uso;
	private String superficie;
	private int reservable;
	private int alquilable;
	
	// Euros por hora a pagar para alquilar el Espacio
	@Transient
	private double tarifa;
	@Column(name = "nmro_plazas")
	private int plazas;
	@Column(name = "canyon_fijo")
	private int canyon;
	@Column(name = "pantalla_proyector")
	private int proyector;
	@Column(name = "equipo_de_sonido")
	private int sonido;
	private String tv;
	private int video;
	private int dvd;
	private int fotocopiadoras;
	private int impresoras;
	private int ordenadores;
	private int faxes;
	private int telefonos;
	private int pizarra;
	@Column(name = "nmro_extintores_polvo")
	private int extpolvo;
	@Column(name = "nmro_extintores_co2")
	private int extco2;

	@JsonSerialize(using = GeometrySerializer.class)
	@JsonDeserialize(using = GeometryDeserializer.class)
	@Column(name = "geom", columnDefinition = "GEOMETRY")
	private Geometry geom;
	private int planta;

	// La etiqueta Transient indica que no tienen relación directa con ninguna
	// columna en la BD
	@Transient
	private Vector<Equipamiento> equipamientos;
	@Transient
	private Vector<Equipamiento> maximoDeEquipamientos;

	public Espacio() {
		super();
		this.maximoDeEquipamientos = new Vector<Equipamiento>();
		this.equipamientos = new Vector<Equipamiento>();
	}

	public Espacio(Vector<Equipamiento> equipamiento, Vector<Equipamiento> equipamientoMaximos) {
		super();
		this.maximoDeEquipamientos = equipamientoMaximos;
		this.equipamientos = equipamiento;
	}

	// Una vez el Espacio se ha construido, se definen los equipamientos y
	// equipamientos máximos permitidos. Además se calcula su tarifa.
	@PostLoad
	private void build() {
		definirEquipamientosMaximos();
		definirEquipamientosExistentes();
		this.tarifa = this.calcularTarifa();
	}

	// Define la cantidad máxima posible para cada tipo de equipamiento en el
	// Espacio
	private void definirEquipamientosMaximos() {

		int superficieFormateado = (int) Double
				.parseDouble(this.superficie.substring(1, this.superficie.length() - 1).replace(",", "."));

		definirEquipamientosMaximosAUX(TipoEquipamiento.CANYON_FIJO, 2);
		definirEquipamientosMaximosAUX(TipoEquipamiento.PANTALLA_PROYECTOR, 1);
		definirEquipamientosMaximosAUX(TipoEquipamiento.EQUIPO_DE_SONIDO, 1);
		definirEquipamientosMaximosAUX(TipoEquipamiento.TV, 3);
		definirEquipamientosMaximosAUX(TipoEquipamiento.VIDEO, 7);
		definirEquipamientosMaximosAUX(TipoEquipamiento.DVD, 2);
		definirEquipamientosMaximosAUX(TipoEquipamiento.FOTOCOPIADORAS, 5);
		definirEquipamientosMaximosAUX(TipoEquipamiento.IMPRESORAS, 5);
		definirEquipamientosMaximosAUX(TipoEquipamiento.ORDENADORES, superficieFormateado);
		definirEquipamientosMaximosAUX(TipoEquipamiento.FAXES, 2);
		definirEquipamientosMaximosAUX(TipoEquipamiento.TELEFONOS, 8);
		definirEquipamientosMaximosAUX(TipoEquipamiento.PIZARRA, 5);
		definirEquipamientosMaximosAUX(TipoEquipamiento.NMRO_EXTINTORES_POLVO, 10);
		definirEquipamientosMaximosAUX(TipoEquipamiento.NMRO_EXTINTORES_CO2, 10);
		definirEquipamientosMaximosAUX(TipoEquipamiento.NMRO_PLAZAS, superficieFormateado);
	}

	// Dado un tipo de equipamiento y su cantidad, lo incluye en los equipamientos
	// máximos permitidos del Espacio
	private void definirEquipamientosMaximosAUX(TipoEquipamiento tipo, int cantidad) {
		this.maximoDeEquipamientos.add(new Equipamiento(tipo, cantidad));
	}

	// Incluye todos los equipamientos disponibles en los equipamientos del Espacio
	private void definirEquipamientosExistentes() {
		definirEquipamientosExistentesAUX(TipoEquipamiento.CANYON_FIJO, this.canyon);
		definirEquipamientosExistentesAUX(TipoEquipamiento.PANTALLA_PROYECTOR, this.proyector);
		definirEquipamientosExistentesAUX(TipoEquipamiento.EQUIPO_DE_SONIDO, this.sonido);
		definirEquipamientosExistentesAUX(TipoEquipamiento.TV, Integer.parseInt(this.tv));
		definirEquipamientosExistentesAUX(TipoEquipamiento.VIDEO, this.video);
		definirEquipamientosExistentesAUX(TipoEquipamiento.DVD, this.dvd);
		definirEquipamientosExistentesAUX(TipoEquipamiento.FOTOCOPIADORAS, this.fotocopiadoras);
		definirEquipamientosExistentesAUX(TipoEquipamiento.IMPRESORAS, this.impresoras);
		definirEquipamientosExistentesAUX(TipoEquipamiento.ORDENADORES, this.ordenadores);
		definirEquipamientosExistentesAUX(TipoEquipamiento.FAXES, this.faxes);
		definirEquipamientosExistentesAUX(TipoEquipamiento.TELEFONOS, this.telefonos);
		definirEquipamientosExistentesAUX(TipoEquipamiento.PIZARRA, this.pizarra);
		definirEquipamientosExistentesAUX(TipoEquipamiento.NMRO_EXTINTORES_POLVO, this.extpolvo);
		definirEquipamientosExistentesAUX(TipoEquipamiento.NMRO_EXTINTORES_CO2, this.extco2);
		definirEquipamientosExistentesAUX(TipoEquipamiento.NMRO_PLAZAS, this.plazas);
	}

	// Dado un tipo de equipamiento y su cantidad, lo incluye en los equipamientos
	// del Espacio
	private void definirEquipamientosExistentesAUX(TipoEquipamiento tipo, int cantidad) {
		if (cantidad > 0 || tipo == TipoEquipamiento.NMRO_PLAZAS) {
			this.equipamientos.add(new Equipamiento(tipo, cantidad));
		}
	}

	public Vector<Equipamiento> getEquipamientos() {
		return this.equipamientos;
	}

	public String getId_espacio() {
		return id_espacio;
	}

	public String getId_edificio() {
		return id_edificio;
	}

	public String getSuperficie() {
		return superficie;
	}

	public int getReservable() {
		return reservable;
	}

	public Vector<Equipamiento> getMaximoDeEquipamientos() {
		return maximoDeEquipamientos;
	}

	public String getId_utc() {
		return id_utc;
	}

	public int getTipo_de_uso() {
		return tipo_de_uso;
	}

	public int getPlanta() {
		return planta;
	}

	@Override
	public String toString() {
		return "Espacio [alquilable=" + alquilable + ", equipamientos=" + equipamientos + ", geom=" + geom
				+ ", id_centro=" + id_centro + ", id_edificio=" + id_edificio + ", id_espacio=" + id_espacio
				+ ", id_utc=" + id_utc + ", maximoDeEquipamientos=" + maximoDeEquipamientos + ", reservable="
				+ reservable + ", superficie=" + superficie + ", tarifa=" + tarifa + ", tipo_de_uso=" + tipo_de_uso
				+ "]";
	}

	public String equi() {
		String e = "";
		for (Equipamiento equipamiento : equipamientos) {
			e += e + equipamiento.toString() + " ";
		}
		return e;
	}

	/*
	 * Devuelve TRUE si el Espacio contiene al menos los equipamientos dados como
	 * parámetro. Devuelve FALSE en cualquier otro caso
	 */
	public boolean comprobarEquipamientoMinimoNecesario(Vector<Equipamiento> eq) {
		int equipamientosQueCumplenMinimo = 0;
		for (int i = 0; i < this.equipamientos.size(); i++) {
			for (int j = 0; j < eq.size(); j++) {
				if (this.equipamientos.elementAt(i).getTipo() == eq.elementAt(j).getTipo()) {
					if (this.equipamientos.elementAt(i).getCantidad() >= eq.elementAt(j).getCantidad()) {
						equipamientosQueCumplenMinimo++;
					}
				}
			}
		}
		return equipamientosQueCumplenMinimo == eq.size();
	}

	/*
	 * Devuelve valor TRUE si el equipamiento dado como parametro se agrega
	 * correctamente a la lista de equipamientos del Espacio. Devuelve valor FALSE
	 * en cualquier otro caso
	 */
	public boolean agregarEquipamiento(TipoEquipamiento tipo, int cantidad) {
		if (cantidad > 0) {
			boolean agregado = false;
			boolean encontrado = false;
			int i = 0;
			while (!encontrado && i < this.equipamientos.size()) {
				// Hay equipamiento del mismo tipo en el Espacio
				if ((this.equipamientos.elementAt(i)).getTipo() == tipo) {
					encontrado = true;
					int cantidadPrevia = this.equipamientos.elementAt(i).getCantidad();
					Equipamiento eq = new Equipamiento(tipo, cantidad + cantidadPrevia);
					// La cantidad de equipamiento respeta los máximos
					if (this.comprobarEquipamientoMaximoPermitido(eq)) {
						this.equipamientos.setElementAt(eq, i);
						agregado = true;
					}
				}
				i++;
			}
			// No hay equipamiento del mismo tipo en el Espacio
			if (!encontrado) {
				Equipamiento eq = new Equipamiento(tipo, cantidad);
				// La cantidad de equipamiento respeta los máximos
				if (this.comprobarEquipamientoMaximoPermitido(eq)) {
					this.equipamientos.addElement(eq);
					agregado = true;
				}
			}
			return agregado;
		} else if (cantidad == 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Dado un equipamiento, si corresponde con alguno del Espacio se modifica su
	 * cantidad asociada. Devuelve TRUE si se modifica la cantidad correctamente.
	 * Devuelve FALSE en cualquier otro caso
	 */
	public boolean editarEquipamiento(TipoEquipamiento tipo, int cantidad) {
		boolean editado = false;
		boolean encontrado = false;
		int i = 0;
		while (!encontrado && i < this.equipamientos.size()) {
			// Hay equipamiento del mismo tipo en el Espacio
			if (this.equipamientos.elementAt(i).getTipo() == tipo) {
				encontrado = true;
				Equipamiento eq = new Equipamiento(tipo, cantidad);
				// La cantidad de equipamiento respeta los máximos
				if (this.comprobarEquipamientoMaximoPermitido(eq)) {
					this.equipamientos.setElementAt(eq, i);
					editado = true;
				}
			}
			i++;
		}
		return editado;
	}

	/*
	 * Dado un equipamiento, si corresponde con alguno del Espacio se reduce su
	 * cantidad. Devuelve TRUE si se elimina correctamente el numero de equipamiento
	 * dado. Devuelve FALSE en cualquier otro caso
	 */
	public boolean eliminarEquipamiento(TipoEquipamiento tipo, int cantidad) {
		boolean eliminado = false;
		boolean encontrado = false;
		int i = 0;
		while (!encontrado && i < this.equipamientos.size()) {
			// Hay equipamiento del mismo tipo en el Espacio
			if (this.equipamientos.elementAt(i).getTipo() == tipo) {
				encontrado = true;
				// El equipamiento coincide con la cantidad
				if (this.equipamientos.elementAt(i).getCantidad() == cantidad) {
					this.equipamientos.remove(i);
					eliminado = true;
				}
			}
			i++;
		}
		return eliminado;
	}

	/*
	 * Dado un equipamiento, devuelve TRUE si no supera la cantidad máxima permitida
	 * para ese equipamiento en el Espacio. Devuelve FALSE en cualquier otro caso
	 */
	private boolean comprobarEquipamientoMaximoPermitido(Equipamiento eq) {
		boolean permitido = false;
		boolean encontrado = false;
		int i = 0;
		while (!encontrado && i < this.maximoDeEquipamientos.size()) {
			if (this.maximoDeEquipamientos.elementAt(i).getTipo() == eq.getTipo()) {
				encontrado = true;
				permitido = eq.getCantidad() <= this.maximoDeEquipamientos.elementAt(i).getCantidad();
			}
			i++;
		}
		return permitido;
	}

	@JsonSerialize(using = GeometrySerializer.class)
	@JsonDeserialize(using = GeometryDeserializer.class)
	@Column(name = "geom", columnDefinition = "GEOMETRY")
	public Geometry getGeom() {
		return geom;
	}

	public int getAlquilable() {
		return alquilable;
	}

	// Devuelve el precio por hora de un Espacio
	public double calcularTarifa() {
		double tarifa = 0;
		if (alquilable == 1) {
			tarifa += plazas * 3; // 3 euros/hora por plaza
			tarifa += ordenadores * 5; // 5 euros/hora por ordenador
			tarifa += geom.getArea() * 1; // 1 euro/hora por metro cuadrado
		}
		return tarifa;
	}

	public double getTarifa() {
		return tarifa;
	}
	
	public boolean cambiarAlquilable(int opcion) {
		if(opcion == 0){
			alquilable = opcion;
		}
		else if(opcion == 1){
			alquilable = opcion;
		}
		else{
			return false;
		}
		return true;

	}

	public boolean cambiarReservable(int opcion) {
		if(opcion == 0){
			reservable = opcion;
		}
		else if(opcion == 1){
			reservable = opcion;
		}
		else{
			return false;
		}
		return true;
	}

}