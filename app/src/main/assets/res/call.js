const urlParams = new URLSearchParams(window.location.search);
const idVD = urlParams.get('idVD');
const ip = urlParams.get('ip');

var dataConn;
var peerConn;
function onload() {
    const peer = new Peer(idVD, {
        host: ip,
        port: 9000,
        path: '/myapp',
        debug: 3
    });

    peer.on('open', function() {//Connection with the peerjs server established
        document.getElementById("wrtc-id").innerHTML = peer.id;
        peerConn=peer
    });

    peer.on('connection', function(dataConnection) { //Start of the connection with another peer
        document.getElementById("peer").innerHTML = "Connettendo con il peer";

        dataConnection.on('open', async function() { //Connection with the other peer established
            document.getElementById("peer").innerHTML = "Connesso con il peer";
            dataConn=dataConnection
            App.connectionEstablished()

            dataConnection.on('close', function() { //The connection with the other peer is closed
               App.connectionClosed()
               document.getElementById("peer").innerHTML = "Nessuna connessione con il peer (Closed)";
            });
        });
    });



}
function send(msg){
    dataConn.send(msg)
    document.getElementById("invioDati").innerHTML = "Dati inviati: String("+msg.length+")"
}

function closeP2PConnection(){
    dataConn.close()
}
function closePeer(){
     peerConn.destroy()
 }