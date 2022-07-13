const express    = require('express');
const mysql      = require('mysql');
const dbconfig   = require('./config/database.js');
const connection = mysql.createConnection(dbconfig);
//const socketio = require("socket.io");

const app = express();
const SocketServer = require('websocket').server
const http = require('http');

//const server = http.createServer((req, res) => {})

//server.listen(8080, ()=>{
//  console.log("Listening on port 8080...")
//})
const server = http.createServer(app);

server.listen(3000, ()=>{
    console.log("Listening on port 3000...")
})


var storage, path, crypto;
const multer = require('multer');
path = require('path');
crypto = require('crypto');

querystring = require('querystring');



/*
const server = app.listen(app.get('port'), () => {
  console.log('Express server listening on port ' + app.get('port'));
});
const io = socketio(server);
*/
const wsServer = new SocketServer({httpServer:server})

const connections = []
const rooms = new Map();  //채팅방 목록을 담을 객체


let today = new Date();   
let year = today.getFullYear().toString(); // 년도
let month = (today.getMonth() + 1).toString();  // 월
if (month.length == 1){month = "0"+month;}
let date = today.getDate().toString();  // 날짜
if (date.length == 1){date = "0"+date;}
let hours = today.getHours().toString(); // 시
if (hours.length == 1){hours = "0"+hours;}
let minutes = today.getMinutes().toString();  // 분
if (minutes.length == 1){minutes = "0"+minutes;}
let seconds = today.getSeconds().toString();  // 초
if (seconds.length == 1){seconds = "0"+seconds;}

wsServer.on('request', (req) => {
    console.log('new connection')
    console.log(req.resourceURL.query);
    console.log(req.resourceURL.query.user);
    console.log(req.resourceURL.query.room);
    console.log("웹서버접속웹서버접속웹서버접속웹서버접속웹서버접속");
    console.log(req.resourceURL);
    console.log("웹서버접속웹서버접속웹서버접속웹서버접속웹서버접속");
    const user = req.resourceURL.query.user;  //사용자 ID
    const room = req.resourceURL.query.room;  //방번호
    const other_user = req.resourceURL.query.other_user;  //방번호
    const connection_socket = req.accept();
    console.log(room);
    rooms.set(user,{user:user, room:room, con:connection_socket});
    var now_time2 = year+month+date+hours+minutes+seconds;
    var sql4 = "select * from chatting_room where room_name=?";
    var values4 = [room]
    connection.query(sql4, values4, (error, rows) => {
      if (error) {
        console.log(error);
        console.log("MAKING aasdasd안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼ㅍ");
        return;              }
      //에러가 안난 경우
      console.log(rows.length);
      console.log("len");
      if (rows.length== 0){
        var sql3 = "insert into chatting_room (room_name, last_message, reg_date, one_nickname, two_nickname) values (?, ?, ?, ?, ?)";
        console.log(user)
        console.log(other_user)
        var values3 = [room, "", now_time2, user, other_user];
        connection.query(sql3, values3, (error, rows) => {
          console.log("123123123132")
          if (error) {
            console.log(error);
            console.log("MAKING ROOMS안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼ㅍ");
            return;              }
          //에러가 안난 경우
          console.log("MAKING ROOMS성공성공성공성공성공성공성공성공성공성공성공성공성공")
        });
      }
      console.log("MAKING asdasd성공성공성공성공성공성공성공성공성공성공성공성공성공")
    });
    
            
    connection_socket.on('message', function(message) {  //채팅메시지가 도달하면
        //for (let i=0;i<rooms.size;i++){
        for(let target of rooms.values()) {  //방 목록 객체를 반복문을 활용해 발송
          console.log("타겟은 여기에요");
          console.log(target[1]);
          console.log("타겟은 여기에요111");
          console.log(room);
          console.log(target.room);
          console.log(user);
          console.log(target.user);
          console.log("타겟은 여기에요1123232323");
          console.log(month);
          console.log(month.length);
          console.log(typeof month);
          console.log("잘봐주세요!!!!!");
          var now_time = year+month+date+hours+minutes+seconds;
          if(room == target.room && user != target.user){  //같은방에 있는 사람이면 전송
            //var sql = "insert into messages ";
            var sql ="";
            let a;
            console.log(message.utf8Data);
            console.log(message.utf8Data.hasOwnProperty("message"));
            var message_to_send =  JSON.parse(message.utf8Data);
            if (message_to_send.hasOwnProperty("message") == 1){
              sql= "insert into messages (room_name, from_nickname, to_nickname, mes, reg_date) values (?, ?, ?, ?, ?)";
              a = message_to_send.message;
              console.log(a);
              console.log(typeof a);
              console.log("trytrytrytrytrytrytry");
            } else {
              console.log("catchcatchcatchcatchcatchcatch");
              sql= "insert into messages (room_name, from_nickname, to_nickname, img, reg_date) values (?, ?, ?, ?, ?)";
              a = message_to_send.image;
            }
            var values = [room, user, target.user, a, now_time];
            connection.query(sql, values, (error, rows) => {
              if (error) {
                console.log(error);
                console.log("안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼ㅍ");
                return;
              }
              //에러가 안난 경우
              var sql2 = "update chatting_room set last_message = ?, reg_date =? where room_name = ?";
              var values2;
              if (message_to_send.hasOwnProperty("message") == 1){
                values2= [message_to_send.message, now_time, room];
              } else {
                values2= ["picture", now_time, room];
              }
              connection.query(sql2, values2, (error, rows) => {
                if (error) {
                  console.log(error);
                  console.log("안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼안돼ㅍ");
                  return;
                }
                //에러가 안난 경우
                console.log("성공성공성공성공성공성공성공성공성공성공성공성공성공")
              });
              console.log("성공성공성공성공성공성공성공성공성공성공성공성공성공")
            });
            var res = JSON.stringify({param:message_to_send, who:user });  //json형태로 메시지 전달, param은 보낼 메시지 who는 보내는 사람이다.
            console.log(res);
            console.log("ㅅㅏㄹㄹㅕㅈㅜㅓ1");
            console.log(res.param);
            console.log("ㅅㅏㄹㄹㅕㅈㅜㅓ5");
            //console.log(res.param.message);
            target.con.sendUTF(message.utf8Data);
            // target[1].con.sendUTF(res);
            console.log("ㅅㅏㄹㄹㅕㅈㅜㅓ2");
            //console.log(target[1].con);
            
            console.log("ㅅㅏㄹㄹㅕㅈㅜㅓ3");
          }
        }
    });

    connection_socket.on('close', function(reasonCode, description) {   //커넥션이 끊기면
        rooms.delete(user);  //방 목록에서 삭제
    });
    /*
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
    */
})

var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(express.urlencoded({extended : false}));
app.use(express.json());


var form = "<!DOCTYPE HTML><html><body>" +
"<form method='post' action='/upload' enctype='multipart/form-data'>" +
"<input type='file' name='upload'/>" +
"<input type='submit' /></form>" +
"</body></html>";

app.post('/profile', (req, res) => {
  console.log(req.body)
})



// configuration =========================
app.set('port', process.env.PORT || 3000);

app.get('/', function (req, res){
  //res.writeHead(200, {'Content-Type': 'text/html' });
  //res.end(form);
  console.log("시작지점");
  res.send("sdfdsfsdf")
});

app.get('/up', function (req, res){
  res.writeHead(200, {'Content-Type': 'text/html' });
  res.end(form);
  console.log("여기에요");
});




// Include the node file module
var fs = require('fs');

storage = multer.diskStorage({
  destination: './uploads/',
  filename: function(req, file, cb) {
    return crypto.pseudoRandomBytes(16, function(err, raw) {
      if (err) {
        return cb(err);
      }
      return cb(null, "" + (raw.toString('hex')) + (path.extname(file.originalname)));
    });
  }
});

// Post files
app.post(
  "/upload",
  multer({
    storage: storage
  }).single('upload'), function(req, res) {
    console.log(req.file);
    console.log(req.query.user_nickname);
    console.log("ㅇㄹㅗㅓㄴㅗㄹㅓㅏㄴㅇㅠㄹㅏㄴㅠㄹㅓㅏㄴㅇ");
    var sql = "update profile set profile_img = ? where nickname = ?";
    var img_url = "https://7db1-192-249-18-214.jp.ngrok.io/uploads/"+req.file.filename;
    console.log(img_url);
    console.log(typeof img_url);
    console.log(typeof req.file.filename);
    console.log("ㅇ랑루ㅡㅏㅇ르ㅏ으라으라으ㅏㄹ");
    var values = [img_url, req.query.user_nickname];
    connection.query(sql, values, (error, rows) => {
      if (error) throw error;
      console.log("프로필 변경 성공!");
    });
    res.redirect("/uploads/" + req.file.filename);
    console.log(req.file.filename);
    return res.status(200).end();
  });

app.get('/uploads/:upload', function (req, res){
    console.log(req.params);
    file = req.params.upload;
    console.log(req.params.upload);
    console.log("여기에요123");
    var img = fs.readFileSync(__dirname + "/uploads/" + file);
    res.writeHead(200, {'Content-Type': 'image/png' });
    res.end(img, 'binary');
  
  });


app.post('/users', (req, res) => {
  console.log(req.body.id);
  var json_object_send = {
    id : '',
    password : '',
    nickname : '',
    sex : '',
    Api_Token : ''
  };
  var sql = 'SELECT * from users where BINARY id = ? and BINARY password = ?';
  var values = [req.body.id, req.body.password];
  connection.query(sql, values, (error, rows) => {
    if (error) throw error;
    console.log('User info is: ', rows);
    if (rows.length == 0){
      console.log('Json object : ', json_object_send);
      res.send(json_object_send);
      console.log("여기여기")
      return;
    }
    console.log("여기여기ㄴㅇㄹㅇㄹ")
    json_object_send.id = rows[0]["id"];
    json_object_send.password = rows[0]["password"];
    json_object_send.nickname = rows[0]["nickname"];
    json_object_send.sex = rows[0]["sex"];
    json_object_send.Api_Token = rows[0]["Api_Token"];
    console.log('Json object : ', json_object_send);
    res.send(json_object_send);
  });
});

app.post('/id_valid', (req, res) => {
  var json_object_send = {
    id_valid : false
  };
  var sql = 'SELECT * from users where BINARY id = ?';
  var values = [req.body.id];
  connection.query(sql, values, (error, rows) => {
    if (error) throw error;
    console.log('User info is: ', rows);
    if (rows.length != 0){
      json_object_send.id_valid = true;
      console.log('Json object : ', json_object_send);
      res.send(json_object_send);
      return;
    }
    res.send(json_object_send);
  });
});

app.post('/nickname_valid', (req, res) => {
  var json_object_send = {
    nickname_valid : false
  };
  var sql = 'SELECT * from users where BINARY nickname = ?';
  var values = [req.body.nickname];
  connection.query(sql, values, (error, rows) => {
    if (error) throw error;
    console.log('User info is: ', rows);
    if (rows.length != 0){
      json_object_send.nickname_valid = true;
      console.log('Json object : ', json_object_send);
      res.send(json_object_send);
      return;
    }
    res.send(json_object_send);
  });
});

app.post('/sign_up', (req, res) => {
  var sql = 'insert into users (id, password, nickname, sex, Api_Token, birth, create_date) values (?,?,?,?,"ERYON",?, ?)';
  var sql2 = 'insert into profile (nickname) values (?)';
  var values2 = [req.body.nickname];
  console.log(req.body.sex);
  var now_time = year+month+date;
  var values = [req.body.id, req.body.password, req.body.nickname,req.body.sex, req.body.birth, now_time];
  connection.query(sql, values, (error, rows) => {
    if (error) {console.log(error);}
    else {
      console.log(rows);
      connection.query(sql2, values2, (error, rows) => {
        if (error) {console.log(error);}
        else {
          console.log(rows);
        }
      });
    }
  });
});

app.post('/read_post', (req, res) => {
  var sql = 'select * from posting';
  var json_container = {
    jsonArray : ""
  };

  connection.query(sql, (error, rows) => {
    if (error) {console.log(error);}
    else {
      console.log("ㅁㅗㄷㅡㄴㄱㅔㅅㅣㅁㅜㄹㅈㅗㅎㅗㅣ");
      //console.log(rows);
      console.log(rows.length);
      var aJsonArray = new Array();
      for (let i=0;i<rows.length;i++){
        var json_object_send = {
          posting_id : rows[i]["posting_id"],
          nickname : rows[i]["nickname"],
          title : rows[i]["title"],
          sub_title : rows[i]["sub_title"],
          contents : rows[i]["contents"],
          score : rows[i]["score"],
          create_date : rows[i]["create_date"],
          update_date : rows[i]["update_date"],
          image1 : rows[i]["image1"],
          image2 : rows[i]["image2"],
          image3 : rows[i]["image3"],
          image4 : rows[i]["image4"],
          image5 : rows[i]["image5"]
        };
        aJsonArray.push(json_object_send);
      }
      var sJson = JSON.stringify(aJsonArray);
      console.log(sJson);
      console.log("ㄴㅐㄱㅏㅇㅜㅓㄴㅎㅏㄴㅡㄴㄱㅗㅅ?");
      json_container.jsonArray = sJson;
      console.log(json_container);
      res.send(json_container);
    }
  });
});

app.post('/load_comment', (req, res) => {
  var sql = 'select * from comment where posting_title = ? and posting_sub_title = ?';
  var values = [req.body.title, req.body.sub_title];
  var json_container = {
    jsonArray : ""
  };

  connection.query(sql, values, (error, rows) => {
    if (error) {console.log(error);}
    else {
      console.log("ㅁㅗㄷㅡㄴㄱㅔㅅㅣㅁㅜㄹㅈㅗㅎㅗㅣ");
      //console.log(rows);
      console.log(rows.length);
      var aJsonArray = new Array();
      for (let i=0;i<rows.length;i++){
        var json_object_send = {
          comment_nickname : rows[i]["comment_nickname"],
          posting_title : rows[i]["posting_title"],
          posting_sub_title : rows[i]["posting_sub_title"],
          contents : rows[i]["contents"],
          like_cnt : rows[i]["like_cnt"],
          create_date : rows[i]["create_date"],
          update_date : rows[i]["update_date"]
        };
        aJsonArray.push(json_object_send);
      }
      var sJson = JSON.stringify(aJsonArray);
      console.log(sJson);
      console.log("sdfnmkdfndjkfndjfnajnvklcjnjk?");
      json_container.jsonArray = sJson;
      console.log(json_container);
      res.send(json_container);
    }
  });
});



app.post('/read_chatting_room', (req, res) => {
  var sql = 'select * from chatting_room where one_nickname = ? or two_nickname = ? order by reg_date desc';
  var values = [req.body.my_nickname, req.body.my_nickname];
  console.log(req.body.my_nickname);
  var json_container = {
    jsonArray : ""
  };
  var my_nickname = req.body.my_nickname;
  connection.query(sql, values, (error, rows) => {
    if (error) {console.log(error);}
    else {
      console.log("ALL CHATTING ROOM SEARCH");
      //console.log(rows);
      console.log(rows.length);
      var aJsonArray = new Array();
      for (let i=0;i<rows.length;i++){
        var json_object_send;
        if (my_nickname == rows[i]["one_nickname"]){
          json_object_send = {
            last_message : rows[i]["last_message"],
            reg_date : rows[i]["reg_date"],
            nickname : rows[i]["two_nickname"]
          };
        }else if (my_nickname == rows[i]["two_nickname"]){
          json_object_send = {
            last_message : rows[i]["last_message"],
            reg_date : rows[i]["reg_date"],
            nickname : rows[i]["one_nickname"]
          };
        }else{
          console.log("ㅇㅔㄹㅓㅂㅏㄹㅅㅐㅇ ㅇㅣㅅㅏㅇㅎㅐㅇㅛ!!");
          return ;
        }
        aJsonArray.push(json_object_send);
      }
      var sJson = JSON.stringify(aJsonArray);
      console.log(sJson);
      console.log("kmlfgmldfmglfmglmflgmfg?");
      json_container.jsonArray = sJson;
      console.log(json_container);
      res.send(json_container);
    }
  });
});


app.post('/load_chat', (req, res) => {
  console.log("채팅 로드!!!!!!!!!!!!!!!!!!!!!");
  var sql = 'select * from messages where room_name = ? order by reg_date asc';
  var values = [req.body.room];
  var json_container = {
    jsonArray : ""
  };
  var my_nickname = req.body.my_nickname;
  connection.query(sql, values, (error, rows) => {
    if (error) {console.log(error);}
    else {
      console.log("ㅌㅡㄱㅈㅓㅇㅊㅐㅌㅣㅇㅂㅏㅇㅈㅗㅎㅗㅣ");
      //console.log(rows);
      console.log(rows.length);
      var aJsonArray = new Array();
      for (let i=0;i<rows.length;i++){
        console.log(rows[i]["mes"]);
        console.log("ㅇㅓㄴㅜㄹㅓㅇㅜㄹㅓㅇㅜㄹㅓㅜㅇㅓㄹㅜㅇㅓㅜㄹㅓ");
        var json_object_send;
        json_object_send = {
          from_nickname : rows[i]["from_nickname"],
          to_nickname : rows[i]["to_nickname"],
          mes : rows[i]["mes"]
        };
        aJsonArray.push(json_object_send);
      }
      var sJson = JSON.stringify(aJsonArray);
      console.log(sJson);
      console.log("asdsvxcvsdfwerfhghgh?");
      json_container.jsonArray = sJson;
      console.log(json_container);
      res.send(json_container);
    }
  });
});

app.post('/edit_post', (req, res) => {
  var imgCnt = req.body.imgCnt;
  imgCnt *= 1;  // 스트링을 숫자로 바꾸기
  var values;
  var sql;
  var json_object_send = {
    edit_ok : false
  };
  console.log(typeof imgCnt);
  console.log(imgCnt);
  if (imgCnt == 0){
    sql = 'update posting set nickname = ?, title = ?, sub_title = ?, contents = ?, score=?, update_date=? where nickname = ? and title = ? and sub_title=?';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.update_date, req.body.nickname, req.body.title, req.body.sub_title];
    console.log(values);
  } else if (imgCnt == 1){
    sql = 'update posting set nickname = ?, title = ?, sub_title = ?, contents = ?, score=?, update_date=?, image1 = ? where nickname = ? and title = ? and sub_title=?';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.update_date, req.body.image1, req.body.nickname, req.body.title, req.body.sub_title];
  } else if (imgCnt == 2){
    sql = 'update posting set nickname = ?, title = ?, sub_title = ?, contents = ?, score=?, update_date=?, image1 = ?, image2 = ? where nickname = ? and title = ? and sub_title=?';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.update_date, req.body.image1, req.body.image2, req.body.nickname, req.body.title, req.body.sub_title];
  }else if (imgCnt == 3){
    sql = 'update posting set nickname = ?, title = ?, sub_title = ?, contents = ?, score=?, update_date=?, image1 = ?, image2 = ?, image3 = ? where nickname = ? and title = ? and sub_title=?';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.update_date, req.body.image1, req.body.image2, req.body.image3, req.body.nickname, req.body.title, req.body.sub_title];
  }else if (imgCnt == 4){
    sql = 'update posting set nickname = ?, title = ?, sub_title = ?, contents = ?, score=?, update_date=?, image1 = ?, image2 = ?, image3 = ?, image4 = ? where nickname = ? and title = ? and sub_title=?';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.update_date, req.body.image1, req.body.image2, req.body.image3, req.body.image4, req.body.nickname, req.body.title, req.body.sub_title];
  }else if (imgCnt == 5){
    sql = 'update posting set nickname = ?, title = ?, sub_title = ?, contents = ?, score=?, update_date=?, image1 = ?, image2 = ?, image3 = ?, image4 = ?, image5 = ? where nickname = ? and title = ? and sub_title=?';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.update_date, req.body.image1, req.body.image2, req.body.image3, req.body.image4, req.body.image5, req.body.nickname, req.body.title, req.body.sub_title];
  }

  connection.query(sql, values, (error, rows) => {
    if (error) {
      console.log(error);
      res.send(json_object_send);
    }
    //에러가 안난 경우
    json_object_send.edit_ok = true;
    res.send(json_object_send);
  });
});



app.post('/delete_post', (req, res) => {
  var json_object_send = {
    is_delete : false
  };
  var sql = 'delete from posting where nickname = ? and title = ? and sub_title = ?';
  var values = [req.body.nickname, req.body.title, req.body.sub_title];
  connection.query(sql, values, (error, rows) => {
    if (error) {
      console.log(error);
      res.send(json_object_send);
    }
    //에러가 안난 경우
    json_object_send.is_delete = true;
    res.send(json_object_send);
  });
});

app.post('/write_post', (req, res) => {
  var imgCnt = req.body.imgCnt;
  imgCnt *= 1;  // 스트링을 숫자로 바꾸기
  var values;
  var sql;
  var json_object_send = {
    insert_ok : false
  };
  console.log(typeof imgCnt);
  console.log(imgCnt);
  if (imgCnt == 0){
    sql = 'insert into posting (nickname, title, sub_title, contents, score) values (?, ?, ?, ?, ?)';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score];
    console.log(values);
  } else if (imgCnt == 1){
    sql = 'insert into posting (nickname, title, sub_title, contents, score, image1) values (?, ?, ?, ?, ?, ?)';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.image1];
  } else if (imgCnt == 2){
    sql = 'insert into posting (nickname, title, sub_title, contents, score, image1, image2) values (?, ?, ?, ?, ?, ?, ?)';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.image1, req.body.image2];
  }else if (imgCnt == 3){
    sql = 'insert into posting (nickname, title, sub_title, contents, score, image1, image2, image3) values (?, ?, ?, ?, ?, ?, ?, ?)';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.image1, req.body.image2, req.body.image3];
  }else if (imgCnt == 4){
    sql = 'insert into posting (nickname, title, sub_title, contents, score, image1, image2, image3, image4) values (?, ?, ?, ?, ?, ?, ?, ?, ?)';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.image1, req.body.image2, req.body.image3, req.body.image4];
  }else if (imgCnt == 5){
    sql = 'insert into posting (nickname, title, sub_title, contents, score, image1, image2, image3, image4, image5) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)';
    values = [req.body.nickname, req.body.title, req.body.sub_title, req.body.contents, req.body.score, req.body.image1, req.body.image2, req.body.image3, req.body.image4, req.body.image5];
  }
  console.log(sql);
  console.log(values);
  connection.query(sql, values, (error, rows) => {
    if (error) {
      console.log(error);
      console.log("ㅇㅔㄹㄹㅓㅂㅏㄹㅅㅐㄹㅇ");
    }
    console.log(rows.length);
    json_object_send.insert_ok = true;
    res.send(json_object_send);
    
   /*
    console.log(rows);
    if (rows.length == 0){  //insert 실패
      console.log("ㅇㅣㄴㅌㅓㅅㅡㅅㅣㄹㅍㅐ");
      res.send(json_object_send);
    }
    else if (rows.length == 1){  // insert 성공
      console.log(rows);
      console.log("ㅇㅣㄴㅌㅓㅅㅡㅅㅓㅇㄱㅗㅇ");
      json_object_send.insert_ok = true;
      res.send(json_object_send);
    }
    */
  });
});


app.post('/create_comment', (req, res) => {
  var values;
  var sql;
  sql = 'insert into comment (comment_nickname, posting_title, posting_sub_title, contents, like_cnt, create_date, update_date) values (?, ?, ?, ?, ?, ?, ?)';
  values = [req.body.comment_nickname, req.body.posting_title, req.body.posting_sub_title, req.body.contents, req.body.like_cnt, req.body.create_date, req.body.update_date];
  var json_object_send = {
    create_ok : false
  };
  console.log(sql);
  console.log(values);
  connection.query(sql, values, (error, rows) => {
    if (error) {
      console.log(error);
      console.log("ㅇㅔㄹㄹㅓㅂㅏㄹㅅㅐㄹㅇ");
    }
    json_object_send.create_ok = true;
    res.send(json_object_send);
  });
});

app.post('/search_post', (req, res) => {
  var values;
  var sql;
  sql = 'select title, sub_title from posting where title Like ?';
  values = ['%'+req.body.title+'%'];
  var json_container = {
    jsonArray : ""
  };
  connection.query(sql, values, (error, rows) => {
    if (error) {console.log(error);}
    else {
      console.log("ㅌㅡㄱㅈㅓㅇㅊㅐㅌㅣㅇㅂㅏㅇㅈㅗㅎㅗㅣ");
      //console.log(rows);
      console.log(rows.length);
      var aJsonArray = new Array();
      for (let i=0;i<rows.length;i++){
        console.log(rows[i]["mes"]);
        console.log("ㅇㅓㄴㅜㄹㅓㅇㅜㄹㅓㅇㅜㄹㅓㅜㅇㅓㄹㅜㅇㅓㅜㄹㅓ");
       
        var json_object_send = {
          posting_id : rows[i]["posting_id"],
          nickname : rows[i]["nickname"],
          title : rows[i]["title"],
          sub_title : rows[i]["sub_title"],
          contents : rows[i]["contents"],
          score : rows[i]["score"],
          create_date : rows[i]["create_date"],
          update_date : rows[i]["update_date"],
          image1 : rows[i]["image1"],
          image2 : rows[i]["image2"],
          image3 : rows[i]["image3"],
          image4 : rows[i]["image4"],
          image5 : rows[i]["image5"]
        };
        
        aJsonArray.push(json_object_send);
      }
      var sJson = JSON.stringify(aJsonArray);
      console.log(sJson);
      json_container.jsonArray = sJson;
      console.log(json_container);
      res.send(json_container);
    }
  });
});


app.post('/get_profile', (req, res) => {
  var json_object_send = {
    nickname : "",
    profile_img : "",
    phone_number : "",
    email : "",
    stack : "",
    one_talk  : "",
    carrerCnt : "",
    career1 : "",
    career2 : "",
    career3 : "",
    career4 : "",
    career5 : ""
  };
  var values = [req.body.nickname];
  var sql = "select * from profile where nickname = ?";
  console.log(sql);
  console.log(values);
  console.log("프로필 불러오기!@!@!@!@!@!@!@!@!@!");
  connection.query(sql, values, (error, rows) => {
    if (error) {
      console.log(error);
      console.log("프로필 불러오기 에러 발생!!프로필 불러오기 에러 발생!!프로필 불러오기 에러 발생!!");
    }
    console.log("sdfjkdnfjdnfjndfjndjn");
    json_object_send.nickname = rows[0]["nickname"];
    json_object_send.profile_img = rows[0]["profile_img"];
    json_object_send.phone_number = rows[0]["phone_number"];
    json_object_send.email = rows[0]["email"];
    json_object_send.stack = rows[0]["stack"];
    json_object_send.one_talk = rows[0]["one_talk"];
    json_object_send.carrerCnt = rows[0]["carrerCnt"];
    json_object_send.career1 = rows[0]["career1"];
    json_object_send.career2 = rows[0]["career2"];
    json_object_send.career3 = rows[0]["career3"];
    json_object_send.career4 = rows[0]["career4"];
    json_object_send.career5 = rows[0]["career5"];
    res.send(json_object_send);
  });
});

