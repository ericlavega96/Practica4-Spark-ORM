package logical;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Etiqueta implements Serializable{
    @Id
    @GeneratedValue
    @Column(name="ETIQUETA_ID")
    private long id;
    @NotNull
    private String etiqueta;

    public Etiqueta() {

    }

    @ManyToMany(mappedBy = "listaEtiquetas")
    private Set<Articulo> listaArticulo;

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
