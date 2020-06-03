package es.ucampus.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;
import domainObjects.valueObject.EstadoReserva;
import dtoObjects.entity.ReservaDTO;
import es.ucampus.demo.repository.RepositorioReservas;

@Service
@Transactional
public class ServiciosReservaImpl implements ServiciosReserva {

	@Autowired
	private RepositorioReservas repositorioReservas;

	ServiciosReservaImpl(RepositorioReservas repositorioReservas) {
		this.repositorioReservas = repositorioReservas;
	}
	/**
     * Dada una reserva, devuelve true si y solo si no hay ningún tipo de problema de colisiones
     *      con las demás reservas almacenadas para ese mismo espacio.
     * @param reserva
     * @return boolean
     */
	public boolean hacerReserva(Reserva reserva) {

		List<Reserva> reservas = repositorioReservas.findAll();

		for (Reserva reserva2 : reservas) {
			if (reserva.mismoEspacio(reserva2)) {
				if (reserva.hayColision(reserva2)) {
					return false;
				}
			}
		}

		repositorioReservas.save(reserva);

		return true;
	}

	/**
     * Dado un identificador de reserva, devuelve true si y solo si el proceso de aceptar la reserva
     *      termina de manera satisfactoria. Si esto ocurre, se comprueban todas las reservas pendientes
     *      y se cancelan las que colisionan con la reserva recien aceptada.
     * @param idReserva
     * @return boolean
     */
	public boolean aceptarReserva(Long idReserva) {
		boolean aceptada = false;
		Optional<Reserva> reserva = repositorioReservas.findById(idReserva);
		if (reserva.isPresent()) {
			aceptada = reserva.get().aceptarReserva();
			if (aceptada) {
				repositorioReservas.save(reserva.get());
				List<Reserva> reservas = repositorioReservas.findByEspacio(reserva.get().getEspacio());
				for (Reserva reserva2 : reservas) {
					if (reserva.get().hayColision(reserva2)) {
						reserva2.cancelarReserva();
						repositorioReservas.save(reserva2);
					}
				}
			}
		}

		return aceptada;
	}

	/**
     * Dado un identificador de reserva, devuelve true si y solo si el proceso de pago termina de manera
     *      satisfactoria.
     * @param idReserva
     * @return boolean
     */
	public boolean pagarReserva(Long idReserva) {
		boolean pagada = false;
		Optional<Reserva> reserva = repositorioReservas.findById(idReserva);
		if (reserva.isPresent()) {
			pagada = reserva.get().pagarReserva();
			if (pagada) {
				repositorioReservas.save(reserva.get());
			}
		}
		return pagada;
	}

	/**
     * Dado un identificador de reserva, devuelve true si y solo si el proceso de cancelar una reserva
     * termina de manera satisfactoria
     * @param idReserva
     * @return boolean
     */
	public boolean cancelarReserva(Long idReserva) {
		boolean cancelada = false;
		Optional<Reserva> reserva = repositorioReservas.findById(idReserva);
		if (reserva.isPresent()) {
			cancelada = reserva.get().cancelarReserva();
			if (cancelada) {
				repositorioReservas.save(reserva.get());
			}
		}
		return cancelada;
	}

	/**
     * Dado un espacio, devuelve una lista de Reservas en formato ReservaDTO con las reservas
     *      asociadas al espacio.
     * @param espacio
     * @return List<ReservaDTO>
     */
	public List<ReservaDTO> buscarReserva(Espacio espacio) {
		List<Reserva> reservas = repositorioReservas.findByEspacio(espacio);
		List<ReservaDTO> reservasDTO = new ArrayList<ReservaDTO>();

		for (Reserva reserva : reservas) {
			ReservaDTO reservaDTO = new ReservaDTO(reserva);
			reservasDTO.add(reservaDTO);
		}

		return reservasDTO;
	}

	/**
     * Dado un Espacio y un EstadoReserva, devuelve una lista de Reservas con el estado correspondiente
     *      en formato ReservaDTO asociadas al espacio 
     * @param espacio
     * @param estado
     * @return List<ReservaDTO>
     */
	public List<ReservaDTO> buscarReservaEstado(Espacio espacio, EstadoReserva estado) {
		List<Reserva> reservas = repositorioReservas.findByEspacio(espacio);
		List<ReservaDTO> reservasDTO = new ArrayList<ReservaDTO>();

		for (Reserva reserva : reservas) {
			if (reserva.getEstado().equals(estado)) {
				ReservaDTO reservaDTO = new ReservaDTO(reserva);
				reservasDTO.add(reservaDTO);
			}
		}

		return reservasDTO;
	}

	/**
     * Dado un usuario, devuelve una lista de Reservas en formato ReservaDTO asociadas al usuario
     * @param usuario
     * @return List<ReservaDTO>
     */
	public List<ReservaDTO> buscarReservaUsuario(String usuario) {
		List<Reserva> reservas = repositorioReservas.findByUsuario(usuario);
		List<ReservaDTO> reservasDTO = new ArrayList<ReservaDTO>();

		for (Reserva reserva : reservas) {
			ReservaDTO reservaDTO = new ReservaDTO(reserva);
			reservasDTO.add(reservaDTO);
		}

		return reservasDTO;
	}

	/**
     * Dado un usuario y un EstadoReserva, devuelve una lista de Reservas en formato ReservaDTO con el
     *      estado correspondiente asociadas al usuario.
     * @param usuario
     * @param estado
     * @return
     */
	public List<ReservaDTO> buscarReservaUsuarioEstado(String usuario, EstadoReserva estado) {
		List<Reserva> reservas = repositorioReservas.findByUsuario(usuario);
		List<ReservaDTO> reservasDTO = new ArrayList<ReservaDTO>();

		for (Reserva reserva : reservas) {
			if (reserva.getEstado().equals(estado)) {
				ReservaDTO reservaDTO = new ReservaDTO(reserva);
				reservasDTO.add(reservaDTO);
			}
		}

		return reservasDTO;
	}


}