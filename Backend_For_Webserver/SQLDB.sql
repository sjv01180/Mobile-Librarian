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

CREATE table Reserved(
        BookISBN CHAR(13) PRIMARY KEY NOT NULL,
        CO_Date DATE NOT NULL,
        CI_Date DATE DEFAULT NULL,
        Due_Date DATE NOT NULL,
        Fid CHAR(10),
		CONSTRAINT `Checks_fidfk_1` FOREIGN KEY (`Fid`) REFERENCES `Faculty`(`Fid`));

CREATE table Checks(
        BookISBN CHAR(13) PRIMARY KEY NOT NULL,
        CO_Date DATE NOT NULL,
        CI_Date DATE DEFAULT NULL,
        Due_Date DATE NOT NULL,
        Sid CHAR(10),
		CONSTRAINT `Checks_sidfk_1` FOREIGN KEY (`Sid`) REFERENCES `Student`(`Sid`));

CREATE table Books(
        BookISBN CHAR(13) PRIMARY KEY NOT NULL,
        Title VARCHAR(30) NOT NULL,
        Author VARCHAR(30) NOT NULL,
        Genre VARCHAR(30) NOT NULL);

CREATE table Userbase(
        Name VARCHAR(30) PRIMARY KEY NOT NULL,
        Passwrd VARCHAR(30) NOT NULL,
        IsCirc BOOLEAN, IsStock BOOLEAN);
