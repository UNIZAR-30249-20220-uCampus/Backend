package es.ucampus.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;


@Repository
public interface RepositorioReservas extends JpaRepository<Reserva, Long>{
	
	List<Reserva> findByEspacio(Espacio espacio);

	List<Reserva> findByUsuario(String usuario);
	
}