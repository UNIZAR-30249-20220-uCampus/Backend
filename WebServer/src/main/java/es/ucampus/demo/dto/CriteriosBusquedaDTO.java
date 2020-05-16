package es.ucampus.demo.dto;

import java.io.IOException;
import java.util.List;



public class CriteriosBusquedaDTO {

    private String nombre;
    private int aforo;
    private List<EquipamientoDTO> equipamientos;
    private List<String> filtrosActivos;

    public String getNombre() {
        return nombre;
    }

    public void setId(String nombre) {
        this.nombre = nombre;
    }

    public int getAforo() {
        return aforo;
    }

    public void setAforo(int aforo) {
        this.aforo = aforo;
    }

    public List<EquipamientoDTO> getEquipamientos() {
        return equipamientos;
    }

    public void setEquipamientos(List<EquipamientoDTO> equipamientos) {
        this.equipamientos = equipamientos;
    }

    public List<String> getFiltrosActivos() {
        return filtrosActivos;
    }

    public void setFiltrosActivos(List<String> filtrosActivos) {
        this.filtrosActivos = filtrosActivos;
    }

    @Override
    public String toString() {
        return "CriteriosBusquedaDTO [aforo=" + aforo + ", equipamientos=" + equipamientos + ", filtrosActivos="
                + filtrosActivos + ", nombre=" + nombre + "]";
    }

	

}
