DROP DATABASE IF EXISTS Library;
CREATE DATABASE IF NOT EXISTS Library;

USE Library;

CREATE table Faculty(
        id CHAR(10) PRIMARY KEY NOT NULL,
        Fname VARCHAR(25) NOT NULL,
        Lname VARCHAR(25) NOT NULL);

CREATE table Student(
        id CHAR(10) PRIMARY KEY NOT NULL,
        Fname VARCHAR(25) NOT NULL,
        Lname VARCHAR(25) NOT NULL);

CREATE table Reserved(
        BookISBN CHAR(13) PRIMARY KEY NOT NULL,
        CO_Date Date NOT NULL DEFAULT CURRENT_DATE,
        CI_Date Date,
        Due_Date Date NOT NULL,
        Fid CHAR(10));

CREATE table Checks(
        BookISBN CHAR(13) PRIMARY KEY NOT NULL,
        CO_Date Date NOT NULL DEFAULT CURRENT_DATE,
        CI_Date Date,
        Due_Date Date NOT NULL,
        Sid CHAR(10));
CREATE table Books(
        BookISBN CHAR(13) PRIMARY KEY NOT NULL,
        Title VARCHAR(30) NOT NULL,
        Author VARCHAR(30) NOT NULL,
        Genre VARCHAR(30) NOT NULL);

CREATE table Userbase(
        Name VARCHAR(30) PRIMARY KEY NOT NULL,
        Passwrd VARCHAR(30) NOT NULL,
        IsCirc BOOLEAN, IsStock BOOLEAN);
