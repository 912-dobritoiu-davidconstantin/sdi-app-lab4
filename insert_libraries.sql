CREATE INDEX library_location_idx ON library(location);
CREATE INDEX library_owner_idx ON library(owner);
CREATE INDEX library_rating_idx ON library(rating);

INSERT INTO library (name, description, location, rating, owner)
SELECT
  CONCAT('Library ', LPAD(CAST(ROW_NUMBER() OVER (ORDER BY random()) AS TEXT), 7, '0')) AS name,
  CONCAT('This is a description of library ', LPAD(CAST(ROW_NUMBER() OVER (ORDER BY random()) AS TEXT), 7, '0'), '.') AS description,
  CONCAT('Location ', LPAD(CAST(ROW_NUMBER() OVER (ORDER BY random()) AS TEXT), 7, '0')) AS location,
  CAST((1 + (random() * 4)) AS INTEGER) AS rating,
  CONCAT('Owner ', LPAD(CAST(ROW_NUMBER() OVER (ORDER BY random()) AS TEXT), 7, '0')) AS owner
FROM
  generate_series(1, 1000000);

DO $$
DECLARE
  batch_size INTEGER := 10000;
  num_batches INTEGER := CEIL((SELECT COUNT(*) FROM library) / batch_size);
  i INTEGER;
BEGIN
  FOR i IN 1..num_batches LOOP
    UPDATE library
    SET
      name = CASE
        WHEN random() < 0.1 THEN CONCAT('Public Library of ', SUBSTRING(location, 10))
        WHEN random() < 0.3 THEN CONCAT('Library of ', SUBSTRING(owner, 7))
        ELSE name
      END,
      description = CASE
        WHEN random() < 0.2 THEN CONCAT('This is the largest library in ', SUBSTRING(location, 10), ' with over a million books and 24/7 service.')
        WHEN random() < 0.4 THEN CONCAT('Come visit the library of ', SUBSTRING(owner, 7), ' to find the latest bestsellers and rare books.')
        ELSE description
      END,
      rating = CASE
        WHEN random() < 0.05 THEN rating + 1
        WHEN random() < 0.1 THEN rating - 1
        ELSE rating
      END
    WHERE library_id BETWEEN ((i - 1) * batch_size + 1) AND (i * batch_size);
  END LOOP;
END $$;

