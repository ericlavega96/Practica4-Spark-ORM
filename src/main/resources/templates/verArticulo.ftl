<!DOCTYPE html>
<html lang="en">

  <head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${titulo}</title>

    <!-- Bootstrap core CSS -->
    <link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">


    <!-- Custom styles for this template -->
    <link href="/css/blog-post.css" rel="stylesheet">
    <style>
        .deleteBtn{
            background-color: red;
            color: #fff;
            font-size: 10px;
            padding: 2px 2px;
            margin: auto ;
            border: none;
            cursor: pointer;
            width: 2%;
            border-radius: 5px;
        }
        a.deleteBtn:hover{
            text-decoration: none;
            opacity: 0.9;
            background-color: darkred;
            color:white;
        }
        .like-btn,
        .dislike-btn {
            color: gainsboro;
        }

        a:hover{
            text-decoration: none;
        }
        .like-btn:hover .fa-thumbs-up{
            color: lawngreen;
        }
        .dislike-btn:hover .fa-thumbs-down{
            color: red;
        }
        .like-btn:focus .fa-thumbs-up{
            color: lawngreen;
        }
        .dislike-btn:focus .fa-thumbs-down{
            color: red;
        }
        .like-btn-on{
            color: lawngreen;
        }
        .dislike-btn-on{
            color: red;
        }
		.likes-dislikes-container {
			display:inline;
            font-size: 50px;
            user-select: none;
            display: inline;
            margin:auto;
		}
		li{
			display: inline-block;
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

        <!-- Post Content Column -->
        <div class="col-lg-8">

          <!-- Title -->
          <h1 class="mt-4">${articulo.titulo}</h1>

          <!-- Author -->
          <p class="lead">
            por
            <a href="#">${articulo.autor.nombre}</a>
          </p>

          <hr>
        <hr>

          <!-- Date/Time -->
          <p>${articulo.fechaText()}</p>

          <hr>

          <!-- Preview Image -->
          <img class="img-fluid rounded" src="http://placehold.it/900x300" alt="">

          <hr>
          <!-- Post Content -->
          <p>${articulo.cuerpo}</p>

          <hr>
            <div class="likes-dislikes-container">
				<ul>
					<li>
						<#if like??>
							<a href="/eliminarLike" class="like-btn-on" >
								<i class="fa fa-thumbs-up"></i>
							</a>
						<#else>
							<a href="/procesarLike" class="like-btn" >
								<i class="fa fa-thumbs-up"></i>
							</a>
						</#if>
						<p>${articulo.likesCount()}</p>
					</li>	
					<li>
						<#if dislike??>
							<a href="/eliminarLike" class="dislike-btn-on">
								<p></p>
								<i class="fa fa-thumbs-down"></i>
							</a>
						<#else>
							<a href="/procesarDislike" class="dislike-btn">
								<p></p>
								<i class="fa fa-thumbs-down"></i>
							</a>
						</#if>
						<p>${articulo.dislikesCount()}</p>
					</li>
				</ul>
            </div>

          <!-- Comments Form -->
          <#if logUser??>
              <div class="card my-4">
                <h5 class="card-header">Deja un comentario:</h5>
                <div class="card-body">
                  <form action="/comentarArticulo/${articulo.id}" method="post">
                    <div class="form-group">
                      <textarea name="comentarioNuevo" class="form-control" rows="3"></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Comentar</button>
                  </form>
                </div>
              </div>
          </#if>

          <!-- Single Comment -->
          <#list articulo.listaComentarios as comentario>
            <div class="media mb-4">
                <img class="d-flex mr-3 rounded-circle" src="http://placehold.it/50x50" alt="">
                <div class="media-body">
                <h5 class="mt-0">${comentario.autor.username}</h5>
                  ${comentario.comentario}
                </div>
            </div>
            <div>
                <#if logUser??>
                    <#if logUser.administrador || logUser.autor>
                        <a href="/eliminarComentario/${articulo.id}/${comentario.id}" class="deleteBtn">Eliminar Comentario</a>
                        <br>
                        <br>
                    </#if>
                </#if>
            </div>
          </#list>
        </div>

        <!-- Sidebar Widgets Column -->
        <div class="col-md-4">

          <!-- Categories Widget -->
            <div class="card my-4">
                <h5 class="card-header">Tags del Artículo</h5>
                <div class="card-body">
                    <div class="row">
                        <div class="col-lg-6">
                            <ul class="list-unstyled mb-0">
                                <#list tagsCol1 as t1>
                                    <li>
                                        <a href="/busquedaPorTag?page=1&tag=${t1}">${t1}</a>
                                    </li>
                                </#list>
                            </ul>
                        </div>
                        <div class="col-lg-6">
                            <ul class="list-unstyled mb-0">
                                <#list tagsCol2 as t2>
                                    <li>
                                        <a href="/busquedaPorTag?page=1&tag=${t2}">${t2}</a>
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
    <script src="/vendor/jquery/jquery.min.js"></script>
    <script src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

  </body>

</html>
