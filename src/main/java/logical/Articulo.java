package logical;

import com.sun.istack.internal.NotNull;
import servicios.ServiciosArticulos;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.*;

@Entity
public class Articulo implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="ARTICULO_ID")
    private long id;
    @NotNull
    @Column(unique = true)
    private String titulo;
    @NotNull
    @Lob
    @Column(unique = true)
    private String cuerpo;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "AUTOR_ID")
    private Usuario autor;
    @NotNull
    private Date fecha;
    @OneToMany(mappedBy = "articulo",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Comentario> listaComentarios;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ARTICULO_ETIQUETA", joinColumns = { @JoinColumn(name = "ARTICULO_ID") }, inverseJoinColumns = { @JoinColumn(name = "ETIQUETA_ID") })
    private Set<Etiqueta> listaEtiquetas;
    @OneToMany(mappedBy = "articulo", cascade = CascadeType.ALL)
    Set<LikesArticulo> likeArticulo = new HashSet<>();

    public Articulo() {
    }

    public Articulo(String titulo, String cuerpo, Usuario autor, Date fecha, Set<Comentario> listaComentarios, Set<Etiqueta> listaEtiquetas) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = fecha;
        this.listaComentarios = listaComentarios;
        this.listaEtiquetas = listaEtiquetas;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Set<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(Set<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    public Set<Etiqueta> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(Set<Etiqueta> listaEtiquetas) {
        this.listaEtiquetas = listaEtiquetas;
    }

    public String textoResumido(){
        if(cuerpo.length() > 70){
           return cuerpo.substring(0, 70)+"...";
        }
        else
            return cuerpo;
    }

    public String fechaText(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        int year = cal.get(Calendar.YEAR);
        String month = nombreMes(cal.get(Calendar.MONTH));
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return month + " " + day + ", " + year;
    }

    public String etiquetasString(){
        String etiquetasString = "";
        List<Etiqueta> list = new ArrayList<>(listaEtiquetas);
        for (int i=0; i < list.size() - 1;i++ ){
            etiquetasString += list.get(i).getEtiqueta() + ",";
        }
        etiquetasString += list.get(listaEtiquetas.size() - 1).getEtiqueta();
        return etiquetasString;
    }

    private String nombreMes(int mes){
        String nombreMes;
        switch (mes+1) {
            case 1:  nombreMes = "Enero";
                break;
            case 2:  nombreMes = "Febrero";
                break;
            case 3:  nombreMes = "Marzo";
                break;
            case 4:  nombreMes = "Abril";
                break;
            case 5:  nombreMes = "Mayo";
                break;
            case 6:  nombreMes = "Junio";
                break;
            case 7:  nombreMes = "Julio";
                break;
            case 8:  nombreMes = "Agosto";
                break;
            case 9:  nombreMes = "Septiembre";
                break;
            case 10: nombreMes = "Octubre";
                break;
            case 11: nombreMes = "Noviembre";
                break;
            case 12: nombreMes = "Diciembre";
                break;
            default: nombreMes = "Mes no Valido!";
                break;
        }
        return nombreMes;
    }

    public long likesCount(){return ServiciosArticulos.getInstancia().getLikesCount(this);}

    public long dislikesCount(){return ServiciosArticulos.getInstancia().getDislikesCount(this);}
}
