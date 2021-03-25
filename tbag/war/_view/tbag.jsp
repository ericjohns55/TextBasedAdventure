<!DOCTYPE html>

<html>
	<head>
		<title>Text Based Adventure</title>
		
		<style>
			body {
				background-color: black;
			}
			p.title {
				font-size: 28px;
				font-family: Arial;
				font-weight: bold;
				color: white;
			}
			
			#movescounter {
				font-size: 24px;
				font-family: Arial;
				margin-left: 50px;
				margin-right: 50px;
				color: white;
			} 
			
			#score {
				font-size: 24px;
				font-family: Arial;
				margin-left: 100px;
				margin-right: 100px;
				color: white;
			} 
			
			#timeleft {
				font-size: 24px;
				font-family: Arial;
				margin-left: 50px;
				margin-right: 50px;
				color: white;
			} 
			
			#stats {
				float: center;
			}
			
			#textarea {
				float: center;
			}
			
			#story {
				background-color: #101010;
				color: green;
			}
			
			#text {
				background-color: #101010;
				color: white;
			}
			
			#button {
				background-color: #303030;
				color: white;
				width: 150px;
				height: 25px;
			}
		</style>
		
		<script type="text/javascript">
			function refocus() {
				document.getElementById("userInput").focus();
			}
		</script>
	</head>

	<body>
		<form action="${pageContext.servletContext.contextPath}/tbag" method="post">			
			<center>
				<table>
					<tr>
						<p class="title">Welcome to the Text Based Adventure Game</p>
					</tr>
					
					<tr>
						<td>
							<span id="movescounter">${moves}</span>
						</td>
						<td>
							<span id="score">${score}</span>
						</td>
						<td>
							<span id="timeleft">${time}</span>
						</td>
					</tr>
				</table>
				
				<table>		
					<tr>
						<td><textarea readonly id="story" name="story" rows="16" cols="108">${story}</textarea></td>
					</tr>			
					<tr>
						<td><input id="text" type="text" name="userInput" size="140" value="${userInput}" onsubmit="refocus()"/></td>
					</tr>
				</table>
				
				<table>
					<tr>
						<td><input type="Submit" id="button" name="submit" value="Submit Text"></td>
						<td><input type="Submit" id="button" name="submit" value="Clear Game"></td>
					</tr>
				</table>
			</center>
		</form>
	</body>
</html>
