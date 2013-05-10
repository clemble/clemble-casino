(function() {
	select = function(selectedCell) {
		$.ajax({
			headers : {
				"Content-Type" : "application/json",
				"Accept" : "application/json",
				"playerId" : localStorage.getItem('playerId'),
				"tableId" : localStorage.getItem('tableId'),
				"sessionId" : localStorage.getItem('sessionId')
			},
			type : "POST",
			url : "http://gogomaya.cloudfoundry.com/spi/active/action",
			data : JSON.stringify({
				type : "select",
				playerId : localStorage.getItem('playerId'),
				cell : selectedCell
			}, null, 2),
			success : function(data, textStatus, jqXHR) {
				alert("Success: " + data);
			},
			error : function(data) {
				alert("Error " + JSON.stringify(data, null, 2));
			}
		});
	}
	bet = function(amount) {
		$.ajax({
			headers : {
				"Content-Type" : "application/json",
				"Accept" : "application/json",
				"playerId" : localStorage.getItem('playerId'),
				"tableId" : localStorage.getItem('tableId'),
				"sessionId" : localStorage.getItem('sessionId')
			},
			type : "POST",
			url : "http://gogomaya.cloudfoundry.com/spi/active/action",
			data : JSON.stringify({
				type : "bet",
				playerId : localStorage.getItem('playerId'),
				bet : amount
			}, null, 2),
			success : function(data, textStatus, jqXHR) {
				alert("Success: " + data);
			},
			error : function(data) {
				alert("Error " + JSON.stringify(data, null, 2));
			}
		});
	}
})
