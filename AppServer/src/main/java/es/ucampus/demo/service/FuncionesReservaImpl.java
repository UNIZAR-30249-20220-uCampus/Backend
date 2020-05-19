package es.ucampus.demo.service;

import java.io.Console;
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
			if(reserva.mismoEspacio(reserva2)){
				if(reserva.hayColision(reserva2)){
					return false;
				}
			}
		}

		repositorioReservas.save(reserva);

		return true;
	}

	public boolean aceptarReserva(Long idReserva){
		boolean aceptar = false;
		Optional<Reserva> reserva = repositorioReservas.findById(idReserva);
		
		if(reserva.isPresent()){
			reserva.get().aceptarReserva();;
			repositorioReservas.save(reserva.get());
			aceptar = true;
		}		

		return aceptar;

	}

	public boolean cancelarReserva(Long idReserva){
		boolean canceled = false;
		Optional<Reserva> reserva = repositorioReservas.findById(idReserva);
		
		if(reserva.isPresent()){
			reserva.get().cancelarReserva();
			repositorioReservas.save(reserva.get());
			canceled = true;
		}		

		return canceled;

	}

	public List<ReservaDTO> buscarReserva(Espacio espacio){
		List<Reserva> reservas = repositorioReservas.findByEspacio(espacio);
		List<ReservaDTO> reservasDTO = new ArrayList<ReservaDTO>();
		
		for (Reserva reserva : reservas) {
			ReservaDTO reservaDTO = new ReservaDTO(reserva);
			reservasDTO.add(reservaDTO);
		}

		return reservasDTO;
	}

	public List<ReservaDTO> buscarReservaEstado(Espacio espacio, EstadoReserva estado){
		List<Reserva> reservas = repositorioReservas.findByEspacio(espacio);
		List<ReservaDTO> reservasDTO = new ArrayList<ReservaDTO>();
		
		for (Reserva reserva : reservas) {
			if(reserva.getEstado().equals(estado)){
				ReservaDTO reservaDTO = new ReservaDTO(reserva);
				reservasDTO.add(reservaDTO);
			}
		}

		return reservasDTO;
	}


	public List<ReservaDTO> buscarReservaUsuario(String usuario){
		List<Reserva> reservas = repositorioReservas.findByUsuario(usuario);
		List<ReservaDTO> reservasDTO = new ArrayList<ReservaDTO>();
		
		for (Reserva reserva : reservas) {
			System.out.println(reservas.toString());

			ReservaDTO reservaDTO = new ReservaDTO(reserva);
			reservasDTO.add(reservaDTO);
		}

		return reservasDTO;
	}

}