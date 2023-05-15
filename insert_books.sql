-- Set batch size
DO $$
DECLARE
  batch_size INT := 1000;
  total_books INT := 1000000;
BEGIN
  FOR i IN 0..((total_books-1)/batch_size) LOOP
    WITH random_authors AS (
      SELECT author_id
      FROM author
      WHERE author_id >= (SELECT FLOOR(RANDOM() * (SELECT MAX(author_id) FROM author)))
      ORDER BY author_id
      LIMIT batch_size
    )
    -- Insert into the book table using the random author list
    INSERT INTO book (title, year, price, rating, author_id)
    SELECT
      CONCAT('Book ', LPAD(CAST(ROW_NUMBER() OVER (ORDER BY random()) + (i*batch_size) AS TEXT), 7, '0')) AS title,
      CAST((1960 + (random() * 62)) AS INTEGER) AS year,
      ROUND((10.0 + (random() * 90.0) + (POWER(random(), 3) * 500.0))::numeric, 2) AS price,
      CAST((1 + (random() * 4)) AS INTEGER) AS rating,
      a.author_id
    FROM
      generate_series(1, batch_size) s
      JOIN random_authors a ON s = (a.author_id - (SELECT MIN(author_id) FROM random_authors) + 1);

    -- Raise notice for every 10k books
    IF (i+1) % 10 = 0 THEN
      RAISE NOTICE 'Inserted % books', (i+1)*batch_size;
    END IF;
  END LOOP;
END$$;

