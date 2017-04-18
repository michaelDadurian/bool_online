<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<!DOCTYPE HTML>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

  <spring:url value = "resources/profile/css/simple-sidebar.css" var = "sidebarCSS" />
  <link href = "${sidebarCSS}" rel = "stylesheet">

  <spring:url value = "resources/profile/img/bool_logo.png" var = "boolLogo" />
  <spring:url value = "/resources/profile/img/bell1.png" var = "bell" />

</head>
<body>


<div class="container" style="background-color: #FDFFFF">
  <div class="row">
    <div class="col-sm-2">
        <a href="/" >
            <img src="${boolLogo}" class="img-rounded" alt="bool_logo" height="75%" width="75%">
    </div>
    <div class="col-sm-8">
      <div id="custom-search-input">
        <div class="input-group col-md-12">
          <input type="text" class="  search-query form-control" placeholder="Search" />
          <span class="input-group-btn">
              <button class="btn btn-danger" type="button"><span class=" glyphicon glyphicon-search"></span></button>
            </span>
        </div>
      </div>
      <button type="button" class="btn btn-primary btn-xs btn-custom">New</button>
      <button type="button" class="btn btn-warning btn-xs btn-custom">Upload</button>
    </div>
    <div class="col-sm-2">
      <img id="notifications" src="${bell}">
    </div>
  </div>
  <div class="row">
    <div class="col-sm-2">
      <div id="sidebar-wrap">
        <ul class="sidebar"style="font-style: normal">
          <li style="list-style-type: none">
            <div id="all_circuit_div"><a href="profile">All Circuit</a></div>
          </li>
          <li style="list-style-type: none">
            <div id="profile_circuit_div"><a href="profile">Profile - Owned By Me</a></div>
          </li>
          <li style="list-style-type: none">
            <div id="shared_circuit_div"><a href="shared">Shared with Me</a></div>
          </li>
          <li style="list-style-type: none">
            <div id="public_circuit_div"><a href="public">Public</a></div>
          </li>
        </ul>
      </div>
    </div>
    <div class="col-sm-10">
      <!--div for All circuits-->
      <section class ="jquery_tables table_all_circuits">
        <div class="panel panel-default panel-table">
          <div class="panel-heading">
            <div class="row">
              <div class="col col-xs-6">
                <h3 class="panel-title"></h3>
              </div>
              <div class="col col-xs-6 text-right">
                <!--<button type="button" class="btn btn-sm btn-primary btn-create">Create New</button> -->
              </div>
            </div>
          </div>
          <div class="panel-body">
            <table class="table table-striped table-bordered table-list">
              <thead>
              <tr>
                <th><span class="glyphicon glyphicon-cog" aria-hidden="true"></span></th>
                <th>Circuit Name</th>
                <th>Owner</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach items="${circuitNames}" var="circuitName">
              <tr>

                <td align="center">

                          <a class="btn btn-default">
                            <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                          </a>
                          <a class="btn btn-default">
                            <span class="glyphicon glyphicon-share" aria-hidden="true"></span>
                          </a>
                          <a class="btn btn-default">
                            <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                          </a>
                        </td>
                        <td>${circuitName}</td>


                </tr>
                </c:forEach>

              </tbody>
            </table>

          </div>
          <div class="panel-footer">
            <div class="row">
              <div class="col col-xs-4">Page 1 of 5
              </div>
              <div class="col col-xs-8">
                <ul class="pagination hidden-xs pull-right">
                  <li><a href="#">1</a></li>
                  <li><a href="#">2</a></li>
                  <li><a href="#">3</a></li>
                  <li><a href="#">4</a></li>
                  <li><a href="#">5</a></li>
                </ul>
                <ul class="pagination visible-xs pull-right">
                  <li><a href="#">«</a></li>
                  <li><a href="#">»</a></li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </section>
      <!--div for circuits owned by user-->
      <section class ="jquery_tables table_owned">
        <div class="panel panel-default panel-table">
          <div class="panel-heading">
            <div class="row">
              <div class="col col-xs-6">
                <h3 class="panel-title"></h3>
              </div>
              <div class="col col-xs-6 text-right">
                <!--<button type="button" class="btn btn-sm btn-primary btn-create">Create New</button> -->
              </div>
            </div>
          </div>
          <div class="panel-body">
            <table class="table table-striped table-bordered table-list">
              <thead>
              <tr>
                <th><span class="glyphicon glyphicon-cog" aria-hidden="true"></span></th>
                <th>Circuit Name</th>
                <th>Owner</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td align="center">
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-share" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                  </a>
                </td>
                <td>Reef's Circuit</td>
                <td>Me</td>
              </tr>
              <tr>
                <td align="center">
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-share" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                  </a>
                </td>
                <td>Reef's Circuit 2</td>
                <td>Me</td>
              </tr>
              </tbody>
            </table>

          </div>
          <div class="panel-footer">
            <div class="row">
              <div class="col col-xs-4">Page 1 of 5
              </div>
              <div class="col col-xs-8">
                <ul class="pagination hidden-xs pull-right">
                  <li><a href="#">1</a></li>
                  <li><a href="#">2</a></li>
                  <li><a href="#">3</a></li>
                  <li><a href="#">4</a></li>
                  <li><a href="#">5</a></li>
                </ul>
                <ul class="pagination visible-xs pull-right">
                  <li><a href="#">«</a></li>
                  <li><a href="#">»</a></li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </section>
      <!--div for circuits shared with user-->
      <section class="jquery_tables table_shared_circuits">
        <div class="panel panel-default panel-table">
          <div class="panel-heading">
            <div class="row">
              <div class="col col-xs-6">
                <h3 class="panel-title"></h3>
              </div>
              <div class="col col-xs-6 text-right">
                <!--<button type="button" class="btn btn-sm btn-primary btn-create">Create New</button> -->
              </div>
            </div>
          </div>
          <div class="panel-body">
            <table class="table table-striped table-bordered table-list">
              <thead>
              <tr>
                <th><span class="glyphicon glyphicon-cog" aria-hidden="true"></span></th>
                <th>Circuit Name</th>
                <th>Owner</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td align="center">
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-share" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                  </a>
                </td>
                <td>Circuit1 shared with Mike</td>
                <td>Me</td>
              </tr>
              <tr>
                <td align="center">
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-share" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                  </a>
                </td>
                <td>Circuit2 shared with Mike</td>
                <td>Me</td>
              </tr>
              <tr>
                <td align="center">
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-share" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                  </a>
                </td>
                <td>Mike's Circuit 3</td>
                <td>Me</td>
              </tr>
              </tbody>
            </table>

          </div>
          <div class="panel-footer">
            <div class="row">
              <div class="col col-xs-4">Page 1 of 5
              </div>
              <div class="col col-xs-8">
                <ul class="pagination hidden-xs pull-right">
                  <li><a href="#">1</a></li>
                  <li><a href="#">2</a></li>
                  <li><a href="#">3</a></li>
                  <li><a href="#">4</a></li>
                  <li><a href="#">5</a></li>
                </ul>
                <ul class="pagination visible-xs pull-right">
                  <li><a href="#">«</a></li>
                  <li><a href="#">»</a></li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!--div for circuits shared with public-->
      <section class ="jquery_tables table_public">
        <div class="panel panel-default panel-table">
          <div class="panel-heading">
            <div class="row">
              <div class="col col-xs-6">
                <h3 class="panel-title"></h3>
              </div>
              <div class="col col-xs-6 text-right">
                <!--<button type="button" class="btn btn-sm btn-primary btn-create">Create New</button> -->
              </div>
            </div>
          </div>
          <div class="panel-body">
            <table class="table table-striped table-bordered table-list">
              <thead>
              <tr>
                <th><span class="glyphicon glyphicon-cog" aria-hidden="true"></span></th>
                <th>Circuit Name</th>
                <th>Owner</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td align="center">
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-share" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                  </a>
                </td>
                <td>Public Circuit</td>
                <td>Me</td>
              </tr>
              <tr>
                <td align="center">
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-share" aria-hidden="true"></span>
                  </a>
                  <a class="btn btn-default">
                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                  </a>
                </td>
                <td>Reef's Circuit 2</td>
                <td>Me</td>
              </tr>
              </tbody>
            </table>

          </div>
          <div class="panel-footer">
            <div class="row">
              <div class="col col-xs-4">Page 1 of 5
              </div>
              <div class="col col-xs-8">
                <ul class="pagination hidden-xs pull-right">
                  <li><a href="#">1</a></li>
                  <li><a href="#">2</a></li>
                  <li><a href="#">3</a></li>
                  <li><a href="#">4</a></li>
                  <li><a href="#">5</a></li>
                </ul>
                <ul class="pagination visible-xs pull-right">
                  <li><a href="#">«</a></li>
                  <li><a href="#">»</a></li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </section>

    </div>

  </div>

  <!--<div class="row">-->
    <!--<div class="col-sm-2">-->

    <!--</div>-->
    <!--<div class="col-sm-8"style="background-color:blue;">-->
      <!--<p>hello world</p>-->
    <!--</div>-->
    <!--<div class="col-sm-2"style="background-color:yellow;">-->
      <!--<p>hello world</p>-->
    <!--</div>-->
  <!--</div>-->

</div>


</body>
<script>
    $(document).ready(function(){
        $(".table_all_circuits").show();
        $(".table_owned").hide();
        $(".table_public").hide();
        $(".table_shared_circuits").hide();

        $("#all_circuit_div").click(function(){
            $(".table_owned").hide();
            $(".table_shared_circuits").hide();
            $(".table_public").hide();
            $(".table_all_circuits").show();
        });
        $("#profile_circuit_div").click(function(){
            $(".table_all_circuits").hide();
            $(".table_shared_circuits").hide();
            $(".table_owned").show();
        });
        $("#shared_circuit_div").click(function(){
            $(".table_all_circuits").hide();
            $(".table_owned").hide();
            $(".table_public").hide();
            $(".table_shared_circuits").show();
        });
        $("#public_circuit_div").click(function(){
            $(".table_all_circuits").hide();
            $(".table_owned").hide();
            $(".table_shared_circuits").hide();
            $(".table_public").show();
        });
    });
</script>
</html>