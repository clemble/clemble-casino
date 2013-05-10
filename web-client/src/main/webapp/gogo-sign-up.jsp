<!DOCTYPE html>
<html>
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js">
</script>
<script>
$(document).ready(function(){
  $("#signUpButton").click(function(){
	localStorage.setItem('email', $("#email").val());
	$.ajax({
	  contentType: "application/json",
	  type: "POST",
	  url: "http://gogomaya.cloudfoundry.com/spi/registration/signin",
//	  url: "http://10.0.0.13:8080/gogomaya-web/spi/registration/signin",
	  data: JSON.stringify({
   	   	playerProfile : {
  	      	nickName : $("#nickName").val(),
  	      	firstName : $("#firstName").val(),
  	      	lastName : $("#lastName").val(),
  	      	gender : $("#gender").val(),
  	      	birthDate : $("#birthDate").val(),
  	      	category : "Novice"
  	   	},
  	   	playerCredential: {
  	      	email : $("#email").val(),
  	      	password : $("#password").val()
  	   	}
  	  }, null, 2),
	  success: function(data, textStatus, jqXHR ) {
		  localStorage.setItem('playerId', data.playerId);
		  //alert("Sign Un Successful. playerId = "+ data.playerId);
		  window.location.href="./specifications.html";
	  },
	  error: function(data) {
	  	  alert("Errrrror " + JSON.stringify(data, null, 2));
	  }
	});
  });
});
</script>
</head>
<title>Go-Go Sign Up</title>

<body>
	<h1>Sign Up</h1>
		First name: <input type="text" id="firstName" value="kukuruku"><br>
		Last name: <input type="text" id="lastName" value="kukuruku"><br>
		Nick name: <input type="text" id="nickName" value="kukuruku"><br>
		Gender: <input type="text" id="gender" value="M"><br>
		Birth date: <input type="text" id="birthDate" value="1/1/1990"><br>
		E-Mail: <input type="text" id="email" value="abc@abc.def"><br>
		Select Password: <input type="text" id="password" value="kukuruku"><br>
		<button id="signUpButton">Send</button>
		<p>
		<a href="login.html">Log In</a>
</body>
</html>
	