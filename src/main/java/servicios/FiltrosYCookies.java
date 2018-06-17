package servicios;

import logical.Usuario;

import static spark.Spark.*;

public class FiltrosYCookies {
    public void aplicarFiltros(){
        /*before((request, response) -> {
            boolean authenticated = false;
            // ... check if authenticated
            Usuario logUser = request.session(true).attribute("usuario");
            if (logUser != null && !logUser.isAdministrador()) {
                halt(401, "Necesitas permiso para acceder a este lugar!");
            }
        });*/

        after((request, response) -> {
            response.header("foo", "set by after filter");
        });

        afterAfter((request, response) -> {
            response.header("foo", "set by afterAfter filter");
        });

        before("/gestionarUsuarios", (request, response) -> {
            // ... check if authenticated
            Usuario logUser = request.session(true).attribute("usuario");
            if (logUser == null || !logUser.isAdministrador()) {
                halt(401, "Error: Necesitas permiso para acceder a este lugar!");
            }
        });

        before("/listaUsuarios", (request, response) -> {
            // ... check if authenticated
            Usuario logUser = request.session(true).attribute("usuario");
            if (logUser == null || !logUser.isAdministrador()) {
                halt(401, "Error: Necesitas permiso para acceder a este lugar!");
            }
        });

        before("/visualizarUsuario/*", (request, response) -> {
            // ... check if authenticated
            Usuario logUser = request.session(true).attribute("usuario");
            if (logUser == null || !logUser.isAdministrador()) {
                halt(401, "Error: Necesitas permiso para acceder a este lugar!");
            }
        });

        before("/editarUsuario/*", (request, response) -> {
            // ... check if authenticated
            Usuario logUser = request.session(true).attribute("usuario");
            if (logUser == null || !logUser.isAdministrador()) {
                halt(401, "Error: Necesitas permiso para acceder a este lugar!");
            }
        });

        before("/eliminarUsuario/*", (request, response) -> {
            // ... check if authenticated
            Usuario logUser = request.session(true).attribute("usuario");
            if (logUser == null || !logUser.isAdministrador()) {
                halt(401, "Error: Necesitas permiso para acceder a este lugar!");
            }
        });

        before("/publicarArticulo", (request, response) -> {
            // ... check if authenticated
            Usuario logUser = request.session(true).attribute("usuario");
            if (logUser == null || (!logUser.isAdministrador() && !logUser.isAutor())) {
                halt(401, "Error: Necesitas permiso para acceder a este lugar!");
            }
        });

        /*get("/crearCookie/:nombreCookie/:valor", (request, response)->{
            //creando cookie en para un minuto
            response.cookie("/", request.params("nombreCookie"), request.params("valor"), 60, true); //incluyendo el path del cookie.

            return "Cookie creado con exito...";
        });*/


    }

}
