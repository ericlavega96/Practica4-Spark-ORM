package servicios;

import logical.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ServiciosUsuarios extends MetodosDB<Usuario>{

    private static ServiciosUsuarios instancia;

    private ServiciosUsuarios() {super(Usuario.class);}

    public static ServiciosUsuarios getInstancia(){
        if(instancia==null){
            instancia = new ServiciosUsuarios();
        }
        return instancia;
    }

    public Usuario findByUsernameAndPassword(String username, String password){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Usuario u where u.username = :username AND u.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        Usuario resultado = (Usuario)query.getSingleResult();
        return resultado;

    }

    public boolean existAdmin(){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Usuario u where u.administrador = true");
        if(query.getResultList().size() == 0)
            return false;
        else
            return true;

    }

    public boolean crearAdmin(){
        if(getInstancia().existAdmin())
            return false;
        else{
            getInstancia().crear(new Usuario("admin","Admin","1234",true,false));
            return true;
        }
    }
}
