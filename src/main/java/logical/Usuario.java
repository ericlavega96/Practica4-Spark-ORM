package logical;

import com.sun.istack.internal.NotNull;
import servicios.ServiciosArticulos;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Usuario implements Serializable{
    @Id
    @Column(name="USUARIO_ID")
    private String username;
    @NotNull
    private String nombre;
    @NotNull
    private String password;

    private boolean administrador;

    private boolean autor;

    @OneToMany(mappedBy = "autor")
    private Set<Comentario> comentarios;

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    Set<LikesArticulo> likeArticulo = new HashSet<>();

    public Usuario() {
    }

    public Usuario(String username, String nombre, String password, boolean administrador, boolean autor) {
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.administrador = administrador;
        this.autor = autor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public boolean isAutor() {
        return autor;
    }

    public void setAutor(boolean autor) {
        this.autor = autor;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Set<LikesArticulo> getLikeArticulo() {
        return likeArticulo;
    }

    public void setLikeArticulo(Set<LikesArticulo> likeArticulo) {
        this.likeArticulo = likeArticulo;
    }

    public boolean likeArticulo(Articulo articulo){
        return ServiciosArticulos.getInstancia().isLiked(this, articulo);
    }

    public boolean dislikeArticulo(Articulo articulo){
        return ServiciosArticulos.getInstancia().isDisliked(this, articulo);
    }
}
