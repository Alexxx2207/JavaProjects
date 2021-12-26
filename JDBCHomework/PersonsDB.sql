CREATE DATABASE IF NOT EXISTS Prsn CHARACTER SET 'utf8mb4';

USE Prsn;

CREATE TABLE IF NOT EXISTS Person(
	id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(12) NOT NULL,
    mid_name VARCHAR(12) DEFAULT '',
    last_name VARCHAR(12) NOT NULL,
    created_at DATETIME DEFAULT current_timestamp,
    CONSTRAINT uniq_names UNIQUE(first_name, mid_name, last_name)
);

INSERT INTO Person(first_name, last_name) VALUES ('Ivan', 'Stojanov'), ('Stojan', 'Ivanov');
SELECT * FROM Person;

CREATE TABLE IF NOT EXISTS Phone(
	id INT PRIMARY KEY AUTO_INCREMENT,
    tel VARCHAR(12) NOT NULL UNIQUE,
    id_person INT NOT NULL,
    Constraint person FOREIGN KEY (id_person)
    REFERENCES Person(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Phone(tel, id_person) VALUES ('11', 1), ('12', 1);
SELECT * FROM Phone;


CREATE VIEW ListPhones AS 
SELECT P.id AS person_id,  Trim(Concat(P.last_name, ', ', P.first_name, ' ', P.mid_name)) AS names, T.tel
	FROM Person P, Phone T
WHERE T.id_person = P.id
ORDER BY names, tel;

SELECT * FROM ListPhones;

CREATE VIEW PersonPhones AS 
SELECT P.id AS person_id,  Trim(Concat(P.last_name, ', ', P.first_name, ' ', P.mid_name)) AS names, T.tel
	FROM Person P
    LEFT JOIN Phone T ON P.id = T.id_person
ORDER BY names, tel;

SELECT * FROM PersonPhones;

SELECT * FROM PersonPhones
WHERE tel IS NULL;


CREATE TABLE Emails(
	id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(12) NOT NULL UNIQUE,
    id_person INT NOT NULL,
    CONSTRAINT e_mail FOREIGN KEY (id_person)
    REFERENCES Person(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Emails (email, id_person) VALUES ('p2@mail.bg', 2);

SELECT * FROM Emails;

CREATE VIEW PersonEmails AS 
SELECT P.id AS person_id,  Trim(Concat(P.last_name, ', ', P.first_name, ' ', P.mid_name)) AS names, E.email
	FROM Person P
    LEFT JOIN Emails E ON P.id = E.id_person
ORDER BY names, email;

SELECT * FROM PersonEmails;

CREATE VIEW ListEmails AS
SELECT * FROM PersonEmails
WHERE email IS NOT NULL;
SELECT * FROM ListEmails;

CREATE VIEW IncompleteEmailList AS 
SELECT * FROM PersonEmails
WHERE email IS NULL;
SELECT * FROM IncompleteEmailList;

CREATE VIEW IncompleteEmailList2 AS 
SELECT * FROM PersonEmails
WHERE person_id NOT IN (SELECT DISTINCT person_id FROM ListEmails);
SELECT email, names FROM IncompleteEmailList2;

CREATE VIEW TelEmail AS
SELECT T.person_id AS id, T.names, T.tel, E.email,
		(SELECT DATE(created_at)
			FROM Person
            WHERE id = T.person_id) AS person_reg
FROM PersonEmails AS E
LEFT JOIN PersonPhones AS T ON E.person_id = T.person_id
ORDER BY names, email, tel;
SELECT * FROM TelEmail;


CREATE VIEW FreshPersonalInfo AS
SELECT Tbl.names, Tbl.email, Tbl.tel, Tbl.person_reg,
		DATEDIFF(date(now()), Tbl.person_reg) AS DaysAgo,
        Tbl.id AS id_person
FROM TelEmail AS Tbl
ORDER BY DaysAgo, names, email, tel;

SELECT * FROM FreshPersonalInfo;

DELIMITER //
CREATE PROCEDURE DisplayFreshPersonRegs(IN days INT)
BEGIN 
	SELECT * 
    FROM FreshPersonalInfo
    WHERE daysAgo <= days;
END //

CALL DisplayFreshPersonRegs(0);
CALL DisplayFreshPersonRegs(1);

DELIMITER //
CREATE FUNCTION CountFreshPersonRegs(days INT)
RETURNS INT
DETERMINISTIC
BEGIN
	DECLARE res INT;
	SELECT Count(DISTINCT names) INTO res
    FROM FreshPersonalInfo
    WHERE DaysAgo <= days;
    
    RETURN res;
END //
