package logical;

public class Etiqueta {
    private long id;
    private String etiqueta;

    public Etiqueta() {

    }

    public Etiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String tagsTransform(){
        return etiqueta.substring(0,1).toUpperCase() + etiqueta.substring(1).toLowerCase();
    }
}
