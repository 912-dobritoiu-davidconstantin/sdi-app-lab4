INSERT INTO library_book (book_id, library_id, borrow_date, return_date)
SELECT
  b.id AS book_id,
  l.id AS library_id,
  CURRENT_DATE - INTERVAL '1 year' + floor(random() * 365) * INTERVAL '1 day' AS borrow_date,
  CASE WHEN random() < 0.5 THEN NULL ELSE CURRENT_DATE - INTERVAL '1 year' + floor(random() * 365) * INTERVAL '1 day' END AS return_date
FROM generate_series(1, 10000000) s
CROSS JOIN (
  SELECT id FROM book ORDER BY random() LIMIT 1
) b
CROSS JOIN (
  SELECT id FROM library ORDER BY random() LIMIT 1
) l
WHERE NOT EXISTS (
  SELECT 1 FROM library_book
  WHERE book_id = b.id
  AND library_id = l.id
  LIMIT 1
);
