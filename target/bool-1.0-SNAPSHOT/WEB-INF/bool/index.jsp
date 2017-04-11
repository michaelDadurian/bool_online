<!DOCTYPE html>

<html>

<head>
	<title>bool: The Online Circuit Builder</title>
	<link rel = "stylesheet" type="text/css" href="style/toolbar.css">
	<link rel = "stylesheet" type="text/css" href="style/workspace.css">
	<link rel = "stylesheet" type="text/css" href="style/attribute-editor.css">
	<link rel = "stylesheet" type="text/css" href="style/console.css">

	<script src = "script/jquery-3.2.0.min.js"></script> <!-- jQuery import -->
	<script src="script/jquery-ui.min.js"></script>
	<script src = "script/kinetic-v5.1.0/kinetic-v5.1.0.js"></script>
	<script src = "script/kinetic-v5.1.0/kinetic-v5.1.0.min.js"></script>

	
	<script src = "script/console.js"></script>
	<script src = "script/components.js"></script>
	<script src = "script/images.js"></script>
	<script src = "script/grid.js"></script>

</head>


<body>
	<div class = "toolbar"> <!-- Toolbar -->
		<ul>
			<li class="dropdown">
				<a href="#" class="dropbtn">Authenticate</a>
				<div class="dropdown-content">
		  			<a href="#">Login/Logout</span>
		  			<a href="splash/splash_screen.jsp">Exit</a>
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
				<img class = "c-icon" src="img/cmenu-and.png" id = "AND">
				<img class = "c-icon" src="img/cmenu-xor.png" id = "XOR">
				<img class = "c-icon" src="img/cmenu-or.png" id = "OR">
				<img class = "c-icon" src="img/cmenu-not.png" id = "NOT">
				<!--<img class = "c-icon" src="img/cmenu-nand.png" id = "NAND">
				<img class = "c-icon" src="img/cmenu-nor.png" id = "NOR">-->
			</div>

			<div class = "onebyone">
				<img class = "c-icon" src="img/cmenu-plus.png" id = "CROSS">
				<img class = "c-icon" src="img/cmenu-minus.png" id = "I">
				<img class = "c-icon" src="img/cmenu-t.png" id = "T">
				<img class = "c-icon" src="img/cmenu-l.png" id = "L">
			</div>

			<div class = "twobytwo">
				<img class = "c-icon" src="img/cmenu-onbox.png" id = "ON">
				<img class = "c-icon" src="img/cmenu-printbox.png" id = "PRINT">
				<img class = "c-icon" src="img/cmenu-varbox.png" id = "VAR">
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

	<script src = "script/canvas.js"></script>
	<script src = "script/attribute-editor.js"></script>
	<script src = "script/selection.js"></script>

	<script src = "script/evaluator.js"></script> <!-- Circuit Evaluator -->

	<script src = "script/toolbar.js"></script>

</body>

</html> 