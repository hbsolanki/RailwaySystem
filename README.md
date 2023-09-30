# Railway System

This is Railway System Project using java

Database :


    create database railway;
    USE railway;


    CREATE TABLE user(username varchar(20),password varchar(20));

    CREATE TABLE admin(id varchar(20),password varchar(20));

    CREATE table route(rid int Primary key auto_increment,station varchar(16000));

    CREATE TABLE train(tname varchar(50),tno int,totalseats int ,stops varchar(16000));

    CREATE table ticket(username varchar(50),ticketno int,price int,personlist varchar(16000),phone varchar(20),trainnumber int,trainname varchar(50),source varchar(50),destiniy varchar(50),stime varchar(20),dtime varchar(20));

    drop table route;
    truncate table route;


select * from user;
select * from admin;
select * from ticket;
select * from train;
select * from route;