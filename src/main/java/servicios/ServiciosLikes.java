package servicios;

import logical.Articulo;
import logical.LikesArticulo;
import logical.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosLikes extends MetodosDB<LikesArticulo>{

    private static ServiciosLikes instancia;

    private ServiciosLikes() {
        super(LikesArticulo.class);
    }

    public static ServiciosLikes getInstancia(){
        if(instancia==null){
            instancia = new ServiciosLikes();
        }
        return instancia;
    }

    public void deleteLikes(Articulo articulo, Usuario user){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select la from LikesArticulo la where la.usuario = :user AND la.articulo = :articulo");
        query.setParameter("user", user);
        query.setParameter("articulo", articulo);
        List<LikesArticulo> resultado = query.getResultList();
        for(LikesArticulo la : resultado)
            eliminar(la.getId());
        return;
    }
}
