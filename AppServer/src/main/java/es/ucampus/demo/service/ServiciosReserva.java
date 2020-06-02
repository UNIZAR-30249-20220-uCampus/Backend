package es.ucampus.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import domainObjects.entity.Espacio;
import domainObjects.entity.Reserva;
import domainObjects.valueObject.EstadoReserva;
import dtoObjects.entity.ReservaDTO;

@Service
public interface ServiciosReserva {

    /**
     * Dada una reserva, devuelve true si y solo si no hay ningún tipo de problema de colisiones
     *      con las demás reservas almacenadas para ese mismo espacio.
     * @param reserva
     * @return boolean
     */
    boolean hacerReserva(Reserva reserva);

    /**
     * Dado un identificador de reserva, devuelve true si y solo si el proceso de aceptar la reserva
     *      termina de manera satisfactoria. Si esto ocurre, se comprueban todas las reservas pendientes
     *      y se cancelan las que colisionan con la reserva recien aceptada.
     * @param idReserva
     * @return boolean
     */
    boolean aceptarReserva(Long idReserva);

    /**
     * Dado un identificador de reserva, devuelve true si y solo si el proceso de pago termina de manera
     *      satisfactoria.
     * @param idReserva
     * @return boolean
     */
    boolean pagarReserva(Long idReserva);
    
    /**
     * Dado un identificador de reserva, devuelve true si y solo si el proceso de cancelar una reserva
     * termina de manera satisfactoria
     * @param idReserva
     * @return boolean
     */
    boolean cancelarReserva(Long idReserva);

    /**
     * Dado un espacio, devuelve una lista de Reservas en formato ReservaDTO con las reservas
     *      asociadas al espacio.
     * @param espacio
     * @return List<ReservaDTO>
     */
    List<ReservaDTO> buscarReserva(Espacio espacio);

    /**
     * Dado un Espacio y un EstadoReserva, devuelve una lista de Reservas con el estado correspondiente
     *      en formato ReservaDTO asociadas al espacio 
     * @param espacio
     * @param estado
     * @return List<ReservaDTO>
     */
    List<ReservaDTO> buscarReservaEstado(Espacio espacio, EstadoReserva estado);

    /**
     * Dado un usuario, devuelve una lista de Reservas en formato ReservaDTO asociadas al usuario
     * @param usuario
     * @return List<ReservaDTO>
     */
    List<ReservaDTO> buscarReservaUsuario(String usuario);

    /**
     * Dado un usuario y un EstadoReserva, devuelve una lista de Reservas en formato ReservaDTO con el
     *      estado correspondiente asociadas al usuario.
     * @param usuario
     * @param estado
     * @return
     */
    List<ReservaDTO> buscarReservaUsuarioEstado(String usuario, EstadoReserva estado);

}
