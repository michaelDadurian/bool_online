<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>

<head>
	<title>bool: The Online Circuit Builder</title>

    <link rel="icon"
    type="favicon.ico"
    href="favicon.ico">

	<spring:url value = "/resources/workspace/style/toolbar.css" var = "toolbarCSS" />
	<spring:url value = "/resources/workspace/style/workspace.css" var = "workspaceCSS" />
	<spring:url value = "/resources/workspace/style/attribute-editor.css" var = "attributeCSS" />
	<spring:url value = "/resources/workspace/style/console.css" var = "consoleCSS" />
	<spring:url value = "/resources/workspace/style/submit.css" var = "submitCSS" />
	<spring:url value = "/resources/workspace/style/user-menu.css" var = "userMenuCSS" />
	<spring:url value = "/resources/workspace/style/quizlet.css" var = "quizletCSS" />
	<spring:url value = "/resources/workspace/style/settings-menu.css" var = "settingsCSS" />

    <spring:url value = "/resources/workspace/script/jquery-3.2.0.min.js" var = "jqueryJS"/>
    <spring:url value = "/resources/workspace/script/jquery-ui.min.js" var = "jqueryUI"/>
    <spring:url value = "/resources/workspace/script/kinetic-v5.1.0/kinetic-v5.1.0.js" var = "kineticJS" />
    <spring:url value = "/resources/workspace/script/kinetic-v5.1.0/kinetic-v5.1.0.min.js" var = "kineticMinJS" />


    <spring:url value = "/resources/workspace/script/global.js" var = "globalJS" />
    <spring:url value = "/resources/workspace/script/console.js" var = "consoleJS" />
    <spring:url value = "/resources/workspace/script/components.js" var = "componentsJS" />
    <spring:url value = "/resources/workspace/script/images.js" var = "imagesJS" />
    <spring:url value = "/resources/workspace/script/grid.js" var = "gridJS" />
    <spring:url value = "/resources/workspace/script/submit.js" var = "submitJS" />
    <spring:url value = "/resources/workspace/script/settings-menu.js" var = "settingsJS" />

	<link href = "${toolbarCSS}" rel = "stylesheet" />
	<link href = "${workspaceCSS}" rel = "stylesheet" />
	<link href = "${attributeCSS}" rel = "stylesheet" />
	<link href = "${consoleCSS}" rel = "stylesheet" />
	<link href = "${submitCSS}" rel = "stylesheet" />
	<link href = "${userMenuCSS}" rel = "stylesheet" />
	<link href = "${quizletCSS}" rel = "stylesheet" />
	<link href = "${settingsCSS}" rel = "stylesheet" />

    <script src="${jqueryJS}"></script>
    <script src="${jqueryUI}"></script>
    <script src="${kineticJS}"></script>
    <script src="${kineticMinJS}"></script>

	<script src="${globalJS}"></script>
	<script src="${consoleJS}"></script>
	<script src="${componentsJS}"></script>
	<script src="${imagesJS}"></script>
	<script src="${gridJS}"></script>
	<script src="${submitJS}"></script>
	<script src="${settingsJS}"></script>



</head>


<body>

	<div class = "user-menu">
		<div class = "auth">
		</div>
	</div>

	<div class = "toolbar"> <!-- Toolbar -->
		<ul>
			<li class="dropdown">
				<a href="#" class="dropbtn">Authenticate</a>
				<div class="dropdown-content">
		  			<a href="/workspace/toggleLoginLogout">Login/Logout | ${currUser}</span>
		  			<a href="/profile">Profile</a>
		  			<a href="/">Exit</a>
				</div>
			</li>
			<li class="dropdown">
				<a href="#" class="dropbtn">File</a>
				<div class="dropdown-content">
		  			<a href="">New Circuit Workspace</a>
		  			<a href="#" onclick = "promptSettingsMenu()">Settings</a>
		  			<a href="#" onclick = "promptQuizletMaker()">Quizlet Constraints</a>
		  			<a href="#" onclick = "writetofile()">Save</a>
		  			<a href="#">
		  				<label for = "loader" class = "loading">Load</label>
		  				<input id = "loader" type="file">
		  			</a>
		  			<a href = "#" onclick = "promptSubmitMenu()">Submit</a>
		  			<!--<a id="run">Run</a>-->
		  			<a href="#" onclick = "flickStopCircuitEvaluation()">Reset Circuit Evaluation</a>
				</div>
			</li>
			<li class="dropdown">
			<a href="#" class="dropbtn">Edit</a>
				<div class="dropdown-content">
		  			<a href="#" onclick = "undo()">Undo<span class="hotkey">ctrl-z</span></a>
		  			<a href="#" onclick = "redo()">Redo<span class="hotkey">ctrl-y</span></a>
		  			<a href="#" onclick = "cut()">Cut<span class="hotkey"> ctrl-x</span></a>
		  			<a href="#" onclick = "copyToClipBoard()">Copy<span class="hotkey">ctrl-c</span></a>
		  			<a href="#" onclick = "pasteToWorkspace()">Paste <span class="hotkey">ctrl-v</span></a>
		  			<a href="#" onclick = "rotateAxis()">Rotate All<span class="hotkey"></span></a>
		  			<a href="#" onclick = "rotateSelected()">Rotate Indivually<span class="hotkey">r</span></a>
		  			<a href="#" onclick = "deleteSelected()">Delete Selected<span class="hotkey">del</span></a>
				</div>
			</li>
		</ul>
	</div>

	<div class = "workspace">
		<div class = "grid"> <!-- Grid interface --> 

			<canvas id = "grid-render"></canvas>

		</div>

		<div class = "cmenu"> <!-- Component menu -->

			<div class = "title">Components</div>

			<div class = "twobytwo">
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/and.png" id = "AND"> <p>and</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/xor.png" id = "XOR"> <p>xor</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/or.png" id = "OR"> <p>or</p>
				</div>
			</div>

			<div class = "onebyone">
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/not.png" id = "NOT"> <p>not</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/buffer.png" id = "BUFFER"> <p>buffer</p>
				</div>
				<div class = "cmenu-comp"> 
					<img class = "c-icon" src="/resources/workspace/img/c-menu/cross.png" id = "CROSS"> <p>wire</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/minus.png" id = "I"> <p>wire</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/t.png" id = "T"> <p>wire</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/l.png" id = "L"> <p>wire</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/crossing.png" id = "CROSSING"> <p>crossing</p>
				</div>
			</div>

			<div class = "onebyone">
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/print.png" id = "PRINT"> <p>print</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/switch.png" id = "SWITCH"> <p>switch</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/light.png" id = "LIGHT"> <p>light</p>
				</div>
				<div class = "cmenu-comp">
					<img class = "c-icon" src="/resources/workspace/img/c-menu/eq.png" id = "EQUATION"> <p>boolean eq</p>
				</div>
			</div>
			
		</div>

		<div class = "console"> <!-- Console -->

			<div class = "console-top">

				<div class = "console-trigger on" id = "debug-trigger">
					<span class = "console-label">Debug</span>
				</div>

				<div class = "console-trigger" id = "truth-table-trigger">
					<span class = "console-label">Truth Table</span>
				</div>

				<div class = "console-trigger" id = "boolean-trigger">
					<span class = "console-label">Boolean Equations</span>
				</div>

				<div class = "console-trigger" id = "quizlet-trigger">
					<span class = "console-label">Quizlet</span>
				</div>

				<div id = "clear-console" class = "bool-button">Clear Debug</div>

			</div>

			<div class = "console-bottom show" id = "debug">
				<p class = "debug-msg"> Welcome to <i>bool</i>! The Online Circuit Builder</p>
				
				<div id = "debug-printed">
				<!--
				<p class = "debug-msg"> To get started, drag and drop some circuit components onto the grid. Then go to file -> run and let the simulation begin!</p>
				<p class = "debug-msg"> To edit any of the circuit components attributes, right click the component on the grid and change away!</p>
				-->
				</div>
			</div>
			
			<div class = "console-bottom" id = "truth-table">
				<div class = "build-circuit">
					<div class = "left">
						<div class = "bool-button" onclick = "eqToTable()">Build Table</div>
						<div class = "bool-button" onclick = "truthTableToBool()"">Build Equation</div>	

						<!--<br>

						<div class = "bool-button" id = "toggle-table" onclick = "toggleTruthTableTextBox()">
							Show Textbox
						</div>		
						-->			
					</div>
					<!--
					<div class = "right">
						<div class = "bool-button" onclick = "addColumnToTruthTable()">
						Add Column
						</div>
						<div class = "bool-button" onclick = "deleteColumnFromTruthTable()">Delete Column</div>
						<div class = "bool-button">Reset Table</div>
					</div>
					-->
				</div>
				<!--
				<div id = "input">
					<div id = "table">					
						<table class = "table-input">
						  <tr class = "header">
						    <th>-</th>
						    <th>A</th>					    	 
						  </tr>	
						  <tr class = "tt-row">
						  	<td class = "row-num">1</td>
						  	<td>0</td>
						  </tr>	  
						  <tr class = "tt-row">
						  	<td class = "row-num">2</td>
						  	<td>0</td>
						  </tr>	
						</table>
					</div>
					<div id = "textbox show">
						<textarea id = "truth-tb">
							00000
						</textarea>
					</div>		
				</div>		
				-->
				<div id = "alt-textbox">
					<textarea id = "truth-tb">- A B / 
1 0 0 / 
2 0 0
					</textarea>
				</div>		
			</div>

			<div class = "console-bottom" id = "boolean">
				<div class = "build-circuit">
					<div class = "bool-button" onclick = "makeCircuitFromBoolEq()">Make Circuit</div>
					<div class = "bool-button" onclick = "makeBoolEqFromCircuit()">Make Boolean Eq</div>
				</div>
				<div id = "textbox">
					<textarea id = "boolean-tb">!(D+!B) + B * !(!A + B)</textarea>
				</div>
			</div>

			<div class = "console-bottom" id = "quizlet">

				<div class = "info-pane">
					<div id = "solution-display"><img class = "c-icon" src="/resources/workspace/img/console/xmark.png"></div>
					<div id = "check-solution" class = "bool-button" onclick = "evaluateAndShowQuizlet()">Check Solution</div>
					<p class = "problem">Problem:</p>
					<p class = "problem-desc"></p>

					<p class = "desc">Description: </p>
					<p class = "desc-desc"></p>
				</div>

				<div class = "row">
					
				</div>

			

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
				<div class = "ae-button" id = "flip"></div>
				<div class = "ae-button" id = "rotate"></div>
				<div class = "ae-button" id = "delete"></div>
				<div class = "ae-button" id = "save"></div>
			</div>
	</div>

	<div class = "blanket" onclick = "hideAllMenus()"></div>

	<div class = "submit-menu">
		
		<p>Submit Circuit To Profile</p>

		<div id = "div-save">
			<form>
				<span class = "desc">Save</span>
				<input type = "radio" class = "save" name = "privacy" id = "save-edit" checked = "checked">
				<span class = "desc">Save As</span>
				<input type = "radio" class = "save" name = "privacy" id = "save-copy">
			</form>
		</div>

		<div id = "div-privacy">
			<form>
				<span class = "desc">Public</span>
				<input type = "radio" class = "privacy" name = "privacy" id = "public" checked = "checked">
				<span class = "desc">Private</span>
				<input type = "radio" class = "privacy" name = "privacy" id = "private">
			</form>
		</div>

		<div id = "div-name">
			<span class = "desc">Name</span>
			<input id = "circuit-name" class = "textbox" type = "text" name = "circuit-name" value = "">				
		</div>

		<div id = "div-share">
			<span class = "desc">Share</span>
			<input id = "circuit-share" class = "textbox" type = "text" name = "circuit-share" value = "">				
		</div>

		<div id = "div-tags">
			<span class = "desc">Tags</span>
			<input id = "circuit-tags" class = "textbox" type = "text" name = "circuit-tags" value = "">				
		</div>

		<div id = "div-submit">
			<button type = "button" onclick = "submitToDatastore()">Submit</button>
			<button type = "button" onclick = "hideSubmitMenu()">Cancel</button>
		</div>
	</div>

	<div class = "quizlet-maker">
		
		<p class = "title">Quizlet Constraints</p>

		<div id = "constraints">
			
			<!--<div>
				<span class = "constraint-label">AND GATE</span>
				<span class = 'constraint-input-right'>
					<input id = "and-constraint" class = "textbox" type = "text" name = "and-constraint" value = "">
					<button class = "constraint-button" id = "and-constraint-up">+</button>
					<button class = "constraint-button" id = "and-constraint-down">-</button>
					<button class = "constraint-button" id = "and-constraint-inf">*</button>	
				</span>
			</div>
			-->
			
		</div>

		<div class = "non-constraints">

			<div id = "div-problem">
				<span class = "desc">Problem</span>
				<textarea id = "quizlet-problem"></textarea>
				<!--<input id = "quizlet-problem" class = "textbox" type = "text" name = "quizlet-problem" value = "">-->				
			</div>

			<div id = "div-desc">
				<span class = "desc">Description</span>
				<textarea id = "quizlet-desc"></textarea>
				<!--<input id = "quizlet-desc" class = "textbox" type = "text" name = "quizlet-desc" value = "">-->				
			</div>

			<div id = "div-answer">
				<span class = "desc">Answer</span>
				<textarea id = "quizlet-answer"></textarea>
				<!--<input id = "quizlet-answer" class = "textbox" type = "text" name = "quizlet-answer" value = "">-->			
			</div>

		</div>

		<div id = "div-submit">
			<button type = "button" onclick = "submitQuizletConstraints()">Submit</button>
			<button type = "button" onclick = "hideQuizletMaker()">Cancel</button>
		</div>
	</div>

	<div class = "settings">
		
		<p class = "title">Settings</p>

		<div id = "loops">
			<span class = "desc">Loop before end evaluation</span>
			<input class = "textbox" type = "text" name = "circuit-tags" value = "">				
		</div>

		<div id = "delay-time">
			<span class = "desc">Default Delay Time</span>
			<input class = "textbox" type = "text" name = "circuit-tags" value = "">				
		</div>

		<div id = "display-delay">
			<span class = "desc">Display Delays</span>
			<input type="checkbox"> 				
		</div>

		<div id = "display-labels">
			<span class = "desc">Display Labels</span>
			<input type="checkbox"> 				
		</div>

		<div id = "display-lines">
			<span class = "desc">Display Grid Lines</span>
			<input id="display-delay" type="checkbox"> 				
		</div>

		<div id = "display-numbers">
			<span class = "desc">Display Grid Numbers</span>
			<input type="checkbox"> 				
		</div>

		<div id = "div-submit">
			<button type = "button" onclick = "submitSettings()">Submit</button>
			<button type = "button" onclick = "hideSettingsMenu()">Cancel</button>
		</div>
	</div>


	<spring:url value = "/resources/workspace/script/canvas.js" var = "canvasJS"/>
	<spring:url value = "/resources/workspace/script/attribute-editor.js" var = "attributeJS"/>
	<spring:url value = "/resources/workspace/script/selection.js" var = "selectionJS"/>

	<spring:url value = "/resources/workspace/script/evaluator.js" var = "evaluatorJS"/> <!-- Circuit Evaluator -->
	<spring:url value = "/resources/workspace/script/saving.js" var = "savingJS"/>

	<spring:url value = "/resources/workspace/script/toolbar.js" var = "toolbarJS"/>
	
	<spring:url value = "/resources/workspace/script/qm.js" var = "qmJS"/>
	<spring:url value = "/resources/workspace/script/truth-table.js" var = "truthJS"/>
	<spring:url value = "/resources/workspace/script/boolean-eq.js" var = "booleanJS"/>
	<spring:url value = "/resources/workspace/script/quizlet.js" var = "quizletJS"/>
	<spring:url value = "/resources/workspace/script/pathfinder.js" var = "pathfindingJS"/>
	<spring:url value = "/resources/workspace/script/load.js" var = "loadJS"/>


    <script src="${canvasJS}"></script>
    <script src="${attributeJS}"></script>
    <script src="${selectionJS}"></script>

    <script src="${evaluatorJS}"></script>
    <script src="${savingJS}"></script>

    <script src="${toolbarJS}"></script>

    <script src="${qmJS}"></script>
    <script src="${truthJS}"></script>
    <script src="${booleanJS}"></script>
    <script src="${quizletJS}"></script>
    <script src="${pathfindingJS}"></script>
    <script src="${loadJS}"></script>

</body>

</html> 