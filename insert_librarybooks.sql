-- Set batch size
DO $$
DECLARE
  batch_size INT := 10000;
  total_records INT := 10000000;
BEGIN
  FOR i IN 0..((total_records-1)/batch_size) LOOP
    INSERT INTO library_book (book_id, library_id, borrow_date, return_date)
    SELECT
      b.book_id AS book_id,
      l.library_id AS library_id,
      CURRENT_DATE - INTERVAL '1 year' + floor(random() * 365) * INTERVAL '1 day' AS borrow_date,
      CASE WHEN random() < 0.5 THEN NULL ELSE CURRENT_DATE - INTERVAL '1 year' + floor(random() * 365) * INTERVAL '1 day' END AS return_date
    FROM generate_series(1, batch_size) s
    CROSS JOIN (
      SELECT book_id FROM book ORDER BY random() LIMIT 1
    ) b
    CROSS JOIN (
      SELECT library_id FROM library ORDER BY random() LIMIT 1
    ) l
    WHERE NOT EXISTS (
      SELECT 1 FROM library_book
      WHERE book_id = b.book_id
      AND library_id = l.library_id
      LIMIT 1
    );

    -- Raise notice for every batch
    IF (i+1) % 1 = 0 THEN
      RAISE NOTICE 'Inserted % records', (i+1)*batch_size;
    END IF;
  END LOOP;
END$$;

-- Create indexes
CREATE INDEX idx_library_book_book_id ON library_book(book_id);
CREATE INDEX idx_library_book_library_id ON library_book(library_id);

