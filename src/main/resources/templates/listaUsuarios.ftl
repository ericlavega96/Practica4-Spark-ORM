<!DOCTYPE html>
<html lang="en">

  <head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${titulo}</title>
      <style>
          .tablaContainer{
              margin: auto;
          }
          table{
              width: 100%;
          }
          th, td {
              text-align: left;
              width: auto;
          }
          a.button {  
              background-color: #007bff;  
              color: #fff;  
              width: 100%;  
              margin: 10px 0 ;
              border: none;
              cursor: pointer;
              border-radius: 10%;
          }
          a.button:hover{
              text-decoration: none;
              opacity: 0.8;
              color: whitesmoke;
          }
      </style>

    <!-- Bootstrap core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/blog-home.css" rel="stylesheet">

  </head>

  <body>

    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <div class="container">
            <a class="navbar-brand" href="/">Blog de Artículos A&E - Gestión de Usuarios</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarResponsive">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="/gestionarUsuarios">Registrar usuario
                            <span class="sr-only">(current)</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/listaUsuarios">Lista de Usuarios</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Page Content -->
    <div class="container">

      <div class="row">

        <!-- Blog Entries Column -->
        <div class="tablaContainer">
          <h1 class="my-4">Lista de Usuarios</h1>
            <table>
                <tr>
                    <th>Username</th><th>Nombre del Usuario</th><th>Contraseña</th><th>Permiso</th>
                </tr>
                <tr>
                <#list listaUsuarios as usuario>
                    <tr>
                        <td>${usuario.username}</td>
                        <td>${usuario.nombre}</td>
                        <td>${usuario.password}</td>
                        <#if usuario.administrador>
                            <td>Administrador</td>
                        <#elseif usuario.autor>
                            <td>Autor</td>
                        <#else>
                            <td>Ninguno</td>
                        </#if>
                        <td><a href="/visualizarUsuario?id=${usuario.username}" class="button">Visualizar</a></td>
                        <td><a href="/editarUsuario?id=${usuario.username}" class="button">Editar</a></td>
                        <td><a href="/eliminarUsuario?id=${usuario.username}" class="button">Eliminar</a></td>
                    </tr>
                </#list>
                </tr>
            </table>
            <br>
            <br>
        </div>
      </div>
    </div>
    <footer class="py-5 bg-dark">
      <div class="container">
        <p class="m-0 text-center text-white">Copyright &copy; Your Website 2018</p>
      </div>
      <!-- /.container -->
    </footer>

    <!-- Bootstrap core JavaScript -->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  </body>

</html>
