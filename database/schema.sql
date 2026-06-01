DROP DATABASE IF EXISTS PoisePMS;
CREATE DATABASE PoisePMS;
USE PoisePMS;

CREATE TABLE customers (
  customer_id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  telephone VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL,
  physical_address VARCHAR(255) NOT NULL,
  PRIMARY KEY (customer_id)
);

CREATE TABLE architects (
  architect_id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  telephone VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL,
  physical_address VARCHAR(255) NOT NULL,
  PRIMARY KEY (architect_id)
);

CREATE TABLE contractors (
  contractor_id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  telephone VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL,
  physical_address VARCHAR(255) NOT NULL,
  PRIMARY KEY (contractor_id)
);

CREATE TABLE structural_engineers (
  structural_engineer_id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  telephone VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL,
  physical_address VARCHAR(255) NOT NULL,
  PRIMARY KEY (structural_engineer_id)
);

CREATE TABLE project_managers (
  project_manager_id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  telephone VARCHAR(20) NOT NULL,
  email VARCHAR(100) NOT NULL,
  physical_address VARCHAR(255) NOT NULL,
  PRIMARY KEY (project_manager_id)
);

CREATE TABLE projects (
  project_number INT NOT NULL AUTO_INCREMENT,
  project_name VARCHAR(100) NOT NULL,
  building_type VARCHAR(50) NOT NULL,
  physical_address VARCHAR(255) NOT NULL,
  erf_number VARCHAR(50) NOT NULL,
  total_fee DECIMAL(10, 2) NOT NULL,
  amount_paid DECIMAL(10, 2) NOT NULL,
  deadline DATE NOT NULL,
  is_finalised BOOLEAN NOT NULL DEFAULT FALSE,
  completion_date DATE DEFAULT NULL,
  customer_id INT NOT NULL,
  architect_id INT NOT NULL,
  contractor_id INT NOT NULL,
  structural_engineer_id INT NOT NULL,
  project_manager_id INT NOT NULL,
  PRIMARY KEY (project_number),
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
  FOREIGN KEY (architect_id) REFERENCES architects(architect_id),
  FOREIGN KEY (contractor_id) REFERENCES contractors(contractor_id),
  FOREIGN KEY (structural_engineer_id) REFERENCES structural_engineers(structural_engineer_id),
  FOREIGN KEY (project_manager_id) REFERENCES project_managers(project_manager_id)
);