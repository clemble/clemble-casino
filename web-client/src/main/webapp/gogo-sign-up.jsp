<!DOCTYPE html>
<html>
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js">
</script>
<script>
$(document).ready(function(){
  $("#signUpButton").click(function(){
	$.ajax({
	  type: "POST",
	  url: "http://gogomaya.cloudfoundry.com/spi/registration/signin",
	  data: {
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
  	  },
	  success: function(data) {
	  	  alert("Server response: " + data);
	  },
	  error: function() {
	  	  alert("Errrrror");
	  },
	  beforeSend: function(xhrObj) {
      	xhrObj.setRequestHeader("Content-Type","application/json");
      	xhrObj.setRequestHeader("Accept","application/json");
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
		Birth date: <input type="text" id="birthDate" value="1/1/1999"><br>
		E-Mail: <input type="text" id="email" value="abc@abc.def"><br>
		Select Password: <input type="text" id="password" value="kukuruku"><br>
		<button id="signUpButton">Send</button>
	