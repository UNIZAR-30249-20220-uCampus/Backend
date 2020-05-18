package domainObjects.valueObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import domainObjects.request.ConjuntoDiaSlotsRequest;
import domainObjects.request.HorarioRequest;

@Entity
@Table(name = "horario")
public class Horario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "fechainicio")
	private Date fechaInicio;
	@Column(name = "fechafin")
	private Date fechaFin;
	private int frecuencia;

	@ManyToMany(cascade=CascadeType.ALL)
	private List<ConjuntoDiaSlots> conjuntoDiaSlots;

	public Horario(){}

	public Horario(Date fechaInicio, Date fechaFin, int frecuencia) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.frecuencia = frecuencia;
		this.conjuntoDiaSlots = new ArrayList<ConjuntoDiaSlots>();
	}

	public Horario(Date fechaInicio, Date fechaFin, int frecuencia, List<ConjuntoDiaSlots> conjuntoDiaSlots) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.frecuencia = frecuencia;
		this.conjuntoDiaSlots = conjuntoDiaSlots;
	}

	public Horario(List<ConjuntoDiaSlots> conjuntoDiaSlots) {
		this.conjuntoDiaSlots = conjuntoDiaSlots;
	}

	public Horario(HorarioRequest horario) {
		this.fechaInicio = horario.getFechaInicio();
		this.fechaFin = horario.getFechaFin();
		this.frecuencia = horario.getFrecuencia();
		List<ConjuntoDiaSlots> c = new ArrayList<ConjuntoDiaSlots>();
		for (ConjuntoDiaSlotsRequest conjuntoDiaSlots : horario.getConjuntoDiaSlots()) {
			c.add(new ConjuntoDiaSlots(conjuntoDiaSlots));
		}
		this.conjuntoDiaSlots = c;
	}

	/*
	 * Devuelve true si comparando la fecha de inicio y de fin de dos horarios
	 * existe una potencial colisión. Devuelve false en cualquier otro caso.
	 */
	public boolean coincidenFechas(Horario horario2) {
		boolean hayColision = false;
		// Fecha1 empieza antes que Fecha2 y Fecha1 acaba más tarde que Fecha2
		if (this.fechaInicio.compareTo(horario2.getFechaInicio()) <= 0
				&& this.fechaFin.compareTo(horario2.getFechaFin()) >= 0) {
			hayColision = true;
		}

		// Fecha1 empieza más tarde que Fecha2 y Fecha1 acaba antes que Fecha2
		else if (this.fechaInicio.compareTo(horario2.getFechaInicio()) >= 0
				&& this.fechaFin.compareTo(horario2.getFechaFin()) <= 0) {
			hayColision = true;
		}

		// Fecha1 empieza antes que Fecha2 y Fecha1 acaba antes que Fecha2
		// pero Fecha1 acaba cuando Fecha2 ya ha empezado
		else if (this.fechaInicio.compareTo(horario2.getFechaInicio()) <= 0
				&& this.fechaFin.compareTo(horario2.getFechaFin()) <= 0
				&& this.fechaFin.compareTo(horario2.getFechaInicio()) >= 0) {
			hayColision = true;
		}

		// Fecha1 empieza más tarde que Fecha2 y Fecha1 acaba más tarde que Fecha2
		// pero Fecha1 empieza cuando Fecha2 no ha acabado
		else if (this.fechaInicio.compareTo(horario2.getFechaInicio()) >= 0
				&& this.fechaFin.compareTo(horario2.getFechaFin()) >= 0
				&& this.fechaInicio.compareTo(horario2.getFechaFin()) <= 0) {
			hayColision = true;
		}
		return hayColision;
	}

	/*
	 * Dados dos vectores de enteros ordenados, devuelve otro vector resultado de su
	 * unión.
	 */
	public Vector<Integer> findUnion(Vector<Integer> firstArr, Vector<Integer> secondArr) {
		int i = 0;
		int j = 0;
		Vector<Integer> union = new Vector<>();
		while (i < firstArr.size() && j < secondArr.size()) {
			if (firstArr.get(i) < secondArr.get(j)) {
				i++;
			} else if (secondArr.get(j) < firstArr.get(i)) {
				j++;
			} else {
				// El elemento [i] se añade a la union cuando es igual que el elemento [j] y no
				// se ha añadido ya
				if (union.size() == 0 || (union.get(union.size() - 1) != firstArr.get(i))) {
					union.add(firstArr.get(i));
				}
				i++;
				j++;
			}
		}
		return union;
	}

	/*
	 * Devuelve true si existe alguna semana en la que ambos horarios coinciden.
	 * Devuelve false en cualquier otro caso.
	 */
	public boolean coincidenSemanas(Horario horario2) {
		Calendar horario1Inicio = Calendar.getInstance();
		Calendar horario1Fin = Calendar.getInstance();
		Calendar horario2Inicio = Calendar.getInstance();
		Calendar horario2Fin = Calendar.getInstance();
		horario1Inicio.setTime(this.fechaInicio);
		horario1Fin.setTime(this.fechaFin);
		horario2Inicio.setTime(horario2.getFechaInicio());
		horario2Fin.setTime(horario2.getFechaFin());

		Vector<Integer> semanas1 = new Vector<>();
		Vector<Integer> semanas2 = new Vector<>();

		int semanaAux;

		// semanas1 almacenará los números de semana que tiene el primer horario
		semanaAux = horario1Inicio.get(Calendar.WEEK_OF_YEAR);
		while (semanaAux <= horario1Fin.get(Calendar.WEEK_OF_YEAR)) {
			semanas1.add(semanaAux);
			semanaAux += this.frecuencia;
		}
		// semanas2 almacenará los números de semana que tiene el segundo horario
		semanaAux = horario2Inicio.get(Calendar.WEEK_OF_YEAR);
		while (semanaAux <= horario2Fin.get(Calendar.WEEK_OF_YEAR)) {
			semanas2.add(semanaAux);
			semanaAux += horario2.getFrecuencia();
		}

		Vector<Integer> unionSemanas = findUnion(semanas1, semanas2);
		if (unionSemanas.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Dados dos horarios, devuelve la union de los dias de la semana.
	 */
	private Vector<Integer> diasSemanaQueColisionan(Horario horario2) {
		Vector<Integer> slots1 = new Vector<>();
		Vector<Integer> slots2 = new Vector<>();
		for (int i = 0; i < this.conjuntoDiaSlots.size(); i++) {
			slots1.add(this.conjuntoDiaSlots.get(i).getDiaSemana());
		}
		for (int i = 0; i < horario2.getconjuntoDiaSlots().size(); i++) {
			slots2.add(horario2.getconjuntoDiaSlots().get(i).getDiaSemana());
		}
		return findUnion(slots1, slots2);
	}

	/*
	 * Devuelve true si existe algún día de la semana en el que ambos horarios
	 * coinciden. Devuelve false en cualquier otro caso.
	 */
	public boolean coincidenDias(Horario horario2) {
		Vector<Integer> v3 = this.diasSemanaQueColisionan(horario2);
		if (v3.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Dado un horario y un dia de la semana, devuelve los ConjuntoDiaSlot del
	 * horario donde el dia de la semana es igual al dado.
	 */
	private Vector<ConjuntoDiaSlots> obtenerConjuntosDondeDiaSemanaEs(int dia) {
		int indice = 0;
		Vector<ConjuntoDiaSlots> c = new Vector<ConjuntoDiaSlots>();
		while (indice < this.conjuntoDiaSlots.size() && dia != this.conjuntoDiaSlots.get(indice).getDiaSemana()) {
			indice++;
		}
		if (indice < this.conjuntoDiaSlots.size()) {
			while (indice < this.conjuntoDiaSlots.size() && dia == this.conjuntoDiaSlots.get(indice).getDiaSemana()) {
				c.add(this.conjuntoDiaSlots.get(indice));
				indice++;
			}
		}
		return c;
	}

	/*
	 * Devuelve true si existe algún slot en el que ambos horarios coinciden.
	 * Devuelve false en cualquier otro caso.
	 */
	public boolean coincidenSlots(Horario horario2) {
		boolean hayColision = false;
		Vector<Integer> dias = this.diasSemanaQueColisionan(horario2);
		Vector<Integer> slots1 = new Vector<>();
		Vector<Integer> slots2 = new Vector<>();
		Vector<Integer> slotsUnion = new Vector<>();
		Vector<ConjuntoDiaSlots> conjuntos1;
		Vector<ConjuntoDiaSlots> conjuntos2;
		int aux;

		// Se examinan solo los días de la semana que colisionan
		for (int i = 0; i < dias.size() && !hayColision; i++) {
			// conjuntos1 almacenará los ConjuntoDiaSlot del primer horario, del dia de la
			// semana que se examina
			// conjuntos2 almacenará los ConjuntoDiaSlot del segundo horario, del dia de la
			// semana que se examina
			conjuntos1 = this.obtenerConjuntosDondeDiaSemanaEs(dias.get(i));
			conjuntos2 = horario2.obtenerConjuntosDondeDiaSemanaEs(dias.get(i));

			for (int indice1 = 0; indice1 < conjuntos1.size() && !hayColision; indice1++) {
				for (int indice2 = 0; indice2 < conjuntos2.size() && !hayColision; indice2++) {
					// slots1 almacenará los números de slot del primer horario, del día de la
					// semana que se examina
					aux = conjuntos1.get(indice1).getSlotInicio();
					while (aux <= conjuntos1.get(indice1).getSlotFinal()) {
						slots1.add(aux);
						aux++;
					}
					// slots2 almacenará los números de slot del segundo horario, del día de la
					// semana que se examina
					aux = conjuntos2.get(indice2).getSlotInicio();
					while (aux <= conjuntos2.get(indice2).getSlotFinal()) {
						slots2.add(aux);
						aux++;
					}
					// Si hay algún slot que produce colisión se termina el bucle y se devuelve true
					slotsUnion = findUnion(slots1, slots2);
					if (slotsUnion.size() > 0) {
						hayColision = true;
					}
				}
			}
		}
		return hayColision;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public int getFrecuencia() {
		return frecuencia;
	}

	public List<ConjuntoDiaSlots> getconjuntoDiaSlots() {
		return conjuntoDiaSlots;
	}

	@Override
	public String toString() {
		return "Horario [conjuntoDiaSlots=" + conjuntoDiaSlots + ", fechaFin=" + fechaFin + ", fechaInicio="
				+ fechaInicio + ", frecuencia=" + frecuencia + ", id=" + id + "]";
	}

}