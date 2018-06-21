<!DOCTYPE html>
<html lang="en">

  <head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${titulo}</title>
      <style>
          .registrarUsuarioContainer{
              margin: auto;
          }
          button {
              background-color: #007bff;
              color: #fff;
              padding: 12px 20px;
              margin: 10px 0 ;
              border: none;
              cursor: pointer;
              width: 100%;
          }
          input[type=text], input[type=password]{
              width: 100%;
              border: 1px solid #ccc;
              box-sizing: border-box;
              padding: 5px 5px;
              margin: 10px 0;
          }
          input[type=checkbox]{
              margin:auto;
              margin-right: 40px;
              margin-top: 20px;
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
        <div class="registrarUsuarioContainer">
          <h1 class="my-4">Registrar Usuario</h1>
            <form action="/registrarNuevoUsuario" method="post">
                Username<br>
                <input name="username" type="text" required>

                Nombre del Usuario<br>
                <input name="nombre" type="text" required>

                Contrasena<br>
                <input name="password" type="password" required>

                <div class="radioButton">
                    Permisos<br>
                    <label>Administrador
                        <input type="checkbox" name="rbAdmin">
                    </label>
                    <label>Autor
                        <input type="checkbox" name="rbAutor">
                    </label>
                </div>
                <br>
                <button name="registrarUsuarioButton" type="submit">Registrar Usuario</button>
            </form>
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
    <script>
        $(document).ready(function(){
            $('input:checkbox').click(function() {
                $('input:checkbox').not(this).prop('checked', false);
            });
        });
    </script>
  </body>
</html>
