package servicios;

import logical.Articulo;
import logical.Etiqueta;
import logical.LikesArticulo;
import logical.Usuario;

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

    public List<Articulo> findByTag(String tag, int i){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select a from Articulo a JOIN a.listaEtiquetas e WHERE e.etiqueta = :tag order by a.fecha DESC");
        query.setParameter("tag", tag);
        query.setFirstResult(5*(i-1));
        query.setMaxResults(5);
        List<Articulo> lista = query.getResultList();
        return lista;
    }

    public long getLikesCount(Articulo articulo){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select l from LikesArticulo l WHERE l.articulo = :articulo AND l.isLike = true");
        query.setParameter("articulo", articulo);
        long resultado = query.getResultList().size();
        return resultado;
    }

    public long getDislikesCount(Articulo articulo){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select l from LikesArticulo l WHERE l.articulo = :articulo AND l.isLike = false");
        query.setParameter("articulo", articulo);
        long resultado = query.getResultList().size();
        return resultado;
    }

    public boolean isLiked(Usuario user, Articulo articulo){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select l from LikesArticulo l WHERE l.articulo = :articulo AND l.usuario = :user AND l.isLike = true");
        query.setParameter("articulo", articulo);
        query.setParameter("user", user);
        System.out.println(query.getFirstResult());
        return query.getResultList().size()>0;
    }

    public boolean isDisliked(Usuario user, Articulo articulo){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select l from LikesArticulo l WHERE l.articulo = :articulo AND l.usuario = :user AND l.isLike = false");
        query.setParameter("articulo", articulo);
        query.setParameter("user", user);
        return query.getResultList().size()>0;
    }

    public int countByTag(String tag){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select a from Articulo a JOIN a.listaEtiquetas e WHERE e.etiqueta = :tag order by a.fecha DESC");
        query.setParameter("tag", tag);
        return query.getResultList().size();
    }




}
