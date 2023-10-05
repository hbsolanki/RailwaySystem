# Railway System

This is Railway System Project using java

Database :


    create database railway;
    USE railway;




    CREATE TABLE user(username varchar(20),password varchar(20));

    CREATE TABLE admin(id varchar(20),password varchar(20));

    CREATE TABLE allroute(rid INT);
    CREATE TABLE alltrain(tno INT);

    CREATE table ticket(username varchar(50),ticketno int,price int,personlist varchar(16000),phone varchar(20),trainnumber int,trainname varchar(50),source varchar(50),destiniy varchar(50),stime varchar(20),dtime varchar(20),ttype varchar(10));



    -- show all details from table
    select * from user;
    select * from admin;
    select * from allroute;
    select * from r2;
    update r2 set src='vg' where km=232;
    select * from alltrain;
    select * from t9216;
    select * from ticket;


    -- delete table
    drop table admin;
    drop table ticket;
    drop table r1;

    -- delete all data from table
    truncate table admin;
    truncate table ticket;
    truncate table allroute;
# RailwaySystem
