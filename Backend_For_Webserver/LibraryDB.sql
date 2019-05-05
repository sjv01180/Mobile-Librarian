DROP DATABASE IF EXISTS Library;
CREATE DATABASE IF NOT EXISTS Library;

USE Library;

CREATE table Faculty(
        Fid CHAR(10) PRIMARY KEY NOT NULL,
        Fname VARCHAR(25) NOT NULL,
        Lname VARCHAR(25) NOT NULL);

CREATE table Student(
        Sid CHAR(10) PRIMARY KEY NOT NULL,
        Fname VARCHAR(25) NOT NULL,
        Lname VARCHAR(25) NOT NULL);

CREATE table Reserves(
        OrderID INT PRIMARY AUTO_INCREMENT,
        BookISBN CHAR(13) NOT NULL,
        CO_Date DATE NOT NULL,
        CI_Date DATE DEFAULT NULL,
        Due_Date DATE NOT NULL,
        Fid CHAR(10),
        CONSTRAINT `Reserves_fidfk_1` FOREIGN KEY (`Fid`) REFERENCES `Faculty`(`Fid`),
        CONSTRAINT `Reserves_bookisbnfk_2` FOREIGN KEY (`BookISBN`) REFERENCES `Books`(`BookISBN`));

CREATE table Checks(
        OrderID INT PRIMARY KEY AUTO_INCREMENT,
        BookISBN CHAR(13) NOT NULL,
        CO_Date DATE NOT NULL,
        CI_Date DATE DEFAULT NULL,
        Due_Date DATE NOT NULL,
        Sid CHAR(10),
        CONSTRAINT `Checks_sidfk_1` FOREIGN KEY (`Sid`) REFERENCES `Student`(`Sid`),
        CONSTRAINT `Checks_bookisbnfk_2` FOREIGN KEY (`BookISBN`) REFERENCES `Books`(`BookISBN`));

CREATE table Books(
        BookISBN CHAR(13) PRIMARY KEY NOT NULL,
        Title VARCHAR(30) NOT NULL,
        Author VARCHAR(30) NOT NULL,
        Genre VARCHAR(30) NOT NULL);

CREATE table Userbase(
        Name VARCHAR(30) PRIMARY KEY NOT NULL,
        Passwrd CHAR(30) NOT NULL,
        Role VARCHAR(5) NOT NULL);


