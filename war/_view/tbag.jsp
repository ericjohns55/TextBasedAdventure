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
					<td><input type="text" name="userInput" size="120" value="${userInput}" /></td>
				</tr>
				<tr>
					<td><input type="Submit" name="submit" value="Tell me what the fuck I just submitted"></td>
				</tr>
				<tr>
					<td><p name="output">${output}</p></td>
				</tr>
				<tr>
					<td><p name="action">${action}</p></td>
				</tr>
				<tr>
					<td><p name="noun">${noun}</p></td>
				</tr>
			</table>
		</form>
	</body>
</html>
