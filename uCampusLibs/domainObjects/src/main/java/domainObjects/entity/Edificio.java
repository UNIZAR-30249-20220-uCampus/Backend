package domainObjects.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Edificio {

  @Id
  private String id_edificio;
  
  private String direccion;
  private String edificio;

  public Edificio() {
    super();
  }

  public Edificio(String id_edificio, String direccion, String edificio) {
    this.id_edificio = id_edificio;
    this.edificio = edificio;
    this.direccion = direccion;
  }

  @Override
  public String toString() {
    return String.format(
        "Edificio[id=%s, edificio='%s', direccion='%s']", id_edificio, edificio, direccion);
  }
  

}