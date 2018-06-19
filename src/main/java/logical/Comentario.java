package logical;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Comentario implements Serializable{
    @Id
    @GeneratedValue
    @Column(name="COMENTARIO_ID")
    private long id;
    private String comentario;
    @ManyToOne
    private Usuario autor;
    @ManyToOne
    private Articulo articulo;

    public Comentario() {

    }

    public Comentario(String comentario, Usuario autor, Articulo articulo) {
        this.comentario = comentario;
        this.autor = autor;
        this.articulo = articulo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}
