CREATE DATABASE ThdlUsers;

USE ThdlUsers;

CREATE TABLE IF NOT EXISTS ThdlUsers
(
	id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	firstname TEXT,
	lastname TEXT,
	middlename TEXT,
	email TEXT,
	username TEXT,
	creditAttributionTag TEXT,
	password TEXT,
	passwordHint TEXT
);

CREATE TABLE IF NOT EXISTS Roles
(
	role TEXT NOT NULL,
	description TEXT,
	PRIMARY KEY ( role(10) )
);

CREATE TABLE IF NOT EXISTS Applications
(
	id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	application TEXT,
	description TEXT
);

CREATE TABLE IF NOT EXISTS UserRolesForApplication
(
	userId MEDIUMINT NOT NULL,
	applicationId MEDIUMINT NOT NULL,
	roles TEXT,
	PRIMARY KEY( userId, applicationId )
)COMMENT="merge colon-delimited role-list with user/app";

