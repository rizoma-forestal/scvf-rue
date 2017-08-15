package ar.gob.ambiente.sacvefor.rue.tipos;

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