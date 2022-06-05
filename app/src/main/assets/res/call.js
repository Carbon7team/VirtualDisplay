var conn;
var peer;
var call
var myStream

function call(ip, id){
    //window.remoteAudio = document.getElementById('remote-audio')
    //window.localAudio = document.getElementById('local-audio')


    //getLocalStream()
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
        });
    });
    peer.on("call",function(c){
        call=c
        console.log("PEER ON CALL")
        App.callStarted()
        navigator.mediaDevices
              .getUserMedia({ video:false, audio: true })
              .then((stream) => {
                myStream=stream
                // play the local preview
                //document.getElementById("local-audio").srcObject = stream;
                //document.getElementById("local-audio");
                call.answer(stream);

                //document.getElementById("menu").style.display = "none";
                //document.getElementById("live").style.display = "block";
                call.on("stream", (remoteStream) => {
                  document.getElementById("remote-audio").srcObject = remoteStream;
                  document.getElementById("remote-audio").play();
                });
              })
              .catch((err) => {
                console.log("Failed to get local stream:", err);
              });

        call.on("close", function(){
            console.log("CALL ON CLOSE")
            App.callEnded()
        })
    });

}

function getLocalStream() {
    navigator.mediaDevices.getUserMedia({video: false, audio: true}).then( stream => {
        window.localStream = stream;
        //window.localAudio.srcObject = stream;
        //window.localAudio.autoplay = true;

    }).catch( err => {
        console.log("u got an error:" + err)
    });
}
function endCall(){
    call.close()
    conn.close()
    peer.destroy()
}
function mute(){
    console.log("MUTE")
   myStream.getAudioTracks()
                 .forEach((track) => (track.enabled = false));
}
function unmute(){
    console.log("UNMUTE")
   myStream.getAudioTracks()
                 .forEach((track) => (track.enabled = true));
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

