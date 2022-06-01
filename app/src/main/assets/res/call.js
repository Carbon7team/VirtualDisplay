var conn;
var peer;

function call(ip, id){
    window.remoteAudio = document.getElementById('remote-audio')
    window.localAudio = document.getElementById('local-audio')


    console.log("CALL")
    peer = new Peer(id, {
        host: ip,
        port: 9000,
        path: '/myapp',
        //debug: 3
    });
    peer.on('open', function() {//Connection with the peerjs server established
        //peerConn=peer
        console.log("PEER ON OPEN")
    })

    peer.on('connection', function(dataConn) { //Start of the connection with another peer
        console.log("PEER ON CONNECTION")
        conn=dataConn
        conn.on('open', function() { //Connection with the other peer established
            console.log("CONN ON OPEN")

            App.connectionEstablished()


        });
        conn.on('close', function() { //The connection with the other peer is closed
                           console.log("CONN ON CLOSE")
                           App.connectionClosed()
                           document.getElementById("peer").innerHTML = "Nessuna connessione con il peer (Closed)";
                       });
    });
    peer.on("call",function(call){
        console.log("PEER ON CALL")
        call.answer(window.localStream)
        call.on("stream", function(stream){
            console.log("CALL ON STREAM")
            window.remoteAudio.srcObject = stream;
            window.remoteAudio.autoplay = true;
            window.peerStream = stream;
        });
    });

}

function getLocalStream() {
    navigator.mediaDevices.getUserMedia({video: false, audio: true}).then( stream => {
        window.localStream = stream;
        window.localAudio.srcObject = stream;
        window.localAudio.autoplay = true;

    }).catch( err => {
        console.log("u got an error:" + err)
    });
}

function sendData(msg){
    conn.send(msg)
    document.getElementById("invioDati").innerHTML = "Dati inviati: String("+msg.length+")"
}

function closeP2PConnection(){
    dataConn.close()
}
function closePeer(){
     peerConn.destroy()
 }