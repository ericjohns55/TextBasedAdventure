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
			
			#stats {
				float: center;
			}
			
			#textarea {
				float: center;
			}
			
			#storyText {
				background-color: #101010;
				color: green;
				overflow: hidden;
				overflow-y: scroll;
			}
			
			#commandText {
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
			
			#time {
				font-size: 24px;
				font-family: Broadway;
				color: white;
			}
			
			::-webkit-scrollbar {
    			display:none;
			}
		</style>
		
		<script type="text/javascript">
			function autoScroll() {
    			var textarea = document.getElementById("storyText");
    			textarea.scrollTop = textarea.scrollHeight;
			}
		</script>
	</head>

	<body onload="autoScroll();" >
		<form action="${pageContext.servletContext.contextPath}/game" method="post">			
			<center>
				<p class="title">Welcome to the Text Based Adventure Game</p>

				<div>	
					<textarea readonly id="storyText" name="story" rows="16" cols="140">${story}</textarea>
					<input id="text" type="text" name="userInput" size="140" value="${userInput}" autofocus/>
				</div>
				
				<table>
					<tr>
						<td>
							<span id="extrainfo">${moves}</span>
						</td>
						<td>
							<span id="extrainfo">${room}</span>
						</td>
					</tr>
				</table>
			</center>
		</form>
	</body>
</html>
