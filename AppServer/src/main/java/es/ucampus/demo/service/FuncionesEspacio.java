package es.ucampus.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import domainObjects.entity.Espacio;
import domainObjects.request.HorarioRequest;
import dtoObjects.entity.EspacioDTO;
import dtoObjects.valueObject.CriteriosBusquedaDTO;
import es.ucampus.demo.repository.RepositorioEspacios;

@Service
public interface FuncionesEspacio {

	EspacioDTO getEspacioDTOId(String id);

	Espacio getEspacioId(String id);

	EspacioDTO getEspacioCoordenadas(int planta, double x, double y);

	List<EspacioDTO> buscarEspacioPorAforo(int aforo);

	List<EspacioDTO> buscarEspacioPorCriterios(CriteriosBusquedaDTO criterios);

	boolean setEquipamiento(CriteriosBusquedaDTO cambios);

	List<EspacioDTO> getEspaciosAlquilables(int planta);

	public double calcularTarifaEspacioAlquilable(String id);

	public List<EspacioDTO> buscarEspaciosporCriteriosYHorario(CriteriosBusquedaDTO criterios, HorarioRequest horario);
}
