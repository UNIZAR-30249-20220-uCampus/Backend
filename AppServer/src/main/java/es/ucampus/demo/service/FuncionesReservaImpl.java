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
public class FuncionesReservaImpl implements FuncionesReserva {

	@Autowired
	private RepositorioReservas repositorioReservas;

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

	public List<ReservaDTO> buscarReserva(Espacio espacio) {
		List<Reserva> reservas = repositorioReservas.findByEspacio(espacio);
		List<ReservaDTO> reservasDTO = new ArrayList<ReservaDTO>();

		for (Reserva reserva : reservas) {
			ReservaDTO reservaDTO = new ReservaDTO(reserva);
			reservasDTO.add(reservaDTO);
		}

		return reservasDTO;
	}

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

	public List<ReservaDTO> buscarReservaUsuario(String usuario) {
		List<Reserva> reservas = repositorioReservas.findByUsuario(usuario);
		List<ReservaDTO> reservasDTO = new ArrayList<ReservaDTO>();

		for (Reserva reserva : reservas) {
			System.out.println(reservas.toString());

			ReservaDTO reservaDTO = new ReservaDTO(reserva);
			reservasDTO.add(reservaDTO);
		}

		return reservasDTO;
	}

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