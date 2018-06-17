package logical;


import freemarker.template.Configuration;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import servicios.FiltrosYCookies;
import servicios.ServiciosBootStrap;
import servicios.ServiciosDataBase;

import servicios.ServiciosUsuario;
import spark.ModelAndView;
import spark.Session;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static spark.Spark.*;


public class Main {

    private static List<Usuario> misUsuarios = new ArrayList<>();

    private static String usernameUsuarioActual;


    public static void main(String[] args) throws SQLException {


        staticFiles.location("/templates");

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);
        cfg.setClassForTemplateLoading(Main.class, "/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(cfg);

        new FiltrosYCookies().aplicarFiltros();

        Usuario user = new Usuario("aavgc", "Adonis", "1234", true, false);
        Usuario ericUser = new Usuario("ericlavega96", "Eric", "1234", true, false);

        misUsuarios.add(user);
        misUsuarios.add(ericUser);


        //Pruebas conexion BD modo Server
        ServiciosBootStrap.iniciarBD();

        ServiciosDataBase.getInstancia().testConexion();

        ServiciosBootStrap.crearTablas();

        ServiciosUsuario SU = new ServiciosUsuario();
        SU.crearAdmin();
        //ServiciosBootStrap.detenetBD();

        String prueba = Encryptamiento("prueba");
        System.out.println(prueba);
        prueba = Desencryptamiento(prueba);
        System.out.println(prueba);

        get("/iniciarSesion", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();

            Map<String, String> cookies = request.cookies();
            String salida="";
            for(String key : cookies.keySet())
                salida+=String.format("Cookie %s = %s", key, cookies.get(key));


            attributes.put("titulo", "Iniciar Sesión-Artículos A&E");
            return new ModelAndView(attributes, "login.ftl");
        }, freeMarkerEngine);

        get("/", (request, response) -> {
            Usuario logUser = request.session(true).attribute("usuario");
            Map<String, Object> attributes = new HashMap<>();
            List<Articulo> misArticulos = SU.listaArticulos();

            if(logUser == null && request.cookie("credenciales") != null){
                request.session(true);
                String username = request.cookie("credenciales");
                request.session().attribute("usuario",
                        SU.buscarUsuario(Desencryptamiento(request.cookie("credenciales"))));
                response.redirect("/");
            }




            attributes.put("titulo", "Página de artículos A&E");
            attributes.put("logUser", logUser);
            attributes.put("tagsCol1", tagsColumnas(2, 1, getAllTags(misArticulos)));
            attributes.put("tagsCol2", tagsColumnas(2, 2, getAllTags(misArticulos)));
            attributes.put("articulos", misArticulos);
            return new ModelAndView(attributes, "index.ftl");
        }, freeMarkerEngine);


        post("/procesarUsuario", (request, response) -> {
            try {
                String usernameAVerificar = request.queryParams("username");
                String passwordsAVerificar = request.queryParams("password");
                String isRecordado = request.queryParams("recordar");
                Usuario logUser = SU.buscarUsuario(usernameAVerificar,passwordsAVerificar);

                if (logUser != null) {
                    request.session(true);
                    request.session().attribute("usuario", logUser);
                    if(isRecordado!=null){
                        response.cookie("/", "credenciales",
                                Encryptamiento(usernameAVerificar), (60*60*24*7), false, true);
                    }
                    response.redirect("/");
                } else {
                    response.redirect("/iniciarSesion");
                }
            } catch (Exception e) {
                System.out.println("Error al intentar iniciar sesión " + e.toString());
            }
            return "";
        });

        get("/gestionarUsuarios", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            Usuario logUser = request.session(true).attribute("usuario");
            attributes.put("titulo", "Gestion de Usuarios-Artículos A&E");
            attributes.put("logUser", logUser);
            return new ModelAndView(attributes, "usuariosIndex.ftl");
        }, freeMarkerEngine);

        post("/registrarNuevoUsuario", (request, response) -> {
            // try {
            String nombre = request.queryParams("nombre");
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String isAdmin = request.queryParams("isAdmin");
            String isAutor = request.queryParams("isAutor");


            Usuario nuevoUsuario = new Usuario(nombre, username, password, isAdmin!=null, false);
            misUsuarios.add(nuevoUsuario);
            SU.crearUsuario(nuevoUsuario);

            response.redirect("/listaUsuarios");

            //} catch (Exception e) {
            //    System.out.println("Error al registrar un usuario " + e.toString());
            //}
            return "";
        });

        get("/listaUsuarios", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            List<Usuario> usuariosEncontrados = SU.listaUsuario();
            attributes.put("titulo", "Lista de Usuarios");
            attributes.put("listaUsuarios", usuariosEncontrados);
            return new ModelAndView(attributes, "listaUsuarios.ftl");
        }, freeMarkerEngine);

        post("/salvarUsuarioEditado", (request, response) -> {
            try {

                Usuario usuarioEditado = SU.buscarUsuario(usernameUsuarioActual);

                String nombre = request.queryParams("nombre");
                String username = request.queryParams("username");
                String password = request.queryParams("password");

                //Faltan los permisos

                usuarioEditado.setNombre(nombre);
                usuarioEditado.setUsername(username);
                usuarioEditado.setPassword(password);
                usuarioEditado.setAdministrador(false);
                usuarioEditado.setAutor(true);

                SU.actualizarUsuario(usuarioEditado);
                response.redirect("/listaUsuarios");
            } catch (Exception e) {
                System.out.println("Error al editar al usuario: " + e.toString());
            }
            return "";
        });


        get("/logout", (request, response) ->
        {
            Session ses = request.session(true);
            ses.invalidate();
            response.removeCookie("credenciales");
            response.redirect("/");
            return "";
        });

        get("/publicarArticulo", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            Usuario logUser = request.session(true).attribute("usuario");
            attributes.put("titulo", "Publicar Artículo");
            attributes.put("logUser", logUser);
            return new ModelAndView(attributes, "publicarArticulo.ftl");
        }, freeMarkerEngine);

       /* post("/procesarArticulo", (request, response) -> {
            System.out.println("Entró al post");

            try {
                String titulo = request.queryParams("title");
                System.out.println("Entro en 1 " + titulo);
                String cuerpo = request.queryParams("cuerpo");
                System.out.println("Entro en 2" + cuerpo);
                List<Comentario> articuloComentarios = new ArrayList<>();
                String[] articuloEtiquetas = request.queryParams("etiquetas").split(",");
                System.out.println("Entro en 3" + articuloEtiquetas);

                Articulo nuevoArticulo = new Articulo(titulo,cuerpo,misUsuarios.get(0),new Date(),articuloComentarios,crearEtiquetas(articuloEtiquetas));
                System.out.println("Se anadió el articulo");
                articulos.add(nuevoArticulo);
                System.out.println("Funciono!");

            } catch (Exception e) {
                System.out.println("Error al intentar publicar articulo" + e.toString());
            }
            response.redirect("/");
            System.out.println("Redirecciona...");
            return "";
        });
    }
    */
        post("/procesarArticulo", (request, response) -> {
            try {
                String titulo = request.queryParams("title");
                String cuerpo = request.queryParams("cuerpo");
                Usuario autor = request.session(true).attribute("usuario");
                Date fecha = new Date();
                List<Comentario> articuloComentarios = new ArrayList<>();
                String[] etiquetas = request.queryParams("etiquetas").split(",");
                List<Etiqueta> articuloEtiquetas = crearEtiquetas(etiquetas);

                Articulo nuevoArticulo = new Articulo(titulo,cuerpo,autor,fecha,articuloComentarios,articuloEtiquetas);
                SU.crearArticulo(nuevoArticulo);

                response.redirect("/");
            } catch (Exception e) {
                System.out.println("Error al publicar artículo: " + e.toString());
            }
            return "";
        });


        get("/leerArticuloCompleto", (request, response) -> {

            String idArticuloActual = request.queryParams("idArticulo");

            Map<String, Object> attributes = new HashMap<>();
            Usuario logUser = request.session(true).attribute("usuario");
            attributes.put("titulo", "Artículo");
            attributes.put("logUser", logUser);
            attributes.put("articulo",SU.buscarArticulo(Long.parseLong(idArticuloActual)));

            attributes.put("tagsCol1", tagsColumnas(2, 1,getTagsArticulo(SU.buscarArticulo(Long.parseLong(idArticuloActual)))));
            attributes.put("tagsCol2", tagsColumnas(2, 2, getTagsArticulo(SU.buscarArticulo(Long.parseLong(idArticuloActual)))));
            return new ModelAndView(attributes, "verArticulo.ftl");
        }, freeMarkerEngine);

        get("/editarArticulo", (request, response) -> {

            String idArticuloEditar = request.queryParams("id");

            Articulo articuloAEditar = SU.buscarArticulo(Long.parseLong(idArticuloEditar));

            System.out.println("Id: "+ articuloAEditar.getId() + " Titulo: " + articuloAEditar.getTitulo() + " Cuerpo " + articuloAEditar.getCuerpo() + " Tags " + articuloAEditar.getListaEtiquetas());

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Editar Usuario");
            attributes.put("articulo", articuloAEditar);

            return new ModelAndView(attributes, "editarArticulo.ftl");
        }, freeMarkerEngine);

        get("/eliminarComentario", (request, response) -> {

            String idArticuloActual = request.queryParams("idArticulo");
            String idComentarioAEliminar = request.queryParams("idComentario");
            System.out.println("Id Articulo "+ idArticuloActual + " idComentario " + idComentarioAEliminar);

            SU.borrarComentario(Long.parseLong(idComentarioAEliminar));

            response.redirect("/leerArticuloCompleto?idArticulo=" + idArticuloActual);
            return "";
        });

        get("/visualizarUsuario", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            String usernameUsuario = request.queryParams("id");
            attributes.put("titulo", "Visualizar Usuario");
            attributes.put("usuario", SU.buscarUsuario(usernameUsuario));
            return new ModelAndView(attributes, "visualizarUsuario.ftl");
        }, freeMarkerEngine);

        get("/editarUsuario", (request, response) -> {

            usernameUsuarioActual = request.queryParams("id");

            Usuario usuario = SU.buscarUsuario(usernameUsuarioActual);

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Editar Usuario");
            attributes.put("usuario", usuario);

            return new ModelAndView(attributes, "editarUsuario.ftl");
        }, freeMarkerEngine);

        get("/eliminarUsuario", (request, response) -> {

            usernameUsuarioActual = request.queryParams("id");
            SU.borrarUsuario(usernameUsuarioActual);

            response.redirect("/listaUsuarios");
            return "";
        });

        post("/comentarArticulo", (request, response) -> {
            try {
                String comentario = request.queryParams("comentarioNuevo");
                Usuario autor = request.session(true).attribute("usuario");
                Articulo articuloActual = SU.buscarArticulo(Long.parseLong(request.queryParams("idArticulo")));

                Comentario nuevoComentario = new Comentario(comentario,autor,articuloActual);
                SU.crearComentario(nuevoComentario);

                response.redirect("/leerArticuloCompleto?idArticulo=" + articuloActual.getId());
            } catch (Exception e) {
                System.out.println("Error al publicar comentario: " + e.toString());
            }
            return "";
        });

    }


    /*public static boolean verificarUsuario(String nombreUsuario,String password){
        boolean usuarioRegistrado = false;
        for (Usuario usuario: misUsuarios){
            if (usuario.getUsername().equals(nombreUsuario)  && usuario.getPassword().equals(password)){
                    usuarioRegistrado = true;
            }
        }
        return usuarioRegistrado;
    }*/

    public static List<String> tagsColumnas(int numColum,int c, List<String> tags){
        List<String> columnaTag = new ArrayList<>();
        int size = tags.size();
        if(tags.size()%2!=0 && numColum>c)
            size++;
        int halfSizeLow = ((size/numColum))*(c - 1);
        int halfSizeHigh = size/numColum*c;

        if(numColum == c && tags.size()%2!=0)
        {
            halfSizeLow++;
            halfSizeHigh++;
        }

        for(int i = halfSizeLow; i < halfSizeHigh; i++){
            columnaTag.add(tags.get(i));
        }

        return columnaTag;
    }

    public static List<String> getAllTags(List<Articulo> articulos){
        List<String> tags = new ArrayList<>();

        for(Articulo A : articulos)
            for(Etiqueta E : A.getListaEtiquetas())
                if(!tags.contains(E.tagsTransform()))
                    tags.add(E.tagsTransform());
        return tags;
    }

    public static List<String> getTagsArticulo(Articulo articulo){
        List<String> tags = new ArrayList<>();

            for(Etiqueta E : articulo.getListaEtiquetas())
                if(!tags.contains(E.tagsTransform()))
                    tags.add(E.tagsTransform());
        return tags;
    }

    public static List<Etiqueta> crearEtiquetas(String[] etiquetas){
        int i = 0;
        List<Etiqueta> etiquetasList = new ArrayList<>();
        for (String etiqueta : etiquetas ){
            etiquetasList.add(new Etiqueta(etiqueta.trim()));
            i++;
        }
        return etiquetasList;
    }

    public static String Encryptamiento(String text){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPasswordCharArray("some-random-data".toCharArray());
        String myEncryptedText = textEncryptor.encrypt(text);
        return myEncryptedText;

    }

    public static String Desencryptamiento(String text){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPasswordCharArray("some-random-data".toCharArray());
        String plainText = textEncryptor.decrypt(text);

        return plainText;
    }


}
