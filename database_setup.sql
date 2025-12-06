CREATE DATABASE traindb;

USE traindb;

CREATE TABLE trains (
    trainNo INT PRIMARY KEY,
    trainName VARCHAR(100),
    source VARCHAR(50),
    destination VARCHAR(50),
    seats INT
);

CREATE TABLE tickets (
    ticketId INT AUTO_INCREMENT PRIMARY KEY,
    passengerName VARCHAR(100),
    trainNo INT,
    FOREIGN KEY (trainNo) REFERENCES trains(trainNo)
);

INSERT INTO trains VALUES (101, 'Chennai Express', 'Chennai', 'Bangalore', 5);
INSERT INTO trains VALUES (202, 'Mumbai Express', 'Mumbai', 'Pune', 3);
INSERT INTO trains VALUES (303, 'Delhi Express', 'Delhi', 'Agra', 4);

select * from trains;

