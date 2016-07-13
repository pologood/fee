import static lib.BDD.*;

def phoneUnicom="18600539639"
CONFIG(
  server: "http://127.0.0.1",
  headers: [host: "test.fee.pay.sogou.com"]
)

GET("/api/phoneinfo?phones=$phoneUnicom")
EXPECT {
  json.code = 0
  json.'data' = NotEmpty
  json.'data' = Grep {
    obj.operator= "CHINA_UNICOM"
  }
}