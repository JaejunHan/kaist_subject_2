const SocketServer = require('websocket').server
const http = require('http')

const server = http.createServer((req, res) => {})

server.listen(8080, ()=>{
    console.log("Listening on port 8080...")
})

wsServer = new SocketServer({httpServer:server})

const connections = []

wsServer.on('request', (req) => {
    const connection = req.accept()
    console.log('new connection')
    connections.push(connection)

    connection.on('message', (mes) => {
        connections.forEach(element => {
            if (element != connection)
                element.sendUTF(mes.utf8Data)
            console.log(mes.utf8Data);
            const obj = JSON.parse(mes.utf8Data);
            console.log(typeof obj);
            console.log(obj.message);
            console.log(obj.image);
        })
    })

    connection.on('close', (resCode, des) => {
        console.log('connection closed')
        connections.splice(connections.indexOf(connection), 1)
    })

})