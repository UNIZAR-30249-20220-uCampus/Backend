package es.ucampus.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;

@Repository
public interface RepositorioReservas extends JpaRepository<Reserva, Long>{
	/**
	 * Dado un espacio devuelve la lista de reservas asociadas con ese espacio
	 * @param espacio
	 * @return List<Reserva>
	 */
	List<Reserva> findByEspacio(Espacio espacio);
	/**
	 * Dado un usuario devuelve la lista de reservas asociadas con el usuario.
	 * @param usuario
	 * @return List<Reserva>
	 */
	List<Reserva> findByUsuario(String usuario);
	
}