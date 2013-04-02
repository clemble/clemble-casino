<html>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
<script src="stomp.js"></script>

<style>
#cnvs {
	border: none;
	-moz-border-radius: 4px;
	cursor: url(pencil.cur), crosshair;
	position: absolute;
	overflow: hidden;
	width: 100%;
	height: 100%;
}

#cnvs:active {
	cursor: url(pencil.cur), crosshair;
}

body {
	overflow: hidden;
}
</style>
<title>RabbitMQ Web STOMP Examples: Bunny Drawing</title>
<link href="main.css" rel="stylesheet" type="text/css" />
</head>
<body lang="en">
	<h1>
		<a href="index.html">RabbitMQ Web STOMP Examples</a> > Bunny Drawing
	</h1>
	<canvas id="cnvs"></canvas>
	<script>
var send; var draw;
send = draw = function(){};

var lines = [];

var canvas = document.getElementById('cnvs');

if (canvas.getContext) {
  var ctx = canvas.getContext('2d');

  var img = new Image();
  img.onload = function() {
    ctx.drawImage(img, 230, 160);
  };
  img.src = 'bunny.png';

  draw = function(p) {
    ctx.beginPath();
    ctx.moveTo(p.x1, p.y1);
    ctx.lineTo(p.x2, p.y2);
    ctx.stroke();
    ctx.drawImage(img, 230, 160);
  };

  var do_resize = function() {
    canvas.width  = window.innerWidth;
    canvas.height = window.innerHeight;

    ctx.font = "bold 20px sans-serif";
    ctx.fillStyle = "#444";
    ctx.fillText("Draw wings on the bunny!", 260, 100);
    ctx.font = "normal 16px sans-serif";
    ctx.fillStyle = "#888";
    ctx.fillText("(For more fun open a second browser)", 255, 130);

    ctx.drawImage(img, 230, 160);

    ctx.strokeStyle = "#fa0";
    ctx.lineWidth = "10";
    ctx.lineCap = "round";

    $.map(lines, function (p) {
      draw(p);
    });
  };

  $(window).resize(do_resize);
  $(do_resize);


  var pos = $('#cnvs').position();
  var prev = null;
  $('#cnvs').mousedown(function(evt) {
      evt.preventDefault();
      evt.stopPropagation();
      $('#cnvs').bind('mousemove', function(e) {
          var curr = {x:e.pageX-pos.left, y:e.pageY-pos.top};
          if (!prev) {
              prev = curr;
              return;
          }
          if (Math.sqrt(Math.pow(prev.x - curr.x, 2) +
                        Math.pow(prev.y - curr.y, 2)) > 8) {
               var p = {x1:prev.x, y1:prev.y, x2:curr.x, y2:curr.y}
               lines.push(p);
               draw(p);
               send(JSON.stringify(p));
               prev = curr;
          }
      });
  });
  $('html').mouseup(function() {
      prev = null;
      $('#cnvs').unbind('mousemove');
  });
}
else {
  document.write("Sorry - this demo requires a browser with canvas tag support.");
}

// Stomp.js boilerplate
Stomp.WebSocketClass = SockJS;

var client = Stomp.client('http://' + window.location.hostname + ':15674/stomp');

client.debug = function() {
  if (window.console && console.log && console.log.apply) {
    console.log.apply(console, arguments);
  }
};

send = function(data) {
  client.send('/topic/bunny', {}, data);
};

var on_connect = function(x) {
  id = client.subscribe('/topic/bunny', function(d) {
    var p = JSON.parse(d.body);
    lines.push(p);
    draw(p, true);
  });
};
var on_error = function() {
  console.log('error');
};
client.connect('guest', 'guest', on_connect, on_error, '/');

</script>
</body>
</html>