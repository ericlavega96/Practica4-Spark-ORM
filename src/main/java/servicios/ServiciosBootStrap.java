package servicios;

import logical.Articulo;
import org.h2.tools.Server;

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
    public static void iniciarBD() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    /**
     *
     * @throws SQLException
     */
    public static void detenetBD() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }


    /**
     * Metodo para crear las tablas necesarias
     * @throws SQLException
     */
    public static void crearTablas() throws  SQLException{

        String tablaUsuarios = "CREATE TABLE IF NOT EXISTS Usuarios\n" +
                "  (\n" +
                "    username VARCHAR(1000) PRIMARY KEY,\n" +
                "    nombre VARCHAR(1000) NOT NULL,\n" +
                "    password VARCHAR(1000) NOT NULL,\n" +
                "    administrador boolean,\n" +
                "    autor boolean\n" +
                "  );";

        String tablaArticulos = "CREATE TABLE IF NOT EXISTS Articulos\n" +
                    "  (\n" +
                    "    id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    titulo VARCHAR(100) UNIQUE NOT NULL,\n" +
                    "    cuerpo CLOB NOT NULL,\n" +
                    "    autor VARCHAR(1000) NOT NULL,\n" +
                    "    fecha date,\n" +
                    "    FOREIGN KEY (autor) REFERENCES Usuarios(username)\n" +
                    "  );";

            String tablaComentarios = "CREATE TABLE IF NOT EXISTS Comentarios\n" +
                    "  (\n" +
                    "    id bigint auto_increment PRIMARY KEY,\n" +
                    "    comentario VARCHAR(1000),\n" +
                    "    autor VARCHAR(1000),\n" +
                    "    articulo bigint,\n" +
                    "    FOREIGN KEY (autor) REFERENCES Usuarios(username),\n" +
                    "    FOREIGN KEY (articulo) REFERENCES Articulos(id)\n" +
                    "  );";

            String tablaEtiquetas = "CREATE TABLE IF NOT EXISTS Etiquetas\n" +
                    "  (\n" +
                    "    id bigint auto_increment PRIMARY KEY,\n" +
                    "    etiqueta VARCHAR(1000) UNIQUE NOT NULL \n" +
                    "  );";

            String tablaArticulosEtiquetas = "CREATE TABLE IF NOT EXISTS ArticulosEtiquetas\n" +
                    "  (\n" +
                    "    id bigint auto_increment PRIMARY KEY,\n" +
                    "    articulo bigint NOT NULL,\n" +
                    "    etiqueta bigint NOT NULL,\n" +
                    "    FOREIGN KEY (articulo) REFERENCES Articulos(id),\n" +
                    "    FOREIGN KEY (etiqueta) REFERENCES Etiquetas(id)\n" +
                    "  );";

            Connection con = ServiciosDataBase.getInstancia().getConexion();
            Statement statement = con.createStatement();
            statement.execute(tablaUsuarios + tablaArticulos + tablaComentarios + tablaEtiquetas + tablaArticulosEtiquetas);
            statement.close();
    }

}