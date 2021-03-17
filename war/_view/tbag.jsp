<!DOCTYPE html>

<html>
	<head>
		<title>Index view</title>
	</head>

	<body>
		<form action="${pageContext.servletContext.contextPath}/tbag" method="post">
			<p>Welcome to Text Based Adventure Game</p> <br><br>
			
			<table>
				<tr>
					<td><textarea readonly id="story" name="story" rows="16" cols="108">${story}</textarea></td>
				</tr>
				<tr>
					<td><input type="text" name="userInput" size="120" value="${userInput}" /></td>
				</tr>
				<tr>
					<td><input type="Submit" name="submit" value="Tell me what the fuck I just submitted"></td>
				</tr>
				<tr>
					<td><input type="Submit" name="submit" value="Clear Game"></td>
				</tr>
			</table>
		</form>
	</body>
</html>
