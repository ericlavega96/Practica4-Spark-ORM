package servicios;


import logical.Etiqueta;
import logical.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosEtiquetas  extends MetodosDB<Etiqueta>{

    private static ServiciosEtiquetas instancia;

    private ServiciosEtiquetas(){super(Etiqueta.class);}

    public static ServiciosEtiquetas getInstancia(){
        if(instancia==null){
            instancia = new ServiciosEtiquetas();
        }
        return instancia;
    }




}
