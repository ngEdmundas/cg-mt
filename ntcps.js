var net = require('net');

var server = net.createServer(function (socket) {
  socket.on('data', function (data) {
    var str = data.toString('utf8');
    //console.log('data socket=', socket);
    console.log('data=', str, 'bytesRead=', socket.bytesRead);
    if (socket.bytesRead === 30) {
      setTimeout(function () {
        socket.write('abc12xyz98');
      }, 300);
    }
    if (socket.bytesRead === 44) {
      setTimeout(function () {
        socket.write('ok');
      }, 300);
    }
  });
  socket.on('close', function (data) {
    console.log('close', data);
  });
  socket.on('error', function (data) {
    console.log('error', data);
  });
  //socket.write('ok');
});

server.listen(9999, '127.0.0.1');
