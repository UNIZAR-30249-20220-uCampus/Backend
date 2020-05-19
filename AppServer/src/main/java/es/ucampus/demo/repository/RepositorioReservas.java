package es.ucampus.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;
import domainObjects.valueObject.EstadoReserva;


@Repository
public interface RepositorioReservas extends JpaRepository<Reserva, Long>{
	
	List<Reserva> findByEspacio(Espacio espacio);
	List<Reserva> findByUsuario(String usuario);

	/*
		@Query(value="SELECT * FROM public.reserva as reserva WHERE reserva.espacio_id_espacio = :espacio AND reserva.estado = CAST(:estado AS character varying)", nativeQuery = true)
		List<Reserva> findByEspacioAndEstado(@Param ("espacio") Espacio espacio, @Param ("estado") EstadoReserva estado);
	*/
	
}