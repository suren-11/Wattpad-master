DROP DATABASE IF EXISTS Wattpad;
CREATE DATABASE IF NOT EXISTS Wattpad;
USE Wattpad;
    SHOW DATABSES;
#================================
CREATE TABLE IF NOT EXISTS Customer(
    id VARCHAR (45),
    name VARCHAR (45),
    address TEXT,
    salary DOUBLE,
    CONSTRAINT PRIMARY KEY(id)
);
#===============================
CREATE TABLE IF NOT EXISTS Product(
    code VARCHAR (45),
    description VARCHAR (45),
    unitPrice DOUBLE,
    qtyOnHand INT,
    CONSTRAINT PRIMARY KEY(code)
    );
#======================================
CREATE TABLE IF NOT EXISTS `Order`(
    orderId VARCHAR(45),
    date VARCHAR(250),
    totalCost DOUBLE,
    customer VARCHAR(45),
    CONSTRAINT PRIMARY KEY (orderId),
    CONSTRAINT FOREIGN KEY (customer) REFERENCES customer(id) ON DELETE CASCADE ON UPDATE CASCADE
    );
#=====================================================================
CREATE TABLE IF NOT EXISTS `Order Details`(
    itemCode VARCHAR(45),
    orderId VARCHAR(45),
    unitPrice DOUBLE,
    qty INT,
    CONSTRAINT PRIMARY KEY (itemCode,orderId),
    CONSTRAINT FOREIGN KEY (itemCode) REFERENCES Product(code) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT  FOREIGN KEY (orderId) REFERENCES `Order`(orderId) ON DELETE CASCADE ON UPDATE CASCADE
    );