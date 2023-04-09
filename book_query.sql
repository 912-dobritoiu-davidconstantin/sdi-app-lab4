INSERT INTO book (title, year, price, rating, author_id)
SELECT 
  CONCAT('Book ', LPAD(CAST(ROW_NUMBER() OVER (ORDER BY random()) AS TEXT), 7, '0')) AS title,
  CAST((1960 + (random() * 62)) AS INTEGER) AS year,
  ROUND((10.0 + (random() * 90.0) + (POWER(random(), 3) * 500.0))::numeric, 2) AS price,
  CAST((1 + (random() * 4)) AS INTEGER) AS rating,
  (SELECT id FROM author ORDER BY random() LIMIT 1) AS author_id
FROM 
  generate_series(1, 1000000);
