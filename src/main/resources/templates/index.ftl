<!DOCTYPE html>
<html lang="en">

  <head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${titulo}</title>

    <!-- Bootstrap core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/blog-home.css" rel="stylesheet">
      <style>
          .editBtn{
              background-color: #007bff;
              color: #fff;
              padding: 5px 5px;
              margin: 10px 0 ;
              border: none;
              cursor: pointer;
              width: 10%;
              border-radius: 20px;
          }
          a.editBtn:hover{
              text-decoration: none;
              opacity: 0.9;
              background-color: cornflowerblue;
              color:white;
          }

      </style>
  </head>

  <body>

    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <div class="container">
            <a class="navbar-brand" href="/">Blog de Artículos A&E</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarResponsive">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="/">Inicio
                            <span class="sr-only">(current)</span>
                        </a>
                    </li>
                    <#if logUser??>
                        <#if logUser.administrador || logUser.autor>
                            <li class="nav-item">
                                <a class="nav-link" href="/publicarArticulo">Artículos</a>
                            </li>
                        </#if>
                    </#if>

                    <#if logUser??>
                        <#if logUser.administrador>
                            <li class="nav-item">
                                <a class="nav-link" href="/listaUsuarios">Gestionar Usuarios</a>
                            </li>
                        </#if>
                    </#if>
                    <#if logUser??>
                        <li class="nav-item">
                            <a class="nav-link" href="/logout">Cerrar sesión</a>
                        </li>
                    <#else>
                        <li class="nav-item">
                            <a class="nav-link" href="/iniciarSesion">Iniciar sesión</a>
                        </li>
                    </#if>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Page Content -->
    <div class="container">

      <div class="row">

        <!-- Blog Entries Column -->
        <div class="col-md-8">

          <h1 class="my-4">Artículos
            <small>Últimas publicaciones</small>
          </h1>

          <!-- Blog Post -->
          <#list articulos as articulo>
            <div class="card mb-4">
                <img class="card-img-top" src="http://placehold.it/750x300" alt="Card image cap">
                <div class="card-body">
              <#if logUser??>
                  <#if logUser.administrador || logUser.autor>
                      <a href="/editarArticulo?id=${articulo.id}" class="editBtn">Editar</a>
                      <br>
                      <br>
                  </#if>
              </#if>
                    <h2 class="card-title">${articulo.titulo}</h2>
                    <p class="card-text">
                    ${articulo.textoResumido()}
                    </p>
                    <a href="/leerArticuloCompleto?idArticulo=${articulo.id}" class="btn btn-primary">Leer más &rarr;</a>
                </div>
                <div class="card-footer text-muted">
                    Subido en ${articulo.fechaText()} por
                    <a href="#">${articulo.autor.nombre}</a>
                </div>
            </div>
        </#list>

          <!-- Pagination -->
          <ul class="pagination justify-content-center mb-4">
            <li class="page-item">
              <a class="page-link" href="#">&larr; Older</a>
            </li>
            <li class="page-item disabled">
              <a class="page-link" href="#">Newer &rarr;</a>
            </li>
          </ul>

        </div>

        <!-- Sidebar Widgets Column -->
        <div class="col-md-4">

          <!-- Categories Widget -->
            <div class="card my-4">
                <h5 class="card-header">Tags</h5>
                <div class="card-body">
                    <div class="row">
                        <div class="col-lg-6">
                            <ul class="list-unstyled mb-0">
                                <#list tagsCol1 as t1>
                                    <li>
                                        <a href="#">${t1}</a>
                                    </li>
                                </#list>
                            </ul>
                        </div>
                        <div class="col-lg-6">
                            <ul class="list-unstyled mb-0">
                                <#list tagsCol2 as t2>
                                    <li>
                                        <a href="#">${t2}</a>
                                    </li>
                                </#list>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

        </div>

      </div>
      <!-- /.row -->

    </div>
    <!-- /.container -->

    <!-- Footer -->
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
