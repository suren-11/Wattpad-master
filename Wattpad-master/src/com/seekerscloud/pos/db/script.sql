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