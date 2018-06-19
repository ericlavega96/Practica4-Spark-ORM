package logical;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class LikesArticulo implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="LIKESARTICULO_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "ARTICULO_ID")
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @Column(name="isLIKE")
    boolean isLike;

    public LikesArticulo() {
    }

    public LikesArticulo(Articulo articulo, Usuario usuario, boolean isLike) {
        this.articulo = articulo;
        this.usuario = usuario;
        this.isLike = isLike;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean isLike) {
        this.isLike = isLike;
    }
}
