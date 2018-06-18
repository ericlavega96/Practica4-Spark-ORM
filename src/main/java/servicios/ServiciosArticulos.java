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
    /*public List<Articulo> findAllByNombre(String nombre){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Articulo e where e.nombre like :nombre");
        query.setParameter("nombre", nombre+"%");
        List<Articulo> lista = query.getResultList();
        return lista;
    }*/


}
