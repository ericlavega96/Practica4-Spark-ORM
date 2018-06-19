package servicios;

import logical.Comentario;

public class ServiciosComentarios extends MetodosDB<Comentario>{
    private static ServiciosComentarios instancia;

    private ServiciosComentarios() {super(Comentario.class);}

    public static ServiciosComentarios getInstancia(){
        if(instancia==null){
            instancia = new ServiciosComentarios();
        }
        return instancia;
    }
}
