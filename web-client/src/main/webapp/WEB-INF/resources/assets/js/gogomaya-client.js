$.get(
    "somepage.php",
    {paramOne : 1, paramX : 'abc'},
    function(data) {
       alert('page content: ' + data);
    }
);