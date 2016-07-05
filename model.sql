-- CREATE DATABASE IF NOT EXISTS fee DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
-- USE fee;

CREATE TABLE IF NOT EXISTS products (
  productId     BIGINT    AUTO_INCREMENT PRIMARY KEY,
  productName   VARCHAR(128),
  outerId       VARCHAR(128),
  providerId    INT,
  feeType       TINYINT,
  standardPrice BIGINT,
  realPrice     BIGINT,
  denominationprice BIGINT,
  status        TINYINT,
  createTime    TIMESTAMP NOT NULL DEFAULT 0,
  updateTime    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE (outerId)
);


CREATE TABLE IF NOT EXISTS orders (
  orderId     BIGINT     AUTO_INCREMENT PRIMARY KEY,
  feeType     TINYINT,
  userId      VARCHAR(128),
  phone       VARCHAR(128),
  specifiedNo VARCHAR(128),
  outerId     VARCHAR(128),
  productId   BIGINT,
  totalAmount      BIGINT,
  curPrice    BIGINT,
  quantity    INT,
  status      TINYINT,
  payChanel   TINYINT,
  payTerminal TINYINT,
  createTime    TIMESTAMP NOT NULL DEFAULT 0,
  updateTime    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX (userId),
  INDEX (phone),
  INDEX (specifiedNo)
);

CREATE TABLE IF NOT EXISTS providers (
  providerId   INT                AUTO_INCREMENT PRIMARY KEY,
  providerName VARCHAR(128),
  payChannel   VARCHAR(128),
  providerDesc         VARCHAR(128),
  status       TINYINT,
  createTime    TIMESTAMP NOT NULL DEFAULT 0,
  updateTime    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- GRANT ALL ON fee.*  TO 'fee'@'10.%.%.%' IDENTIFIED BY 'c8kFuH4iR9y5PaMbJ5aU';
-- FLUSH PRIVILEGES;
