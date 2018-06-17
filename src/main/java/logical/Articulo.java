package logical;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Articulo {
    private long id;
    private String titulo;
    private String cuerpo;
    private Usuario autor;
    private Date fecha;
    private List<Comentario> listaComentarios;
    private List<Etiqueta> listaEtiquetas;

    public Articulo() {
        this.listaComentarios = new ArrayList<Comentario>();
        this.listaEtiquetas = new ArrayList<Etiqueta>();

    }

    public Articulo(String titulo, String cuerpo, Usuario autor, Date fecha, List<Comentario> listaComentarios, List<Etiqueta> listaEtiquetas) {
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

    public List<Comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    public List<Etiqueta> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(List<Etiqueta> listaEtiquetas) {
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
        for (int i = 0; i < listaEtiquetas.size();i++) {
            System.out.println("Etiqueta " + listaEtiquetas.get(i));
            etiquetasString+= listaEtiquetas.get(i);
            if (i < listaEtiquetas.size() -1){
                etiquetasString += ", ";
            }
        }

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
}
