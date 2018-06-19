package servicios;

import logical.Articulo;
import org.h2.tools.Server;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServiciosBootStrap {

    /**
     *
     * @throws SQLException
     */
    private static ServiciosBootStrap instancia;

    private ServiciosBootStrap(){

    }

    public static ServiciosBootStrap getInstancia(){
        if(instancia == null){
            instancia=new ServiciosBootStrap();
        }
        return instancia;
    }

    public void iniciarBD() {
        try {
            Server.createTcpServer("-tcpPort",
                    "9092",
                    "-tcpAllowOthers",
                    "-tcpDaemon").start();
        }catch (SQLException ex){
            System.out.println("Problema con la base de datos: "+ex.getMessage());
        }
    }

    public void init(){
        iniciarBD();
    }

}