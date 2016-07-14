import static lib.BDD.*;

SQLCONFIG(
        db: 'fee',
        user: 'fee'
)

SQL("delete from orders")
SQL("delete from products")

CONFIG(
        server: "http://127.0.0.1",
        headers: [host: "test.fee.pay.sogou.com"]
)

GET("/api/phoneinfo") {
    r.query = [phones: "18600539639"]
}
EXPECT {
    json.code = 0
    json.'data' = NotEmpty
    json.'data' = Grep {
        obj.operator = "CHINA_UNICOM"
        obj.provinceId = "110000"
    }
}

GET("/api/phoneinfo") {
    r.query = [phones: "15811330571"]
}
EXPECT {
    json.code = 0
    json.'data' = NotEmpty
    json.'data' = Grep {
        obj.operator = "CHINA_MOBILE"
        obj.provinceId = "110000"
    }
}

GET("/api/phoneinfo") {
    r.query = [phones: "18875617560"]
}
EXPECT {
    json.code = 0
    json.'data' = NotEmpty
    json.'data' = Grep {
        obj.operator = "CHINA_MOBILE"
        obj.provinceId = "130000"
    }
}

GET("/api/phoneinfo")
EXPECT {
    json.code = 400
}

GET("/api/phoneinfo") {
    r.query = [phones: "111"]
}
EXPECT {
    json.code = 400
}


GET("/api/phoneProducts") {
    r.query = [feeType : "PHONE",
               operator: "CHINA_MOBILE",
               province: "110000"]
}
def phoneProNum
def phoneProductId
EXPECT {
    json.code = 0
    json.'data.length()' > 0
    json.closure = { json ->
        phoneProNum = json.'data.length()'
        phoneProductId = json.'data[0].productId'
    }
}

def productCount
SQL("select count(*)  as num from products") { row ->
    productCount = row.num
    assert productCount == phoneProNum
}

GET("/api/phoneProducts") {
    r.query = [feeType : "FLOW",
               operator: "CHINA_MOBILE",
               province: "110000"]
}
def flowProNum
def flowProductId
EXPECT {
    json.code = 0
    json.'data.length()' > 0
    json.closure = { json ->
        flowProNum = json.'data.length()'
        flowProductId = json.'data[0].productId'
    }
}

SQL("select count(*)  as num from products") { row ->
    productCount = row.num
    assert productCount == (phoneProNum + flowProNum)
}

println "phoneProNum:$phoneProNum"
println "flowProNum:$flowProNum"
println "productCount:$productCount"
println "phoneProductId:$phoneProductId"
println "flowProductId:$flowProductId"



GET("/api/phoneProducts") {
    r.query = [feeType : "PHONE",
               operator: "CHINA_MOBILE",
               province: "110000"]
}
GET("/api/phoneProducts") {
    r.query = [feeType : "FLOW",
               operator: "CHINA_MOBILE",
               province: "110000"]
}


SQL("select count(*)  as num from products") { row ->
    productCountNew = row.num
    println "productCountNew:$productCountNew"
    assert productCountNew == productCount
}

GET("/api/phoneProducts") {
    r.query = [feeType : "FLOW",
               province: "110000"]
}
EXPECT {
    json.code = 400
}
GET("/api/phoneProducts") {
    r.query = [feeType : "FLOW",
               operator: "CHINA_MOBILE"]
}
EXPECT {
    json.code = 400
}

POST("/api/order") {
    r.body = [feeType    : "PHONE",
              productId  : phoneProductId,
              quantity   : 1,
              payChannel : "ZHIFUBAO",
              payTerminal: "WAP",
              province   : "110000",
              operator   : "CHINA_MOBILE"
    ]
}
EXPECT {
    json.code = 400
}
POST("/api/order") {
    r.body = [feeType    : "PHONE",
              phone      : "15811330571",
              quantity   : 1,
              payChannel : "ZHIFUBAO",
              payTerminal: "WAP",
              province   : "110000",
              operator   : "CHINA_MOBILE"
    ]
}
EXPECT {
    json.code = 400
}

POST("/api/order") {
    r.body = [feeType    : "PHONE",
              productId  : phoneProductId,
              phone      : "15811330571",
              quantity   : 1,
              payChannel : "ZHIFUBAO",
              payTerminal: "WAP",
              province   : "110000",
              operator   : "CHINA_UNICOM"
    ]
}
EXPECT {
    json.code = 500
}

POST("/api/order") {
    r.body = [feeType    : "PHONE",
              productId  : phoneProductId,
              phone      : "15811330571",
              quantity   : 1,
              payChannel : "ZHIFUBAO",
              payTerminal: "WAP",
              province   : "130000",
              operator   : "CHINA_MOBILE"
    ]
}
EXPECT {
    json.code = 500
}

POST("/api/order") {
    r.body = [feeType    : "PHONE",
              productId  : phoneProductId,
              phone      : "15811330571",
              quantity   : 1,
              payTerminal: "WAP",
              province   : "110000",
              operator   : "CHINA_MOBILE"
    ]
}
EXPECT {
    json.code = 400
}

POST("/api/order") {
    r.body = [feeType   : "PHONE",
              productId : phoneProductId,
              phone     : "15811330571",
              quantity  : 1,
              payChannel: "ZHIFUBAO",
              province  : "110000",
              operator  : "CHINA_MOBILE"
    ]
}
EXPECT {
    json.code = 400
}

POST("/api/order") {
    r.body = [feeType    : "PHONE",
              productId  : phoneProductId,
              quantity   : 2,
              phone      : "15811330571",
              payChannel : "ZHIFUBAO",
              payTerminal: "WAP",
              province   : "110000",
              operator   : "CHINA_MOBILE"
    ]
}
EXPECT {
    json.code = 400
}

def orderCountOld
SQL("select count(*) as num from orders") { row ->
    orderCountOld = row.num
}


POST("/api/order") {
    r.body = [feeType    : "PHONE",
              productId  : phoneProductId,
              phone      : "15811330571",
              quantity   : 1,
              payChannel : "ZHIFUBAO",
              payTerminal: "WAP",
              province   : "110000",
              operator   : "CHINA_MOBILE"
    ]
}
def phoneOrderId
EXPECT {
    json.code = 0
    json.closure = { json ->
        phoneOrderId = json.'data.order.orderId'
        status = json.'data.order.status'
        assert status.equals("TOPAY")
        payInfo = json.'data.paymentInfo'
        payReturnType = json.'data.payReturnType'
        println "payinfo:$payInfo"
        println "payReturnType:$payReturnType"
    }
}

def orderCount
SQL("select count(*) as num from orders") { row ->
    orderCount = row.num
    assert orderCount == (orderCountOld + 1)
}
println "orderCount:$orderCountOld"
println "orderCount:$orderCount"



orderCountOld = orderCount

POST("/api/order") {
    r.body = [feeType    : "FLOW",
              productId  : flowProductId,
              phone      : "15811330571",
              quantity   : 1,
              payChannel : "ZHIFUBAO",
              payTerminal: "WAP",
              province   : "110000",
              operator   : "CHINA_MOBILE"
    ]
}

def flowOrderId
EXPECT {
    json.code = 0
    json.closure = { json ->
        flowOrderId = json.'data.order.orderId'
        status = json.'data.order.status'
        assert status.equals("TOPAY")
        payInfo = json.'data.paymentInfo'
        payReturnType = json.'data.payReturnType'
        println "payinfo:$payInfo"
        println "payReturnType:$payReturnType"
    }
}

SQL("select count(*) as num from orders") { row ->
    orderCount = row.num
    assert orderCount == (orderCountOld + 1)
}

POST("/api/order") {
    r.body = [feeType    : "FLOW",
              productId  : flowProductId,
              phone      : "15811330571",
              payChannel : "ZHIFUBAO",
              payTerminal: "WAP",
              province   : "110000",
              operator   : "CHINA_MOBILE"
    ]
}

EXPECT {
    json.code = 0
    json.closure = { json ->
        flowOrderId = json.'data.order.orderId'
        status = json.'data.order.status'
        assert status.equals("TOPAY")
        payInfo = json.'data.paymentInfo'
        payReturnType = json.'data.payReturnType'
        println "payinfo:$payInfo"
        println "payReturnType:$payReturnType"
    }
}


for (i = 0; i < 3; i++) {
    feeType = "PHONE"
    POST("/api/order") {
        r.body = [feeType    : feeType,
                  productId  : phoneProductId,
                  phone      : "15811330571",
                  payChannel : "ZHIFUBAO",
                  payTerminal: "WAP",
                  province   : "110000",
                  operator   : "CHINA_MOBILE"
        ]
    }
}

for (j = 0; j < 3; j++) {
    feeType = "FLOW"
    POST("/api/order") {
        r.body = [feeType    : feeType,
                  productId  : flowProductId,
                  phone      : "15811330571",
                  payChannel : "ZHIFUBAO",
                  payTerminal: "WAP",
                  province   : "110000",
                  operator   : "CHINA_MOBILE"
        ]
    }
}

GET("/api/orders/list") {
    r.query = [queryType: "BY_PHONE", phone: "15811330571"]
}

def orderIdLastFir
def orderIdLastSec
def orderIdLastThr
def statusLastFir
def statusLastSec
def statusLastThr

EXPECT {
    json.code = 0
    json.closure = { json ->
        orderIdLastFir = json.'data[0].orderId'
        orderIdLastSec = json.'data[1].orderId'
        orderIdLastThr = json.'data[2].orderId'
        statusLastFir = json.'data[0].status'
        statusLastSec = json.'data[1].status'
        statusLastThr = json.'data[2].status'
        println "$orderIdLastFir:$statusLastFir"
        println "$orderIdLastSec:$statusLastSec"
        println "$orderIdLastThr:$statusLastThr"
    }
}

def border1_max
def border1_min
def border2_max
def border2_min

GET("/api/orders/pages") {
    r.query = [queryType: "BY_PHONE", phone: "15811330571"]
}
EXPECT {
    json.code = 0
    json.closure = { json ->
        border1_max = json.'data[0].max'
        border1_min = json.'data[0].min'
        border2_max = json.'data[1].max'
        border2_min = json.'data[1].min'
    }
}
def border3_max
GET("/api/orders/pages/backward") {
    r.query = [queryType: "BY_PHONE", phone: "15811330571", orderId: border2_min]
}

EXPECT {
    json.code = 0
    json.closure = { json ->
        border3_max = json.'data[0].max'
    }
}

GET("/api/orders/list") {
    r.query = [queryType: "BY_PHONE", phone: "15811330571", orderId: border3_max]
}
def orderId3Fir
def orderId3Sec
def orderId3Thr
def status3Fir
def status3Sec
def status3Thr
EXPECT {
    json.code = 0
    json.closure = { json ->
        orderId3Fir = json.'data[0].orderId'
        orderId3Sec = json.'data[1].orderId'
        orderId3Thr = json.'data[2].orderId'
        status3Fir = json.'data[0].status'
        status3Sec = json.'data[1].status'
        status3Thr = json.'data[2].status'
        println "$orderId3Fir:$status3Fir"
        println "$orderId3Sec:$status3Sec"
        println "$orderId3Thr:$status3Thr"
    }
}

GET("/api/order/$orderId3Sec")
EXPECT {
    json.code = 0
    json.'data' = NotEmpty
    json.closure = { json ->
        status = json.'data.status'
        orderId = json.'data.orderId'
        phone = json.'data.phone'
        type = json.'data.feeType'
        assert status.equals(status3Sec)
        assert orderId.equals(orderId3Sec)
        assert phone.equals("15811330571")
        assert type.equals("FLOW")

    }
}



STAT()