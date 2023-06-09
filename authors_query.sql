INSERT INTO author (name, email, bio, country)
SELECT
CONCAT(first_name.val, ' ', last_name.val),
CONCAT(REPLACE(REPLACE(first_name.val, ' ', ''), '-', ''), '.', REPLACE(REPLACE(last_name.val, ' ', ''), '-', ''), '@example.com'),
CONCAT('Bio of author ', first_name.val, ' ', last_name.val),
country.val
FROM (
SELECT 'John' AS val UNION SELECT 'Jane' UNION SELECT 'Michael' UNION SELECT 'Emily' UNION SELECT 'William' UNION SELECT 'Sophia' UNION SELECT 'Daniel' UNION SELECT 'Olivia' UNION SELECT 'Alexander' UNION SELECT 'Ella'
) first_name,
(
SELECT 'Doe' AS val UNION SELECT 'Smith' UNION SELECT 'Johnson' UNION SELECT 'Brown' UNION SELECT 'Garcia' UNION SELECT 'Lee' UNION SELECT 'Williams' UNION SELECT 'Miller' UNION SELECT 'Davis' UNION SELECT 'Taylor'
) last_name,
(
SELECT 'United States' AS val UNION SELECT 'Canada' UNION SELECT 'Mexico' UNION SELECT 'Brazil' UNION SELECT 'Argentina' UNION SELECT 'France' UNION SELECT 'Germany' UNION SELECT 'Spain' UNION SELECT 'Italy' UNION SELECT 'Russia'
) country
CROSS JOIN (
SELECT 0 AS num UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9
) ones
CROSS JOIN (
SELECT 0 AS num UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9
) tens
CROSS JOIN (
SELECT 0 AS num UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9
) hundreds
CROSS JOIN (
SELECT 0 AS num UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9
) thousands
LIMIT 1000000;