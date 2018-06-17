package servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiciosDataBase {
    private static ServiciosDataBase blogDBInstancia;
    private String URL = "jdbc:h2:tcp://localhost/~/blogDB";


    private  ServiciosDataBase(){
        registrarDriver();
    }

    private void registrarDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiciosDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retornando la instancia.
     * @return
     */
    public static ServiciosDataBase getInstancia(){
        if(blogDBInstancia==null){
            blogDBInstancia = new ServiciosDataBase();
        }
        return blogDBInstancia;
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException ex) {
            Logger.getLogger(ServiciosDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    public void testConexion() {
        getConexion();
        System.out.println("Conexión realizado con exito...");
    }

}
