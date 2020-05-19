package es.ucampus.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;
import domainObjects.valueObject.EstadoReserva;
import dtoObjects.entity.ReservaDTO;

@Service
public interface FuncionesReserva {

    boolean hacerReserva(Reserva reserva);
    boolean aceptarReserva(Long idReserva);
    boolean cancelarReserva(Long idReserva);
    List<ReservaDTO> buscarReserva(Espacio espacio);
    List<ReservaDTO> buscarReservaEstado(Espacio espacio, EstadoReserva estado);
    List<ReservaDTO> buscarReservaUsuario(String usuario);
}
