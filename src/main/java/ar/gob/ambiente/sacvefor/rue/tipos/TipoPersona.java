package ar.gob.ambiente.sacvefor.rue.tipos;

/**
 * Enum que guarda los tipos de personas:
 * Física o Jurídica
 * @author rincostante
 */
public enum TipoPersona {
    JURIDICA    ("Persona Jurídica"),
    FISICA      ("Persona Física");

    private final String nombre;

    private TipoPersona(String nombre){
        this.nombre=nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString(){
        return nombre;
    }
}