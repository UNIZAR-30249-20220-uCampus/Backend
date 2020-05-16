package domainObjects.valueObject;


public enum TipoEquipamiento
{
    CANYON_FIJO ("CAYON_FIJO"),
    PANTALLA_PROYECTOR ("PANTALLA_PROYECTOR"),
    EQUIPO_DE_SONIDO ("EQUIPO_DE_SONIDO"),
    TV("TV"),
    VIDEO("VIDEO"),
    DVD("DVD"),
    FOTOCOPIADORAS ("FOTOCOPIADORAS"),
    IMPRESORAS ("IMPRESORAS"),
    ORDENADORES ("ORDENADORES"),
    FAXES ("FAXES"),
    TELEFONOS ("TELEFONOS"),
    PIZARRA("PIZARRA"),
    NMRO_EXTINTORES_POLVO ("NMRO_EXTINTORES_POLVO"),
    NMRO_EXTINTORES_CO2("NMRO_EXTINTORES_CO2"),
    NMRO_PLAZAS ("NMRO_PLAZAS");

    private final String name;       

    private TipoEquipamiento(String s) {
        name = s;
    }

    public String toString() {
       return this.name;
    }
}