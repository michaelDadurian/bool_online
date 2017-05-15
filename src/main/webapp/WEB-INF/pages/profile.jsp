<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<!DOCTYPE HTML>
<html lang="en">
<head>
  <title>Profile</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>



  <spring:url value = "/resources/workspace/script/jquery-3.2.0.min.js" var = "jqueryJS"/>
  <spring:url value = "/resources/workspace/script/jquery-ui.min.js" var = "jqueryUI"/>

  <spring:url value = "/resources/profile/script/delete.js" var = "deleteJS"/>
  <spring:url value = "/resources/profile/script/share.js" var = "shareJS"/>
  <spring:url value = "/resources/profile/script/get-link.js" var = "getLinkJS"/>
  <spring:url value = "/resources/profile/script/clone.js" var = "cloneJS"/>
  <spring:url value = "/resources/profile/script/upload-local.js" var = "uploadJS" />

  <spring:url value = "/resources/profile/css/upload.css" var = "uploadStyle" />
  <spring:url value = "/resources/profile/css/profile_style.css" var = "profileStyle" />
  <spring:url value = "/resources/profile/css/share-menu.css" var = "shareMenuStyle" />
  <spring:url value = "/resources/profile/css/get-link.css" var = "linkMenuStyle" />

  <script src="${jqueryJS}"></script>
  <script src="${jqueryUI}"></script>

  <script src="${deleteJS}"></script>
  <script src="${shareJS}"></script>
  <script src="${getLinkJS}"></script>
  <script src="${cloneJS}"></script>
  <script src="${uploadJS}"></script>

  <link href = "${uploadStyle}" rel = "stylesheet" />
  <link href = "${profileStyle}" rel = "stylesheet" />
  <link href = "${shareMenuStyle}" rel = "stylesheet" />
  <link href = "${linkMenuStyle}" rel = "stylesheet" />



</head>
<body>


<div class="container" style="background-color: #FDFFFF">
  <div class="row">
    <div class=" col-md-2 col-sm-2">
            <a href="/"><img src= "/resources/profile/img/bool_logo.png" class="img-rounded" alt="bool_logo" height="75%" width="75%"></a>
    </div>
    <div class="col-md-8 col-sm-8">
        <form action="/profile/submitSearch" modelAttribute="searchParams" method="get" id="search_input">
          <div id="custom-search-input">
            <div class="input-group col-md-12">
              <input type="text" class="search-query form-control" placeholder="Search" name="searchParams" />
              <span class="input-group-btn">
                  <button class="btn btn-danger" type="button"><span class=" glyphicon glyphicon-search"></span></button>
                </span>
            </div>
          </div>
        </form>
      <a href="/workspace"><button type="button" class="btn btn-primary btn-xs btn-custom">New</button></a>

      <a href="#">
        <label for = "loader" class = "btn btn-warning btn-xs btn-custom loading">Upload</label>
        <input id = "loader" type="file">
      </a>
    </div>
    <div class="col-md-1 col-sm-1">
      <span class="span_filler">
        <!--###added span class here-->
      </span>
      <div class ="notifications_dropdown">
        <img src="/resources/profile/img/bell1.png" class="dropdown-toggle" type="button" data-toggle="dropdown">
        <ul class="dropdown-menu">
          <li class="dropdown-header"><a href="/profile/notifications">Notifications</a></li>
            <c:forEach items="${notificationNames}" var="notificationName" varStatus="status">
              <li class="dropdown_style"><a href="/profile/loadCircuitFromNotification?searchParams=owner%3A${notificationOwners[status.index]}+${notificationName}"> ${notificationOwners[status.index]} has shared ${notificationName} with you! </a></li>
            </c:forEach>
        </ul>
      </div>
  </div>
    <!--User dropdown -->
    <div class="col-md-1 col-sm-1">
      <span class="span_filler_1">
          <!--###added span class here-->
      </span>

      
      <div class="profile_logout">
        <a href="/profile/logout">Log Out</a>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-xl-2 col-md-2 col-sm-3">
      <div id="sidebar-wrap">
        <ul class="sidebar"style="font-style: normal">
          <li style="list-style-type: none">
            <div id="all_circuit_div"><a href="/profile">All Circuit</a></div>
          </li>
          <li style="list-style-type: none">
            <div id="profile_circuit_div"><a href="/profile/submitSearch?searchParams=owner%3A${currUser}">Owned By Me</a></div>
          </li>
          <li style="list-style-type: none">
            <div id="shared_circuit_div"><a href="/profile/submitSearch?searchParams=shared%3A${currUser}">Shared with Me</a></div>
          </li>
          <li style="list-style-type: none">
            <div id="public_circuit_div"><a href="/profile/submitSearch?searchParams=%23public">Public</a></div>
          </li>
        </ul>
      </div>
    </div>
    <div class="col-xl-10 col-md-10 col-sm-9">
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
              <c:forEach items="${circuitNames}" var="circuitName" varStatus="status">
                  <tr class="profileRow${status.index}">

                    <td align="center">

                      <c:if test="${canOpen[status.index] eq 'true'}">
                          <a href = "#" class="open-button">
                            <span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span>
                          </a>
                      </c:if>

                      <c:if test="${canGetLink[status.index] eq 'true'}">
                          <a href = "#" class="get-link-button" onclick="getLink(this)">
                            <span class="glyphicon glyphicon-link" aria-hidden="true"></span>
                          </a>
                      </c:if>


                      <c:if test="${canShare[status.index] eq 'true'}">

                          <a href = "#" class="share-button" onclick="getCircuit(this)">
                            <span class="glyphicon glyphicon-share" aria-hidden="true"></span>
                          </a>

                      </c:if>


                      <c:if test="${canDelete[status.index] eq 'true'}">
                          <a href= "#" class="delete-button" onclick="deleteProfileRow(this)">
                              <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                          </a>
                      </c:if>

                      <c:if test="${canClone[status.index] eq 'true'}">

                          <a href="#" class="clone-button" onclick="cloneCircuit(this)" >
                            <span class="glyphicon glyphicon-screenshot" aria-hidden="true"></span>
                          </a>

                      </c:if>



                    </td>
                    <td class="circuitNames">${circuitName}</td>
                    <td class="circuitOwners">${circuitOwners[status.index]}</td>


                  </tr>
              </c:forEach>

              </tbody>
            </table>

          </div>
      </section>


<div class = "share-menu">

    <p>Edit Shared</p>

    <div id = "edit-shared">
        <p class="circuit-name"></p>
        <p class="circuit-owner"></p>
        <p class="circuit-tags"></p>
        <form>
            <span class = "desc">Public</span>
            <input type = "radio" class = "privacy" name = "privacy" id = "public">
            <span class = "desc">Private</span>
            <input type = "radio" class = "privacy" name = "privacy" id = "private">
        </form>
        <form action="/profile/share" modelAttribute="sharedAttribute" method="get" id="profile-share">
            <input type = "text" name = "sharedAttribute" id="shared-text">
        </form>
    </div>


    <div id = "submit-shared">
        <button type = "button" onclick = "confirmEdit()">Submit</button>
        <button type = "button" onclick = "hideShareMenu()">Cancel</button>
    </div>
</div>

<div class = "get-link-menu"
    <p>Shareable Link</p>

    <div id = "link-display">
        <form>
            <input type = "textarea" name = "link-text" id="shareable-link">

        </form>
    </div>

    <div id = "close-link-menu">
        <button type = "button" onclick = "hideLinkMenu()">Close</button>
    </div>

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
