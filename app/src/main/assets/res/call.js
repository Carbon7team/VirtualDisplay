var conn
var peer
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
            call.close()
            conn.close()
            peer.destroy()

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

function endCall(){
    if(conn===undefined){
        App.connectionClosed()
        peer.destroy()
    }else{
        call.close()
        conn.close()
    }
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
}


