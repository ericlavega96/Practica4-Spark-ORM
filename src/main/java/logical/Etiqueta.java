package logical;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Etiqueta implements Serializable{
    @Id
    private long id;
    @NotNull
    @Column(unique = true)
    private String etiqueta;

    public Etiqueta() {

    }

    @ManyToMany(mappedBy = "listaEtiquetas", fetch = FetchType.EAGER) //indicando que la carga será en linea.
    private Set<Articulo> listaArticulo;                               //La dueña de la relación es la clase Clase

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
