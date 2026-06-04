CREATE DATABASE booknest;
SHOW DATABASES;
USE booknest;

SHOW TABLES;

DESC book;

SET SESSION cte_max_recursion_depth = 10000;

USE booknest;

CREATE TABLE numbers (
    n INT PRIMARY KEY
);

INSERT INTO numbers VALUES
(1),(2),(3),(4),(5),(6),(7),(8),(9),(10);

INSERT INTO numbers
SELECT n + (SELECT MAX(n) FROM numbers)
FROM numbers;

SELECT COUNT(*) FROM numbers;

truncate table book;

INSERT INTO book (isbn, title, author, price, available)
SELECT
    n,
    CONCAT('Book ', n),
    CONCAT('Author ', n),
    FLOOR(100 + (RAND() * 900)),
    TRUE
FROM numbers
LIMIT 10000;

SELECT COUNT(*) FROM book;
SELECT * FROM book ;

