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

    /**
     *
     * @param nombre
     * @return
     */
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
        Query query = em.createQuery("select a from Articulo a");

        //SELECT t.tokenName FROM Role r JOIN r.tkns t WHERE r.roleId = :roleId

        //select a from Articulo a, Articulo_Etiqueta ae, Etiqueta e where ae.LISTAARTICULO_ARTICULO_ID = a.id AND ae.LISTAETIQUETAS_ETIQUETA_ID = e.id AND e.etiqueta = :tag order by a.FECHA DESC

        //query.setParameter("tag", tag);
        List<Articulo> lista = query.getResultList();
        return lista;
    }


}
