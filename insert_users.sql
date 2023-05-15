-- Insert 10,000 users with random profile IDs between 1 and 3
INSERT INTO users (password, username, user_profile_id)
SELECT 'password123', 'user' || id, floor(random() * 3) + 1
FROM generate_series(1, 10000) AS id;

