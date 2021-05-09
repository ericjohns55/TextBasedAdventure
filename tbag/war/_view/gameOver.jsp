<!DOCTYPE html>

<html>
	<head>
		<title>Text Based Adventure End</title>
		
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
				font-size: 25px;
				font-family: Broadway;
				margin-left: 5px;
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
			#time{
				font-size: 24px;
				font-family: Broadway;
				color: white;
			}
		</style>
		
		<script type="text/javascript">
			function autoScroll() {
    			var textarea = document.getElementById("storyText");
    			textarea.scrollTop = textarea.scrollHeight;
    			if(textarea.selectionStart == textarea.selectionEnd) {
    				 
 				 }
			}
		</script>
		
		
	</head>

	<body onload="autoScroll();" >
		<form action="${pageContext.servletContext.contextPath}/gameOver" method="post">			
			<center>
				<p class="title">Welcome to the Text Based Adventure Game</p>

				<div>	
					<textarea readonly id="storyText" name="story" rows="16" cols="140">You walk out of the door and into a wooded area, you take a sigh of relief as you realize your nightmare is now over.</textarea>
				</div>
				

				<table>
					<tr>
						<td>
							<span id="extrainfo">100% complete</span>
						</td>
					</tr>
				</table>
				
				<input type="Submit" name="home" value="Home">
			</center>
		</form>
	</body>
</html>
