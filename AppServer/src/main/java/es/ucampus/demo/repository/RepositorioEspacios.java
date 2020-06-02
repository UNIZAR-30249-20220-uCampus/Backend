package es.ucampus.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domainObjects.entity.Espacio;

import javax.transaction.Transactional;

@Repository
public interface RepositorioEspacios extends JpaRepository<Espacio, String>{

	@Query(value="SELECT id_espacio FROM public.espaciosgeneral as espacio WHERE espacio.planta = :planta AND ST_Contains(espacio.geom, ST_SetSRID(ST_Point(:x, :y),25830))", nativeQuery = true)
    String findByCoordenadas(@Param ("planta") int planta, @Param ("x") double x, @Param ("y") double y);

    List<Espacio> findByPlazasGreaterThanEqual(int nmro_plazas);

    List<Espacio> findByPlazasGreaterThanEqualAndCanyonGreaterThanEqualAndProyectorGreaterThanEqualAndSonidoGreaterThanEqualAndTvGreaterThanEqualAndVideoGreaterThanEqualAndDvdGreaterThanEqualAndFotocopiadorasGreaterThanEqualAndImpresorasGreaterThanEqualAndOrdenadoresGreaterThanEqualAndFaxesGreaterThanEqualAndTelefonosGreaterThanEqualAndPizarraGreaterThanEqualAndExtpolvoGreaterThanEqualAndExtco2(
        int plazas, int canyon, int proyector, int sonido, String tv, int video, int dvd, int fotocopiadora, int impresora, int ord, int fax, int tlf, int pizarra, int extpol, int extco2);

    @Transactional
    @Modifying
    @Query(value = "UPDATE public.espaciosgeneral SET canyon_fijo = :canyon, pantalla_proyector= :proyector, equipo_de_sonido= :sonido, tv= :tv, video= :video, dvd= :dvd, fotocopiadoras= :fotocopiadora, impresoras= :impresora, ordenadores= :ord, faxes= :fax, telefonos= :tlf, pizarra= :pizarra, nmro_extintores_polvo= :extpol, nmro_extintores_co2= :extco2 WHERE id_espacio = :espacio", nativeQuery = true)
    int establecerEquipamiento(@Param("espacio") String espacio, @Param("canyon") int canyon,
            @Param("proyector") int proyector, @Param("sonido") int sonido, @Param("tv") String tv,
            @Param("video") int video, @Param("dvd") int dvd, @Param("fotocopiadora") int fotocopiadora,
            @Param("impresora") int impresora, @Param("ord") int ord, @Param("fax") int fax, @Param("tlf") int tlf,
            @Param("pizarra") int pizarra, @Param("extpol") int extpol, @Param("extco2") int extco2);

	//Devuelve los espacios que son alquilables en la planta "planta"
	@Query(value="SELECT id_espacio FROM public.espaciosgeneral as espacio WHERE espacio.planta = :planta AND espacio.alquilable = 1", nativeQuery = true)
	List<String> findEspaciosAlquilables(@Param ("planta") int planta);
}