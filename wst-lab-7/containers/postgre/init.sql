CREATE
USER admin WITH PASSWORD 'admin';
CREATE
DATABASE db_lab1_wst;
GRANT ALL PRIVILEGES ON DATABASE
db_lab1_wst TO admin;
\c db_lab1_wst admin;
CREATE TABLE students
(
    id           BIGSERIAL PRIMARY KEY,
    email        VARCHAR(255),
    password     VARCHAR(255),
    group_number VARCHAR(255),
    birth_date   DATE,
    is_local     BOOLEAN
);

INSERT INTO students (email, password, group_number, birth_date, is_local)
VALUES ('first@mail.ru', 'first', '1a', '1998.01.01', 'true');
INSERT INTO students (email, password, group_number, birth_date, is_local)
VALUES ('second@mail.ru', 'second', '2a', '1998.02.02', 'true');
INSERT INTO students (email, password, group_number, birth_date, is_local)
VALUES ('third@mail.ru', 'third', '3a', '1998.03.03', 'false');