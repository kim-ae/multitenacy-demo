create database app;
create database cliente1;
create database cliente2;

use cliente1;

CREATE TABLE info_user (
	id int(11) auto_increment PRIMARY KEY,
	email varchar(255) UNIQUE KEY,
	sensitivy_information varchar(255)
);

INSERT INTO info_user(email, sensitivy_information) VALUES ("email-cliente1@email.com","Some sensitivy information for cliente 1.");

use cliente2;

CREATE TABLE info_user (
	id int(11) auto_increment PRIMARY KEY,
	email varchar(255) UNIQUE KEY,
	sensitivy_information varchar(255)
);

INSERT INTO info_user(email, sensitivy_information) VALUES ("email-cliente2@email.com","Some sensitivy information for cliente 2.");