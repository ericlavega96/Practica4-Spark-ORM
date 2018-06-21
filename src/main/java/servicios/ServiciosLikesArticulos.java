package servicios;

import logical.LikesArticulo;

public class ServiciosLikesArticulos extends MetodosDB<LikesArticulo>{

    private static ServiciosLikesArticulos instancia;

    private ServiciosLikesArticulos(){super(LikesArticulo.class);}

    public static ServiciosLikesArticulos getInstancia(){
        if(instancia==null){
            instancia = new ServiciosLikesArticulos();
        }
        return instancia;
    }
}
