package servicios;

import logical.Articulo;

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
        Query query = em.createQuery("select e from Articulo e order by e.FECHA DESC LIMIT 5 OFFSET :inicio");
        query.setParameter("inicio", i-1);
        List<Articulo> lista = query.getResultList();
        return lista;
    }

    public List<Articulo> findByTag(String tag){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select a from Articulo a, Articulo_Etiqueta ae, Etiqueta e where ae.LISTAARTICULO_ARTICULO_ID = a.id AND ae.LISTAETIQUETAS_ETIQUETA_ID = e.id AND e.etiqueta = :tag order by a.FECHA DESC");
        query.setParameter("tag", tag);
        List<Articulo> lista = query.getResultList();
        return lista;
    }


}
