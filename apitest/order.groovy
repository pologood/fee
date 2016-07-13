import static lib.BDD.*;

SQLCONFIG(
        db: 'fee',
        user: 'fee'
)


CONFIG(
  server: "http://127.0.0.1",
  headers: [host: "test.fee.pay.sogou.com"]
)

GET("/api/phoneinfo"){
  r.query = [phones: "18600539639"]
}
EXPECT {
  json.code = 0
  json.'data' = NotEmpty
  json.'data' = Grep {
    obj.operator= "CHINA_UNICOM"
    obj.provinceId="110000"
  }
}

GET("/api/phoneinfo"){
  r.query = [phones: "15811330571"]
}
EXPECT {
  json.code = 0
  json.'data' = NotEmpty
  json.'data' = Grep {
    obj.operator= "CHINA_MOBILE"
    obj.provinceId="110000"
  }
}

GET("/api/phoneinfo"){
  r.query = [phones: "18875617560"]
}
EXPECT {
  json.code = 0
  json.'data' = NotEmpty
  json.'data' = Grep {
    obj.operator= "CHINA_MOBILE"
    obj.provinceId="130000"
  }
}

GET("/api/phoneinfo")
EXPECT {
  json.code = 400
}

GET("/api/phoneinfo"){
  r.query =[phones: "111"]
}
EXPECT {
  json.code = 400
}



STAT()