<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>

<html>

<head>
	<title>bool: The Online Circuit Builder</title>

	<script>var pageContext = "${pageContext.request.contextPath}";</script>

	<spring:url value = "resources/workspace/stylesheets/attribute-editor.css" var = "attributeCSS" />
    <spring:url value = "resources/workspace/stylesheets/workspace.css" var = "workspaceCSS"/>
    <spring:url value = "resources/workspace/stylesheets/toolbar.css" var = "toolbarCSS"/>
    <spring:url value = "resources/workspace/stylesheets/console.css" var = "consoleCSS"/>

    <spring:url value = "resources/workspace/scripts/jquery-3.2.0.min.js" var = "jqueryJS"/>
    <spring:url value = "resources/workspace/scripts/jquery-ui.min.js" var = "jqueryUI"/>
    <spring:url value = "resources/workspace/scripts/kinetic-v5.1.0/kinetic-v5.1.0.js" var = "kineticJS" />
    <spring:url value = "resources/workspace/scripts/kinetic-v5.1.0/kinetic-v5.1.0.min.js" var = "kineticMinJS" />

    <spring:url value = "resources/workspace/scripts/console.js" var = "consoleJS" />
    <spring:url value = "resources/workspace/scripts/components.js" var = "componentsJS" />
    <spring:url value = "resources/workspace/scripts/images.js" var = "imagesJS" />
    <spring:url value = "resources/workspace/scripts/grid.js" var = "gridJS" />


    <link href = "${attributeCSS}" rel = "stylesheet" />
    <link href = "${workspaceCSS}" rel = "stylesheet" />
    <link href = "${toolbarCSS}" rel = "stylesheet" />
    <link href = "${consoleCSS}" rel = "stylesheet" />

    <script src="${jqueryJS}"></script>
    <script src="${jqueryUI}"></script>
    <script src="${kineticJS}"></script>
    <script src="${kineticMinJS}"></script>
    <script src="${consoleJS}"></script>
    <script src="${componentsJS}"></script>
    <script src="${imagesJS}"></script>
    <script src="${gridJS}"></script>


</head>


<body>
	<div class = "toolbar"> <!-- Toolbar -->
		<ul>
			<li class="dropdown">
				<a href="#" class="dropbtn">Authenticate</a>
				<div class="dropdown-content">
		  			<a href="#">Login/Logout</span>
		  			<a href="/">Exit</a>
				</div>
			</li>
			<li class="dropdown">
				<a href="#" class="dropbtn">File</a>
				<div class="dropdown-content">
		  			<a href="#">New</a>
		  			<a href="#">Save</a>
		  			<a href="#">Load</a>
		  			<a href="#">Submit</a>
		  			<a id="run" href="doskopdk">Run</a>
		  			<a href="#">Toggle Dynamic Evaluator</a>
				</div>
			</li>
			<li class="dropdown">
			<a href="#" class="dropbtn">Edit</a>
				<div class="dropdown-content">
		  			<a href="#">Undo  ctrl-z</a>
		  			<a href="#">Redo  ctrl-y</a>
		  			<a href="#">Cut   ctrl-x</a>
		  			<a href="#">Copy  ctrl-c</a>
		  			<a href="#">Paste ctrl-v</a>
				</div>
			</li>
		</ul>
	</div>

	<div class = "workspace">
		<div class = "grid"> <!-- Grid interface --> 

			<canvas id = "grid-render"></canvas>

		</div>

		<div class = "cmenu"> <!-- Component menu -->

			<div class = "twobytwo">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-and.png" id = "AND">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-xor.png" id = "XOR">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-or.png" id = "OR">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-not.png" id = "NOT">
				<!--<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-nand.png" id = "NAND">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-nor.png" id = "NOR">-->
			</div>

			<div class = "onebyone">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-plus.png" id = "CROSS">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-minus.png" id = "I">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-t.png" id = "T">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-l.png" id = "L">
			</div>

			<div class = "twobytwo">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-onbox.png" id = "ON">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-printbox.png" id = "PRINT">
				<img class = "c-icon" src="resources/workspace/stylesheets/img/cmenu-varbox.png" id = "VAR">
			</div>
			
		</div>

		<div class = "console"> <!-- Console -->

			<div class = "console-top">

				<div class = "console-trigger" id = "debug-trigger">
					<span class = "console-label">Debug</span>
				</div>

				<div class = "console-trigger" id = "truth-table-trigger">
					<span class = "console-label">Truth Table</span>
				</div>

				<div class = "console-trigger" id = "boolean-trigger">
					<span class = "console-label">Boolean Equations</span>
				</div>

				<div class = "name-container" id = "name-container">		
					<span class = "name-label">Circuit Name</span>
					<input id = "name-input" class = "name-input-textbox" type = "text" name = "circuit-name" value = "">
				</div>

			</div>

			<div class = "console-bottom" id = "debug">
				<p class = "debug-msg"> Welcome to <i>bool</i>! The Online Circuit Builder</p>
			</div>
			

		</div>

	</div>

	<div class = "attribute-editor"> <!-- Attribute Editor -->
			<p class = "title">Modify Component Attribute</p>

			<div class = "ae-input" id = "ae-details">
				<p id = "detail-desc"></p>
			</div>

			<div class = "ae-input" id = "ae-delay">		
				<span class = "ae-desc">Delay</span>
				<input id = "ae-delay-text" class = "ae-textbox" type = "text" name = "delay" value = "">
			</div>

			<div class = "ae-input" id = "ae-print">
				<span class = "ae-desc">Message</span>
				<input id = "ae-message-text" class = "ae-textbox" type = "text" name = "print" value = "">		
			</div>

			<div class = "ae-input" id = "ae-label">
				<span class = "ae-desc">Label</span> 
				<input id = "ae-label-text" class = "ae-textbox" type = "text" name = "label" value = "">
			</div>

			<div class = "button-container">	
				<div class = "ae-button" id = "rotate"></div>
				<div class = "ae-button" id = "delete"></div>
				<div class = "ae-button" id = "save"></div>
			</div>
	</div>

    <spring:url value = "resources/workspace/scripts/canvas.js" var = "canvas" />
    <spring:url value = "resources/workspace/scripts/attribute-editor.js" var = "attributeEditor" />
    <spring:url value = "resources/workspace/scripts/selection.js" var = "selection" />
    <spring:url value = "resources/workspace/scripts/evaluator.js" var = "evaluator" />
    <spring:url value = "resources/workspace/scripts/toolbar.js" var = "toolbar" />

    <script src="${canvas}"></script>
    <script src="${attributeEditor}"></script>
    <script src="${selection}"></script>
    <script src="${evaluator}"></script>
    <script src="${toolbar}"></script>
</body>

</html> 