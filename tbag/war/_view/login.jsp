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
				font-family: Broadway;
				font-weight: bold;
				color: white;
			}
			
			#extrainfo {
				font-size: 18px;
				font-family: Broadway;
				margin-left: 50px;
				margin-right: 50px;
				color: white;
			} 
			
			#infolabel {
				font-size: 18px;
				font-family: Broadway;
				color: white;
			}
			
			.error {
				font-size: 16px;
				font-family: Broadway;
				font-weight: bold;
				color: red;
			}
		</style>
	</head>
	
	<body>
		<form action="${pageContext.servletContext.contextPath}/login" method="post">			
			<center>
				<p class="title">Welcome to the Text Based Adventure Game</p>
				<p id="extrainfo">Please Login to Continue</p>

				<c:if test="${! empty errorMessage}">
					<div class="error">${errorMessage}</div>
				</c:if>
				
				<table>
					<tr>
						<td id="infolabel">User Name:</td>
						<td><input type="text" name="username" size="12" value="${username}" /></td>
					</tr>
					<tr>
						<td id="infolabel">Password:</td>
						<td><input type="password" name="password" size="12" value="${password}" /></td>
					</tr>
				</table>
				
				<input type="Submit" name="login" value="Login">
				<input type="Submit" name="create" value="Create Account">
			</center>
		</form>
	</body>
</html>