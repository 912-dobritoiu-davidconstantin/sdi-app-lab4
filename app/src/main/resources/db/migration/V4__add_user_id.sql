ALTER TABLE author ADD COLUMN user_id integer;
ALTER TABLE book ADD COLUMN user_id integer;
ALTER TABLE library ADD COLUMN user_id integer;
ALTER TABLE library_book ADD COLUMN user_id integer;