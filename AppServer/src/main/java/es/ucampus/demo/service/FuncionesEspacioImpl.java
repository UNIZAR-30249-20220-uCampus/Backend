package es.ucampus.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;
import domainObjects.request.HorarioRequest;
import domainObjects.valueObject.EstadoReserva;
import domainObjects.valueObject.Horario;
import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;
import dtoObjects.valueObject.HorarioDTO;
import es.ucampus.demo.repository.RepositorioEspacios;
import es.ucampus.demo.repository.RepositorioReservas;

@Service
@Transactional
public class FuncionesEspacioImpl implements FuncionesEspacio {

	@Autowired
	private RepositorioEspacios espaciosRepository;
	@Autowired
	private RepositorioReservas repositorioReservas;

	/**
	 * Constructor del repositorio de espacios
	 * @param espaciosRepository
	 */
	FuncionesEspacioImpl(RepositorioEspacios espaciosRepository) {
		this.espaciosRepository = espaciosRepository;
	}

	/**
	 * Dado un identificador devuelve su Espacio correspondiente en formato EspacioDTO.
	 * Devuelve null si no existe.
	 */
	public EspacioDTO getEspacioDTOId(String id) {
		Optional<Espacio> espacio = espaciosRepository.findById(id);
		// Transformar a DTO
		if (espacio.isPresent()) {
			EspacioDTO espacioDTO = new EspacioDTO(espacio.get());
			return espacioDTO;
		} else {
			return null;
		}
	}

	/**
	 * Dado un identificador devuelve su Espacio correspondiente
	 * Devuelve null si no existe.
	 */
	public Espacio getEspacioId(String id) {
		Optional<Espacio> espacio = espaciosRepository.findById(id);
		// Transformar a DTO
		if (espacio.isPresent()) {
			return espacio.get();
		} else {
			return null;
		}
	}

	/**
	 * Dada una planta y las coordenadas 'x' e 'y' devuelve el Espacio correspondiente
	 * 		en formato EspacioDTO.
	 * Devuelve null si no existe.
	 */
	public EspacioDTO getEspacioCoordenadas(int planta, double x, double y) {
		EspacioDTO espacioDTO = new EspacioDTO();
		String id = espaciosRepository.findByCoordenadas(planta, x, y);
		if (id != null) {
			System.out.println(id);
			Optional<Espacio> espacio = espaciosRepository.findById(id);
			if (espacio.isPresent()) {
				espacioDTO = new EspacioDTO(espacio.get());
			} else {
				espacioDTO = null;
			}
		} else {
			espacioDTO = null;
		}
		return espacioDTO;
	}

	/**
	 * Dado un aforo minimo devuelve una lista de Espacios en formato EspacioDTO con
	 * 		un aforo igual o superior al requerido.
	 */
	public List<EspacioDTO> buscarEspacioPorAforo(int aforo) {
		List<Espacio> espacios = espaciosRepository.findByPlazasGreaterThanEqual(aforo);
		List<EspacioDTO> espaciosDTO = new ArrayList<EspacioDTO>();
		for (Espacio espacio : espacios) {
			EspacioDTO espacioDTO = new EspacioDTO(espacio);
			espaciosDTO.add(espacioDTO);
		}

		return espaciosDTO;
	}

	/**
	 * Dados unos criterios de búsqueda devuelve una lista de Espacios en formato EspacioDTO
	 * 		que cumplen con los requisitos.
	 */
	public List<EspacioDTO> buscarEspacioPorCriterios(CriteriosBusquedaDTO criterios) {

		List<Integer> numEq = criterios.cantidadEquipamientos();

		System.out.println(numEq);
		System.out.println(criterios.getAforo());

		List<Espacio> espacios = espaciosRepository
				.findByPlazasGreaterThanEqualAndCanyonGreaterThanEqualAndProyectorGreaterThanEqualAndSonidoGreaterThanEqualAndTvGreaterThanEqualAndVideoGreaterThanEqualAndDvdGreaterThanEqualAndFotocopiadorasGreaterThanEqualAndImpresorasGreaterThanEqualAndOrdenadoresGreaterThanEqualAndFaxesGreaterThanEqualAndTelefonosGreaterThanEqualAndPizarraGreaterThanEqualAndExtpolvoGreaterThanEqualAndExtco2(
						criterios.getAforo(), numEq.get(0), numEq.get(1), numEq.get(2), Integer.toString(numEq.get(3)),
						numEq.get(4), numEq.get(5), numEq.get(6), numEq.get(7), numEq.get(8), numEq.get(9),
						numEq.get(10), numEq.get(11), numEq.get(12), numEq.get(13));
		List<EspacioDTO> espaciosDTO = new ArrayList<EspacioDTO>();
		for (Espacio espacio : espacios) {
			EspacioDTO espacioDTO = new EspacioDTO(espacio);
			espaciosDTO.add(espacioDTO);
		}

		return espaciosDTO;
	}

	/**
	 * Dados unos criterios de búsqueda y horario devuelve una lista de Espacios en formato EspacioDTO
	 * 		que cumplen con estos requisitos y tiene el horario disponible.
	 */
	public List<EspacioDTO> buscarEspaciosPorCriteriosYHorario(CriteriosBusquedaDTO criterios) {
		Reserva miReserva;
		// Almacena los Espacio que cumplen los criterios de equipamientos y son
		// reservables en el horario dado
		List<Espacio> espaciosResultantes = new ArrayList<Espacio>();
		List<Integer> numEq = criterios.cantidadEquipamientos();
		// "espacios" almacena los Espacio que cumplen los criterios de equipamientos
		List<Espacio> espacios = espaciosRepository
				.findByPlazasGreaterThanEqualAndCanyonGreaterThanEqualAndProyectorGreaterThanEqualAndSonidoGreaterThanEqualAndTvGreaterThanEqualAndVideoGreaterThanEqualAndDvdGreaterThanEqualAndFotocopiadorasGreaterThanEqualAndImpresorasGreaterThanEqualAndOrdenadoresGreaterThanEqualAndFaxesGreaterThanEqualAndTelefonosGreaterThanEqualAndPizarraGreaterThanEqualAndExtpolvoGreaterThanEqualAndExtco2(
						criterios.getAforo(), numEq.get(0), numEq.get(1), numEq.get(2), Integer.toString(numEq.get(3)),
						numEq.get(4), numEq.get(5), numEq.get(6), numEq.get(7), numEq.get(8), numEq.get(9),
						numEq.get(10), numEq.get(11), numEq.get(12), numEq.get(13));
		List<Reserva> reservas;
		// Para cada Espacio que cumple los criterios de equipamiento se examinan sus
		// Reserva
		for (Espacio espacio : espacios) {
			miReserva = new Reserva(espacio, criterios.getHorarioRequest().getHorario(), null, null);
			reservas = repositorioReservas.findByEspacio(espacio);
			if(!reservas.isEmpty()){
				// Para cada reserva se comprueba que no colisione con la búsqueda
				for (Reserva reserva : reservas) {
					// Si hay colisión no se agrega a los Espacio resultantes
					if (!miReserva.hayColision(reserva)) {
						espaciosResultantes.add(espacio);
					}
				}
			}
			else{
				espaciosResultantes.add(espacio);
			}
		}
		// Los Espacio resultantes se transforman a EspacioDTO
		List<EspacioDTO> espaciosResultantesDTO = new ArrayList<EspacioDTO>();
		for (Espacio e : espaciosResultantes) {
			espaciosResultantesDTO.add(new EspacioDTO(e));
		}
		return espaciosResultantesDTO;
	}

	/**
	 * Devuelve true si y solo si ha modificado con exito el equipamiento de un Espacio.
	 */
	public boolean setEquipamiento(CriteriosBusquedaDTO cambios) {
		List<Integer> numEq = cambios.cantidadEquipamientos();
		String nombreEspacio = "\"" + cambios.getNombre() + "\"";
		System.out.println("NOMBRE ESPACIO: ");
		System.out.println(nombreEspacio);
		int i = espaciosRepository.establecerEquipamiento(nombreEspacio, numEq.get(0), numEq.get(1), numEq.get(2),
				Integer.toString(numEq.get(3)), numEq.get(4), numEq.get(5), numEq.get(6), numEq.get(7), numEq.get(8),
				numEq.get(9), numEq.get(10), numEq.get(11), numEq.get(12), numEq.get(13));
		return i > 0;
	}

	/**
	 * Dada la planta de un Espacio devuelve la lista de Espacios alquilables en formato EspacioDTO
	 * 		disponibles en la planta.
	 */
	public List<EspacioDTO> getEspaciosAlquilables(int planta) {
		List<EspacioDTO> listaEspaciosDTO = new ArrayList<EspacioDTO>();
		List<String> listaDeIds = espaciosRepository.findEspaciosAlquilables(planta);
		for (String id : listaDeIds) {
			Optional<Espacio> espacio = espaciosRepository.findById(id);
			EspacioDTO espacioDTO = new EspacioDTO(espacio.get());
			listaEspaciosDTO.add(espacioDTO);
		}
		return listaEspaciosDTO;
	}

	/**
	 * Dada una lista de Espacios en formato EspacioDTO devuelve una lista de igual formato
	 * 		con los Espacios alquilables.
	 */
	public List<EspacioDTO> getEspaciosAlquilables(List<EspacioDTO> espacios) {
		List<EspacioDTO> listaEspaciosDTO = new ArrayList<EspacioDTO>();
		for (EspacioDTO espacio : espacios) {
			if (espacio.esAlquilable()) {
				listaEspaciosDTO.add(espacio);
			}
		}
		return listaEspaciosDTO;
	}

	/**
	 * Dado el identificador de un Espacio devuelve la tarifa en función de sus caracteríristicas.
	 */
	public double calcularTarifaEspacioAlquilable(String id) {
		Optional<Espacio> espacio = espaciosRepository.findById(id);
		if (espacio.isPresent()) {
			return espacio.get().calcularTarifa();
		} else {
			return 0;
		}
	}
}
