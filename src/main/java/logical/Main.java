package logical;


import freemarker.template.Configuration;

import org.jasypt.util.text.BasicTextEncryptor;
import servicios.*;

import spark.ModelAndView;
import spark.Session;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.SQLException;
import java.util.*;

import static spark.Spark.*;


public class Main {


    private static String usernameUsuarioActual;
    private static String idArticuloActual;


    public static void main(String[] args) throws SQLException {


        staticFiles.location("/templates");

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);
        cfg.setClassForTemplateLoading(Main.class, "/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(cfg);

        new FiltrosYCookies().aplicarFiltros();

        //Pruebas conexion BD modo Server
        ServiciosBootStrap.getInstancia().init();

        //System.out.println(ServiciosArticulos.getInstancia().findByTag("moda"));

        //ServiciosDataBase.getInstancia().testConexion();

        //ServiciosBootStrap.crearTablas();
        //SU.crearAdmin();
        //ServiciosBootStrap.detenetBD();
        ServiciosUsuarios.getInstancia().crearAdmin();


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
            if(logUser == null && request.cookie("dcfgvhb2hjrkb2j289yhuij") != null){
                request.session(true);
                String username = request.cookie("dcfgvhb2hjrkb2j289yhuij");
                request.session().attribute("usuario",
                        ServiciosUsuarios.getInstancia().find(Desencryptamiento(request.cookie("dcfgvhb2hjrkb2j289yhuij"))));
                response.redirect("/");
            }
            response.redirect("/home?page=1");
            return "";
        });

        get("/home", (request, response) -> {
            Usuario logUser = request.session(true).attribute("usuario");
            Map<String, Object> attributes = new HashMap<>();
            int pagina = Integer.parseInt(request.queryParams("page"));
            List<Articulo> misArticulos = ServiciosArticulos.getInstancia().findAllIndexado(pagina);
            List<String> tags = getTags(ServiciosEtiquetas.getInstancia().findAll());
            double maxPage = Math.ceil((double)ServiciosArticulos.getInstancia().findAll().size()/5);

            attributes.put("titulo", "Página de artículos A&E");
            attributes.put("logUser", logUser);
            attributes.put("tagsCol1", tagsColumnas(2, 1, tags));
            attributes.put("tagsCol2", tagsColumnas(2, 2, tags));
            attributes.put("articulos", misArticulos);
            attributes.put("page", pagina);
            if(pagina <= 1)
                attributes.put("validP", null);
            else
                attributes.put("validP", "true");
            if(pagina >= maxPage)
                attributes.put("validN", null);
            else
                attributes.put("validN", "true");

            attributes.put("prevPage", (pagina - 1));
            attributes.put("nextPage", (pagina + 1));

            return new ModelAndView(attributes, "index.ftl");
        }, freeMarkerEngine);


        post("/procesarUsuario", (request, response) -> {
            try {
                String usernameAVerificar = request.queryParams("username");
                String passwordsAVerificar = request.queryParams("password");
                String isRecordado = request.queryParams("recordar");
                Usuario logUser = ServiciosUsuarios.getInstancia().findByUsernameAndPassword(usernameAVerificar,passwordsAVerificar);

                if (logUser != null) {
                    request.session(true);
                    request.session().attribute("usuario", logUser);
                    if(isRecordado!=null){
                        response.cookie("/", "dcfgvhb2hjrkb2j289yhuij",
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
            try {
                String nombre = request.queryParams("nombre");
                String username = request.queryParams("username");
                String password = request.queryParams("password");
                String isAdmin = request.queryParams("rbAdmin");
                String isAutor = request.queryParams("rbAutor");

                Usuario nuevoUsuario = new Usuario(username, nombre, password, isAdmin!=null, isAutor!=null);
                ServiciosUsuarios.getInstancia().crear(nuevoUsuario);

                response.redirect("/listaUsuarios");

            } catch (Exception e) {
                System.out.println("Error al registrar un usuario " + e.toString());
            }
            return "";
        });

        get("/listaUsuarios", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            List<Usuario> usuariosEncontrados = ServiciosUsuarios.getInstancia().findAll();
            attributes.put("titulo", "Lista de Usuarios");
            attributes.put("listaUsuarios", usuariosEncontrados);
            return new ModelAndView(attributes, "listaUsuarios.ftl");
        }, freeMarkerEngine);

        post("/salvarUsuarioEditado", (request, response) -> {
            try {

                Usuario usuarioEditado = ServiciosUsuarios.getInstancia().find(usernameUsuarioActual);

                String nombre = request.queryParams("nombre");
                String username = request.queryParams("username");
                String password = request.queryParams("password");
                String isAdmin = request.queryParams("rbAdmin");
                String isAutor = request.queryParams("rbAutor");

                //Faltan los permisos

                usuarioEditado.setNombre(nombre);
                usuarioEditado.setUsername(username);
                usuarioEditado.setPassword(password);
                usuarioEditado.setAdministrador(isAdmin!=null);
                usuarioEditado.setAutor(isAutor!=null);

                ServiciosUsuarios.getInstancia().editar(usuarioEditado);
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
            response.removeCookie("dcfgvhb2hjrkb2j289yhuij");
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


        post("/procesarArticulo", (request, response) -> {
            try {
                String titulo = request.queryParams("title");
                String cuerpo = request.queryParams("cuerpo");
                Usuario autor = request.session(true).attribute("usuario");
                Date fecha = new Date();
                Set<Comentario> articuloComentarios = new HashSet<>();
                String tags = request.queryParams("etiquetas");
                String[] etiquetas = request.queryParams("etiquetas").split(",");
                Set<Etiqueta> articuloEtiquetas = crearEtiquetas(etiquetas);

                Articulo nuevoArticulo = new Articulo(titulo,cuerpo,autor,fecha,articuloComentarios,articuloEtiquetas);
                System.out.println("Probando crear: " + nuevoArticulo.getTitulo());

                ServiciosArticulos.getInstancia().crear(nuevoArticulo);

                response.redirect("/");
            } catch (Exception e) {
                //System.out.println("Error al publicar artículo: " + e.toString());
            }
            return "";
        });

        post("/salvarArticuloEditado", (request, response) -> {
            //try {

                Articulo articuloEditado = ServiciosArticulos.getInstancia().find(Long.parseLong(idArticuloActual));

                String titulo = request.queryParams("title");
                String cuerpo = request.queryParams("cuerpo");
                Usuario autor = request.session(true).attribute("usuario");
                Date fecha = new Date();
                String[] etiquetas = request.queryParams("etiquetas").split(",");
                Set<Etiqueta> articuloEtiquetas = crearEtiquetas(etiquetas);

                articuloEditado.setTitulo(titulo);
                articuloEditado.setCuerpo(cuerpo);
                articuloEditado.setAutor(autor);
                articuloEditado.setFecha(fecha);
                articuloEditado.setListaEtiquetas(articuloEtiquetas);

                ServiciosArticulos.getInstancia().editar(articuloEditado);

                response.redirect("/");
            //} catch (Exception e) {
                //System.out.println("Error al editar el artículo: " + e.toString());
            //}
            return "";
        });


        get("/leerArticuloCompleto/:id", (request, response) -> {

            idArticuloActual = request.params("id");
            Map<String, Object> attributes = new HashMap<>();
            Articulo miArticulo = ServiciosArticulos.getInstancia().find(Long.parseLong(idArticuloActual));
            List<String> tags = getTags(new ArrayList(miArticulo.getListaEtiquetas()));
            Usuario logUser = request.session(true).attribute("usuario");
            attributes.put("titulo", "Artículo");
            attributes.put("logUser", logUser);
            attributes.put("articulo",miArticulo);
            if (logUser.likeArticulo(miArticulo)) {
                System.out.println("Like encontrado: " + logUser.likeArticulo(miArticulo));
                attributes.put("like", "true");
            }
            else {
                System.out.println("Disike no encontrado...");
                attributes.put("like", null);
            }
            if (logUser.dislikeArticulo(miArticulo)) {
                System.out.println("Like encontrado: " + logUser.likeArticulo(miArticulo));
                attributes.put("dislike", "true");
            }
            else
                attributes.put("dislike",null);

            attributes.put("tagsCol1", tagsColumnas(2, 1,tags));
            attributes.put("tagsCol2", tagsColumnas(2, 2, tags));
            return new ModelAndView(attributes, "verArticulo.ftl");
        }, freeMarkerEngine);

        get("/editarArticulo/:id", (request, response) -> {

            idArticuloActual = request.params("id");

            Articulo articuloAEditar = ServiciosArticulos.getInstancia().find(Long.parseLong(idArticuloActual));

            System.out.println("Titulo:"+articuloAEditar.getTitulo()+" Cuerpo: "+articuloAEditar.getCuerpo());

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Editar Articulo");
            attributes.put("articulo", articuloAEditar);

            return new ModelAndView(attributes, "editarArticulo.ftl");
        }, freeMarkerEngine);

        get("/eliminarComentario/:idArticulo/:idComentario", (request, response) -> {

            String idArticuloActual = request.params("idArticulo");
            String idComentarioAEliminar = request.params("idComentario");

            ServiciosComentarios.getInstancia().eliminar(Long.parseLong(idComentarioAEliminar));

            response.redirect("/leerArticuloCompleto/" + idArticuloActual);
            return "";
        });

        get("/visualizarUsuario", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            String usernameUsuario = request.queryParams("id");
            attributes.put("titulo", "Visualizar Usuario");
            attributes.put("usuario", ServiciosUsuarios.getInstancia().find(usernameUsuario));
            return new ModelAndView(attributes, "visualizarUsuario.ftl");
        }, freeMarkerEngine);

        get("/editarUsuario", (request, response) -> {

            usernameUsuarioActual = request.queryParams("id");

            Usuario usuario = ServiciosUsuarios.getInstancia().find(usernameUsuarioActual);

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Editar Usuario");
            attributes.put("usuario", usuario);

            return new ModelAndView(attributes, "editarUsuario.ftl");
        }, freeMarkerEngine);

        get("/eliminarUsuario", (request, response) -> {

            usernameUsuarioActual = request.queryParams("id");
            ServiciosUsuarios.getInstancia().eliminar(usernameUsuarioActual);

            response.redirect("/listaUsuarios");
            return "";
        });

        post("/comentarArticulo/:id", (request, response) -> {
            try {
                String comentario = request.queryParams("comentarioNuevo");
                Usuario autor = request.session(true).attribute("usuario");
                Articulo articuloActual = ServiciosArticulos.getInstancia().find(Long.parseLong(request.params("id")));

                Comentario nuevoComentario = new Comentario(comentario,autor,articuloActual);
                ServiciosComentarios.getInstancia().crear(nuevoComentario);

                response.redirect("/leerArticuloCompleto/" + articuloActual.getId());
            } catch (Exception e) {
                System.out.println("Error al publicar comentario: " + e.toString());
            }
            return "";
        });

        get("/busquedaPorTag", (request, response) -> {
            Usuario logUser = request.session(true).attribute("usuario");
            Map<String, Object> attributes = new HashMap<>();
            int pagina = Integer.parseInt(request.queryParams("page"));
            String tag = request.queryParams("tag");;

            List<Articulo> misArticulos = ServiciosArticulos.getInstancia().findByTag(tag,pagina);
            List<String> tags = getTags(ServiciosEtiquetas.getInstancia().findAll());

            double maxPage = Math.ceil((double)ServiciosArticulos.getInstancia().countByTag(tag)/5);

            attributes.put("titulo", "Página de artículos A&E");
            attributes.put("logUser", logUser);
            attributes.put("tagFind", tag);
            attributes.put("tagsCol1", tagsColumnas(2, 1, tags));
            attributes.put("tagsCol2", tagsColumnas(2, 2, tags));
            attributes.put("articulos", misArticulos);
            if(pagina <= 1)
                attributes.put("validP", null);
            else
                attributes.put("validP", "true");
            if(pagina >= maxPage)
                attributes.put("validN", null);
            else
                attributes.put("validN", "true");

            attributes.put("prevPage", (pagina - 1));
            attributes.put("nextPage", (pagina + 1));
            return new ModelAndView(attributes, "ArticulosTags.ftl");
        }, freeMarkerEngine);


        get("/procesarLike", (request, response) -> {
            try {

                Usuario usuario = request.session(true).attribute("usuario");
                Articulo articulo = ServiciosArticulos.getInstancia().find(Long.parseLong(idArticuloActual));

                ServiciosLikes.getInstancia().deleteLikes(articulo,usuario);
                ServiciosLikesArticulos.getInstancia().crear(new LikesArticulo(articulo,usuario,true));

                response.redirect("/leerArticuloCompleto/" + idArticuloActual);

            } catch (Exception e) {
                System.out.println("Error al indicar like en el artículo actual: " + e.toString());
            }
            return "";
        });

        get("/procesarDislike", (request, response) -> {
            try {

                Usuario usuario = request.session(true).attribute("usuario");
                Articulo articulo = ServiciosArticulos.getInstancia().find(Long.parseLong(idArticuloActual));

                ServiciosLikes.getInstancia().deleteLikes(articulo,usuario);
                ServiciosLikesArticulos.getInstancia().crear(new LikesArticulo(articulo,usuario,false));

                response.redirect("/leerArticuloCompleto/" + idArticuloActual);

            } catch (Exception e) {
                System.out.println("Error al indicar like en el artículo actual: " + e.toString());
            }
            return "";
        });

        get("/eliminarLike", (request, response) -> {
            try {

                Usuario usuario = request.session(true).attribute("usuario");
                Articulo articulo = ServiciosArticulos.getInstancia().find(Long.parseLong(idArticuloActual));

                ServiciosLikes.getInstancia().deleteLikes(articulo,usuario);

                response.redirect("/leerArticuloCompleto/" + idArticuloActual);

            } catch (Exception e) {
                System.out.println("Error al indicar like en el artículo actual: " + e.toString());
            }
            return "";
        });
    }


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

    public static Set<Etiqueta> crearEtiquetas(String[] etiquetas){
        Set<Etiqueta> etiquetasList = new HashSet<>();
        for (String etiqueta : etiquetas )
            etiquetasList.add(new Etiqueta(etiqueta.trim()));
        return etiquetasList;
    }

    public static String Encryptamiento(String text){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPasswordCharArray("kjnkuakffbkjg23425".toCharArray());
        String myEncryptedText = textEncryptor.encrypt(text);
        return myEncryptedText;

    }

    public static String Desencryptamiento(String text){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPasswordCharArray("kjnkuakffbkjg23425".toCharArray());
        String plainText = textEncryptor.decrypt(text);

        return plainText;
    }

    public static List<String> getTags(List<Etiqueta> etiquetas){
        List<String> tags = new ArrayList<>();

        for(Etiqueta E : etiquetas)
            if(!tags.contains(E.tagsTransform()))
                tags.add(E.tagsTransform());
        return tags;
    }

}
