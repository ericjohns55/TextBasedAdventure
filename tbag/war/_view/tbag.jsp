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
			
			#extrainfo {
				font-size: 18px;
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
			
			#storyText {
				background-color: #101010;
				color: green;
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
			#time{
				font-size: 24px;
				font-family: Arial;
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
			
			
			/*
			function startTimer(duration, display) {
    			var timer = duration, minutes, seconds;
   				setInterval(function () {
        			minutes = parseInt(timer / 60, 10);
        			seconds = parseInt(timer % 60, 10);

        			//minutes = minutes < 10 ? "0" + minutes : minutes;
        			//seconds = seconds < 10 ? "0" + seconds : seconds;

        			rem = document.getElementById("timeRem");
    				rem.value = minutes*60 + seconds;

       				display.textContent = minutes + ":" + seconds;
       				display.value = minutes + ":" + seconds;

        			if (--timer < 0) {
            			timer = duration;
        			}
    			}, 1000);
			}

			function start() {
    			
    			display = document.getElementById("time");
    			console.log("${duration} aa");
    			startTimer(${duration}, display);
			}; */
		</script>
		
		
	</head>

	<body onload="autoScroll();" >
		<form action="${pageContext.servletContext.contextPath}/tbag" method="post">			
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
