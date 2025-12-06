# 🚂 Train Ticket Booking System

Full-stack Java console application for railway ticket management with MySQL backend.

## Features
- View available trains with seat availability
- Book tickets (auto-decrements seats)
- Cancel tickets (auto-increments seats)
- View all bookings with train details
- Foreign key constraints for data integrity

## Tech Stack
- **Java** (JDBC, PreparedStatements) [web:12]
- **MySQL** (traindb database)
- **Collections** (Scanner for input)

## Database Setup
1. Run `database_setup.sql` in MySQL Workbench
CREATE DATABASE traindb;
-- Tables: trains, tickets with FK
-- Sample data: 3 trains (Chennai Express, Mumbai Express, Delhi Express)
## Sample Output
--- Available Trains ---
Train No: 101 | Name: Chennai Express | From: Chennai | To: Bangalore | Seats Left: 5

text
## How to Run
Start MySQL server (root/2003)

Execute database_setup.sql

Compile: javac train.java

Run: java train
## Menu
View Trains 2. Book Ticket

Cancel Ticket 4. View All Bookings

Exit
## Database Schema
trains: trainNo(PK), trainName, source, destination, seats
tickets: ticketId(AI,PK), passengerName, trainNo(FK)undefined
