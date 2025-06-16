drop database if exists Work;

create database Work;

use Work;

-- Table for Professionals
CREATE TABLE IF NOT EXISTS Professional (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE, -- Assuming format XXX.XXX.XXX-XX
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    gender VARCHAR(50),
    birth_date DATE
);

-- Table for Companies
CREATE TABLE IF NOT EXISTS Company (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE, -- Assuming format XX.XXX.XXX/XXXX-XX
    name VARCHAR(255) NOT NULL,
    description TEXT,
    city VARCHAR(255)
);

-- Table for Admin Users
CREATE TABLE IF NOT EXISTS Admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    -- Add other admin-specific fields if necessary in the future
);

-- Table for Vacancies
CREATE TABLE IF NOT EXISTS Vacancy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    remuneration DECIMAL(10, 2),
    application_deadline DATE NOT NULL,
    city VARCHAR(255) NOT NULL, -- Denormalized for filtering (R4)
    CONSTRAINT fk_vacancy_company FOREIGN KEY (company_id) REFERENCES Company(id) ON DELETE CASCADE
);

-- Table for Applications
CREATE TABLE IF NOT EXISTS Application (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    professional_id BIGINT NOT NULL,
    vacancy_id BIGINT NOT NULL,
    resume_path VARCHAR(255), -- Path to the stored PDF resume
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN', -- e.g., OPEN, NOT_SELECTED, INTERVIEW
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_application_professional FOREIGN KEY (professional_id) REFERENCES Professional(id) ON DELETE CASCADE,
    CONSTRAINT fk_application_vacancy FOREIGN KEY (vacancy_id) REFERENCES Vacancy(id) ON DELETE CASCADE,
    CONSTRAINT uk_professional_vacancy UNIQUE (professional_id, vacancy_id) -- Ensure a professional applies only once per vacancy
);
