package servicios;

import logical.Articulo;
import logical.Etiqueta;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosArticulos extends MetodosDB<Articulo>{

    private static ServiciosArticulos instancia;

    private ServiciosArticulos() {
        super(Articulo.class);
    }

    public static ServiciosArticulos getInstancia(){
        if(instancia==null){
            instancia = new ServiciosArticulos();
        }
        return instancia;
    }


    public List<Articulo> findAllIndexado(int i){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Articulo e order by e.fecha DESC");
        query.setFirstResult(5*(i-1));
        query.setMaxResults(5);
        List<Articulo> lista = query.getResultList();
        return lista;
    }

    public List<Articulo> findByTag(String tag){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select a from Articulo a JOIN a.listaEtiquetas e WHERE e.etiqueta = :tag");
        query.setParameter("tag", tag);
        List<Articulo> lista = query.getResultList();
        return lista;
    }


}
