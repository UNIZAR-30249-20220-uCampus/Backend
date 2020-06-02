package es.ucampus.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import domainObjects.entity.Espacio;
import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;

@Service
public interface ServiciosEspacio {

	/**
	 * Dado un identificador devuelve su Espacio correspondiente en formato EspacioDTO.
	 * Devuelve null si no existe.
	 */
	EspacioDTO getEspacioDTOId(String id);

	/**
	 * Dado un identificador devuelve su Espacio correspondiente
	 * Devuelve null si no existe.
	 */
	Espacio getEspacioId(String id);

	/**
	 * Dada una planta y las coordenadas 'x' e 'y' devuelve el Espacio correspondiente
	 * 		en formato EspacioDTO.
	 * Devuelve null si no existe.
	 */
	EspacioDTO getEspacioCoordenadas(int planta, double x, double y);

	/**
	 * Dado un aforo minimo devuelve una lista de Espacios en formato EspacioDTO con
	 * 		un aforo igual o superior al requerido.
	 */
	List<EspacioDTO> buscarEspacioPorAforo(int aforo);

	/**
	 * Dados unos criterios de búsqueda devuelve una lista de Espacios en formato EspacioDTO
	 * 		que cumplen con los requisitos.
	 */
	List<EspacioDTO> buscarEspacioPorCriterios(CriteriosBusquedaDTO criterios);

	/**
	 * Dados unos criterios de búsqueda y horario devuelve una lista de Espacios en formato EspacioDTO
	 * 		que cumplen con estos requisitos y tiene el horario disponible.
	 */
	List<EspacioDTO> buscarEspaciosPorCriteriosYHorario(CriteriosBusquedaDTO criterios);

	/**
	 * Devuelve true si y solo si ha modificado con exito el equipamiento de un Espacio.
	 */
	boolean setEquipamiento(CriteriosBusquedaDTO cambios);

	/**
	 * Dada la planta de un Espacio devuelve la lista de Espacios alquilables en formato EspacioDTO
	 * 		disponibles en la planta.
	 */
	List<EspacioDTO> getEspaciosAlquilables(int planta);

	/**
	 * Dada una lista de Espacios en formato EspacioDTO devuelve una lista de igual formato
	 * 		con los Espacios alquilables.
	 */
	List<EspacioDTO> getEspaciosAlquilables(List<EspacioDTO> espacios);

	/**
	 * Dado el identificador de un Espacio devuelve la tarifa en función de sus caracteríristicas.
	 */
	double calcularTarifaEspacioAlquilable(String id);
}
