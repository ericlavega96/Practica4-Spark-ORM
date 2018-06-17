package servicios;

import logical.Articulo;
import logical.Comentario;
import logical.Etiqueta;
import logical.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiciosUsuario {

    public List<Usuario> listaUsuario() {
        List<Usuario> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {

            String query = "select * from usuarios";
            con = ServiciosDataBase.getInstancia().getConexion(); //referencia a la conexion.
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Usuario u = new Usuario();
                u.setUsername(rs.getString("username"));
                u.setNombre(rs.getString("nombre"));
                u.setPassword(rs.getString("password"));
                u.setAdministrador(rs.getBoolean("administrador"));
                u.setAutor(rs.getBoolean("autor"));

                lista.add(u);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lista;
    }

    public List<Articulo> listaArticulos() {
        List<Articulo> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {

            String query = "select * from articulos order by FECHA DESC";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Articulo o = new Articulo();
                o.setId(rs.getLong("id"));
                o.setTitulo(rs.getString("titulo"));
                o.setCuerpo(rs.getString("cuerpo"));
                o.setAutor(buscarUsuario(rs.getString(  "autor")));
                o.setFecha(rs.getDate("fecha"));

                o.setListaEtiquetas(listaEtiquetaArticulo(o));
                o.setListaComentarios(listaComentarios(o));

                lista.add(o);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lista;
    }

    public List<Etiqueta> listaEtiquetaArticulo(Articulo art) {
        List<Etiqueta> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {

            String query = "select e.ID as codigo, e.ETIQUETA as tag from ETIQUETAS e, ARTICULOSETIQUETAS ae where ae.ETIQUETA = e.ID AND ae.ARTICULO = ?";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1, art.getId());
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Etiqueta o = new Etiqueta();
                o.setId(rs.getLong("codigo"));
                o.setEtiqueta(rs.getString("tag"));
                System.out.println("Codigo " + o.getId() + " Etiqueta "+ o.getEtiqueta());

                lista.add(o);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lista;
    }

    public List<Comentario> listaComentarios(Articulo art) {
        List<Comentario> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {

            String query = "select * from COMENTARIOS where ARTICULO = ?";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1, art.getId());
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Comentario o = new Comentario();
                o.setId(rs.getLong("ID"));
                o.setComentario(rs.getString("COMENTARIO"));
                o.setArticulo(buscarArticuloLight(rs.getLong("ARTICULO")));
                o.setAutor(buscarComentador(o));
                lista.add(o);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lista;
    }

    public Usuario buscarComentador(Comentario cmt) {
        Usuario comentador = new Usuario();
        Connection con = null; //objeto conexion.
        try {

            String query = "select u.USERNAME as uname, u.NOMBRE as unombre, u.PASSWORD as upassword, u.ADMINISTRADOR as uadmin, u.AUTOR as uautor from USUARIOS u, COMENTARIOS c where c.ID = ? AND u.USERNAME = c.AUTOR";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1, cmt.getId());
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                comentador.setUsername(rs.getString("uname"));
                comentador.setNombre(rs.getString("unombre"));
                comentador.setPassword(rs.getString("upassword"));
                comentador.setAdministrador(rs.getBoolean("uadmin"));
                comentador.setAutor(rs.getBoolean("uautor"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return comentador;
    }

    public List<Etiqueta> listaEtiquetas() {
        List<Etiqueta> lista = new ArrayList<>();
        Connection con = null; //objeto conexion.
        try {

            String query = "select * from ETIQUETAS";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Etiqueta o = new Etiqueta();
                o.setId(rs.getLong("id"));
                o.setEtiqueta(rs.getString("etiqueta"));

                lista.add(o);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lista;
    }

    public Usuario buscarAutor(Articulo art) {
        Usuario autor = new Usuario();
        Connection con = null; //objeto conexion.
        try {

            String query = "select * from USUARIOS where  AUTOR =?";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1, art.getId());
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                autor.setUsername(rs.getString("USERNAME"));
                autor.setNombre(rs.getString("NOMBRE"));
                autor.setPassword(rs.getString("PASSWORD"));
                autor.setAdministrador(rs.getBoolean("ADMINISTRADOR"));
                autor.setAutor(rs.getBoolean("AUTOR"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return autor;
    }



    public Usuario buscarUsuario(String username, String password) {
        Usuario user = null;

        Connection con = null; //objeto conexion.
        try {

            String query = "select * from USUARIOS where USERNAME = ? AND PASSWORD = ?";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setString(1, username);
            prepareStatement.setString(2, password);

            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                user = new Usuario();
                user.setUsername(rs.getString("USERNAME"));
                user.setNombre(rs.getString("NOMBRE"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setAdministrador(rs.getBoolean("ADMINISTRADOR"));
                user.setAutor(rs.getBoolean("AUTOR"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return user;
    }

    public Usuario buscarUsuario(String username) {
        Usuario user = null;

        Connection con = null; //objeto conexion.
        try {

            String query = "select * from USUARIOS where USERNAME = ?";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setString(1, username);

            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                user = new Usuario();
                user.setUsername(rs.getString("USERNAME"));
                user.setNombre(rs.getString("NOMBRE"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setAdministrador(rs.getBoolean("ADMINISTRADOR"));
                user.setAutor(rs.getBoolean("AUTOR"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return user;
    }

    public boolean crearEtiqueta(Etiqueta o){
        boolean ok =false;
        if(buscarUsuario(o.getEtiqueta()) != null)
            return false;

        Connection con = null;
        try {

            String query = "insert into ETIQUETAS(ETIQUETA) values(?)";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, o.getEtiqueta());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean crearComentario(Comentario o){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "insert into COMENTARIOS(COMENTARIO, AUTOR, ARTICULO) values(?,?,?)";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, o.getComentario());
            prepareStatement.setString(2, o.getAutor().getUsername());
            prepareStatement.setLong(3, o.getArticulo().getId());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean crearUsuario(Usuario o){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "insert into USUARIOS(USERNAME, NOMBRE, PASSWORD, ADMINISTRADOR, AUTOR) values(?,?,?,?,?)";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, o.getUsername());
            prepareStatement.setString(2, o.getNombre());
            prepareStatement.setString(3, o.getPassword());
            prepareStatement.setBoolean(4, o.isAdministrador());
            prepareStatement.setBoolean(5, o.isAutor());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean crearArticulo(Articulo o){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "insert into ARTICULOS(TITULO, CUERPO, AUTOR,FECHA) values(?,?,?,?)";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, o.getTitulo());
            prepareStatement.setString(2, o.getCuerpo());
            prepareStatement.setString(3, o.getAutor().getUsername());
            prepareStatement.setDate(4,new java.sql.Date(o.getFecha().getTime()));

            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for(Etiqueta e : o.getListaEtiquetas()){
            crearArticuloEtiqueta(o, e);
        }

        return ok;
    }

    public boolean crearArticuloEtiqueta(Articulo o, Etiqueta e){
        boolean ok = false;
        crearEtiqueta(e);
        Connection con = null;
        try {

            String query = "insert into ARTICULOSETIQUETAS(ARTICULO, ETIQUETA) values(?,?)";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setLong(1, buscarArticulo(o.getTitulo()).getId());
            prepareStatement.setLong(2, buscarEtiqueta(e.getEtiqueta()).getId());

            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean actualizarUsuario(Usuario o){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update USUARIOS set NOMBRE=?, PASSWORD=?, ADMINISTRADOR=?, AUTOR=? where USERNAME= ?";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, o.getNombre());
            prepareStatement.setString(2, o.getPassword());
            prepareStatement.setBoolean(3, o.isAdministrador());
            prepareStatement.setBoolean(4, o.isAutor());
            //Indica el where...
            prepareStatement.setString(5, o.getUsername());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean actualizarArticulo(Articulo o){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "update ARTICULOS set TITULO=?, CUERPO=?, AUTOR=?,  where ID= ?";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, o.getTitulo());
            prepareStatement.setString(2, o.getCuerpo());
            prepareStatement.setString(3, o.getAutor().getUsername());
            //Indica el where...
            prepareStatement.setLong(4, o.getId());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarUsuario(String username){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "delete from USUARIOS where USERNAME = ?";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setString(1, username);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarArticulo(long key){
        boolean ok = false;

        Connection con = null;
        try {
            borrarArticuloEtiqueta(key);
            borrarComentariosArticulo(key);

            String query = "delete from ARTICULOS where ID = ?";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setLong(1, key);
            //

            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarComentario(long key){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "delete from COMENTARIOS where ID = ?";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setLong(1, key);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarEtiqueta(long key){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "delete from ETIQUETAS where ID = ?";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setLong(1, key);

            borrarEtiquetaArticulo(key);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;


        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarArticuloEtiqueta(long key){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "delete from ARTICULOSETIQUETAS where ARTICULO = ?";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setLong(1, key);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarEtiquetaArticulo(long key){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "delete from ARTICULOSETIQUETAS where ETIQUETA = ?";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setLong(1, key);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarComentariosArticulo(long key){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "delete from COMENTARIOS where ARTICULO = ?";
            con = ServiciosDataBase.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setLong(1, key);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean crearAdmin(){

        boolean ok = false;
        Connection con = null;

        if(!buscarAdmin()){
            try {

                String query = "insert into USUARIOS(USERNAME, NOMBRE, PASSWORD, ADMINISTRADOR, AUTOR) values(?,?,?,?,?)";
                con = ServiciosDataBase.getInstancia().getConexion();
                //
                PreparedStatement prepareStatement = con.prepareStatement(query);
                //Antes de ejecutar seteo los parametros.
                prepareStatement.setString(1, "admin");
                prepareStatement.setString(2, "admin");
                prepareStatement.setString(3, "1234");
                prepareStatement.setBoolean(4, true);
                prepareStatement.setBoolean(5, false);
                //
                int fila = prepareStatement.executeUpdate();
                ok = fila > 0 ;

            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            } finally{
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return ok;
    }

    public boolean buscarAdmin() {
        boolean existe = false;

        Connection con = null; //objeto conexion.
        try {

            String query = "select * from USUARIOS where ADMINISTRADOR = TRUE";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                existe = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return existe;
    }

    public Etiqueta buscarEtiqueta(String etiqueta) {
        Etiqueta tag = null;

        Connection con = null; //objeto conexion.
        try {

            String query = "select * from ETIQUETAS where ETIQUETA = ?";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setString(1, etiqueta);

            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                tag = new Etiqueta();
                tag.setId(rs.getLong("ID"));
                tag.setEtiqueta(rs.getString("ETIQUETA"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return tag;
    }

    public Articulo buscarArticulo(String titulo) {
        Articulo articulo= null;

        Connection con = null; //objeto conexion.
        try {

            String query = "select * from ARTICULOS where TITULO = ?";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setString(1, titulo);

            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                articulo = new Articulo();
                articulo.setId(rs.getLong("ID"));
                articulo.setTitulo(rs.getString("TITULO"));
                articulo.setCuerpo(rs.getString("CUERPO"));
                articulo.setAutor(buscarUsuario(rs.getString("AUTOR")));
                articulo.setFecha(rs.getDate("fecha"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return articulo;
    }

    public Articulo buscarArticulo(Long id) {
        Articulo articulo= null;

        Connection con = null; //objeto conexion.
        try {

            String query = "select * from ARTICULOS where ID = ?";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1, id);

            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                articulo = new Articulo();
                articulo.setId(rs.getLong("ID"));
                articulo.setTitulo(rs.getString("TITULO"));
                articulo.setCuerpo(rs.getString("CUERPO"));
                articulo.setAutor(buscarUsuario(rs.getString("AUTOR")));
                articulo.setFecha(rs.getDate("FECHA"));
                articulo.setListaComentarios(listaComentarios(articulo));
                articulo.setListaEtiquetas(listaEtiquetaArticulo(articulo));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return articulo;
    }

    public Articulo buscarArticuloLight(Long id) {
        Articulo articulo= null;

        Connection con = null; //objeto conexion.
        try {

            String query = "select * from ARTICULOS where ID = ?";
            con = ServiciosDataBase.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setLong(1, id);

            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                articulo = new Articulo();
                articulo.setId(rs.getLong("ID"));
                articulo.setTitulo(rs.getString("TITULO"));
                articulo.setCuerpo(rs.getString("CUERPO"));
                articulo.setAutor(buscarUsuario(rs.getString("AUTOR")));
                articulo.setFecha(rs.getDate("FECHA"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ServiciosUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return articulo;
    }


}
