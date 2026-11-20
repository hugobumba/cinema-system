--liquibase formatted sql
--changeset copilot:020-seed-test context:test

-- INSERT USERS
INSERT INTO users (name, email, password, role)
SELECT 'Hugo Bumba', 'hugo@mail', '123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'hugo@mail');

INSERT INTO users (name, email, password, role)
SELECT 'John Doe', 'john@mail', '123', 'CUSTOMER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'john@mail');

--INSERT CINEMAS
INSERT INTO cinema (name, location)
SELECT 'Cinema Lisbon', 'Lisbon'
WHERE NOT EXISTS (SELECT 1 FROM cinema WHERE name = 'Cinema Lisbon' AND location = 'Lisbon');

INSERT INTO cinema (name, location)
SELECT 'Cinema Porto', 'Porto'
WHERE NOT EXISTS (SELECT 1 FROM cinema WHERE name = 'Cinema Porto' AND location = 'Porto');

INSERT INTO cinema (name, location)
SELECT 'Cinema Braga', 'Braga'
WHERE NOT EXISTS (SELECT 1 FROM cinema WHERE name = 'Cinema Braga' AND location = 'Braga');

-- INSERT ROOMS
INSERT INTO room (number, cinema_id)
SELECT '1', c.id FROM cinema c
WHERE c.name = 'Cinema Lisbon'
  AND NOT EXISTS (SELECT 1 FROM room r WHERE r.cinema_id = c.id AND r.number = '1');

INSERT INTO room (number, cinema_id)
SELECT '2', c.id FROM cinema c
WHERE c.name = 'Cinema Lisbon'
  AND NOT EXISTS (SELECT 1 FROM room r WHERE r.cinema_id = c.id AND r.number = '2');

INSERT INTO room (number, cinema_id)
SELECT '3', c.id FROM cinema c
WHERE c.name = 'Cinema Lisbon'
  AND NOT EXISTS (SELECT 1 FROM room r WHERE r.cinema_id = c.id AND r.number = '3');

INSERT INTO room (number, cinema_id)
SELECT '4', c.id FROM cinema c
WHERE c.name = 'Cinema Lisbon'
  AND NOT EXISTS (SELECT 1 FROM room r WHERE r.cinema_id = c.id AND r.number = '4');

INSERT INTO room (number, cinema_id)
SELECT '5', c.id FROM cinema c
WHERE c.name = 'Cinema Lisbon'
  AND NOT EXISTS (SELECT 1 FROM room r WHERE r.cinema_id = c.id AND r.number = '5');

INSERT INTO room (number, cinema_id)
SELECT '1', c.id FROM cinema c
WHERE c.name = 'Cinema Porto'
  AND NOT EXISTS (SELECT 1 FROM room r WHERE r.cinema_id = c.id AND r.number = '1');

INSERT INTO room (number, cinema_id)
SELECT '2', c.id FROM cinema c
WHERE c.name = 'Cinema Porto'
  AND NOT EXISTS (SELECT 1 FROM room r WHERE r.cinema_id = c.id AND r.number = '2');

INSERT INTO room (number, cinema_id)
SELECT '3', c.id FROM cinema c
WHERE c.name = 'Cinema Porto'
  AND NOT EXISTS (SELECT 1 FROM room r WHERE r.cinema_id = c.id AND r.number = '3');

INSERT INTO room (number, cinema_id)
SELECT '1', c.id FROM cinema c
WHERE c.name = 'Cinema Braga'
  AND NOT EXISTS (SELECT 1 FROM room r WHERE r.cinema_id = c.id AND r.number = '1');

-- INSERT SEATS
INSERT INTO seat (room_id, seat_row, seat_number)
SELECT r.id, seat_rows.seat_row, seat_numbers.seat_number
FROM room r
JOIN cinema c ON c.id = r.cinema_id
CROSS JOIN (SELECT 'A' AS seat_row UNION ALL SELECT 'B') seat_rows
CROSS JOIN (SELECT '1' AS seat_number UNION ALL SELECT '2' UNION ALL SELECT '3') seat_numbers
WHERE (
    (c.name = 'Cinema Lisbon' AND r.number IN ('1', '2', '3'))
    OR (c.name = 'Cinema Porto' AND r.number IN ('1', '2'))
    OR (c.name = 'Cinema Braga' AND r.number = '1')
)
AND NOT EXISTS (
    SELECT 1
    FROM seat s
    WHERE s.room_id = r.id
      AND s.seat_row = seat_rows.seat_row
      AND s.seat_number = seat_numbers.seat_number
);

-- INSERT MOVIES
INSERT INTO movie (title, description, poster_url, genre, duration_in_minutes, age_rating)
SELECT 'Fast and Furious', 'Deterministic test fixture', NULL, 'ACTION', 140, 'AGE_16'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE title = 'Fast and Furious');

INSERT INTO movie (title, description, poster_url, genre, duration_in_minutes, age_rating)
SELECT 'Avatar', 'Deterministic test fixture', NULL, 'SCIENCE_FICTION', 150, 'AGE_12'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE title = 'Avatar');

INSERT INTO movie (title, description, poster_url, genre, duration_in_minutes, age_rating)
SELECT 'Spiderman', 'Deterministic test fixture', NULL, 'ACTION', 130, 'AGE_12'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE title = 'Spiderman');

INSERT INTO movie (title, description, poster_url, genre, duration_in_minutes, age_rating)
SELECT 'Titanic', 'Deterministic test fixture', NULL, 'DRAMA', 145, 'AGE_18'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE title = 'Titanic');

INSERT INTO movie (title, description, poster_url, genre, duration_in_minutes, age_rating)
SELECT 'Up', 'Deterministic test fixture', NULL, 'ANIMATION', 100, 'AGE_6'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE title = 'Up');

INSERT INTO movie (title, description, poster_url, genre, duration_in_minutes, age_rating)
SELECT 'Cinderella', 'Deterministic test fixture', NULL, 'ANIMATION', 110, 'AGE_6'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE title = 'Cinderella');

INSERT INTO movie (title, description, poster_url, genre, duration_in_minutes, age_rating)
SELECT 'Jumanji', 'Deterministic test fixture', NULL, 'ADVENTURE', 120, 'AGE_12'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE title = 'Jumanji');

INSERT INTO movie (title, description, poster_url, genre, duration_in_minutes, age_rating)
SELECT 'Michael', 'Deterministic test fixture', NULL, 'BIOGRAPHY', 90, 'ALL'
WHERE NOT EXISTS (SELECT 1 FROM movie WHERE title = 'Michael');

-- INSERT SHOWTIMES
INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-01 20:00:00',
    10.00
FROM room r
JOIN cinema c ON c.id = r.cinema_id
JOIN movie m ON m.title = 'Spiderman'
WHERE c.name = 'Cinema Lisbon'
  AND r.number = '1'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-01 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-02 20:00:00',
    10.00
FROM room r
JOIN cinema c ON c.id = r.cinema_id
JOIN movie m ON m.title = 'Avatar'
WHERE c.name = 'Cinema Lisbon'
  AND r.number = '1'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-02 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-01 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Fast and Furious'
WHERE c.name = 'Cinema Lisbon'
  AND r.number = '2'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-01 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-02 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Spiderman'
WHERE c.name = 'Cinema Lisbon'
  AND r.number = '2'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-02 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-01 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Michael'
WHERE c.name = 'Cinema Lisbon'
  AND r.number = '3'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-01 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-02 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Cinderella'
WHERE c.name = 'Cinema Lisbon'
  AND r.number = '3'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-02 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-01 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Spiderman'
WHERE c.name = 'Cinema Porto'
  AND r.number = '1'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-01 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-02 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Michael'
WHERE c.name = 'Cinema Porto'
  AND r.number = '1'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-02 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-01 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Up'
WHERE c.name = 'Cinema Porto'
  AND r.number = '2'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-01 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-02 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Spiderman'
WHERE c.name = 'Cinema Porto'
  AND r.number = '2'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-02 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-01 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Jumanji'
WHERE c.name = 'Cinema Braga'
  AND r.number = '1'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-01 20:00:00'
);

INSERT INTO showtime (room_id, movie_id, start_time, price)
SELECT
    r.id,
    m.id,
    TIMESTAMP '2026-01-02 20:00:00',
    10.00
FROM room r
         JOIN cinema c ON c.id = r.cinema_id
         JOIN movie m ON m.title = 'Titanic'
WHERE c.name = 'Cinema Braga'
  AND r.number = '1'
  AND NOT EXISTS (
    SELECT 1 FROM showtime st
    WHERE st.room_id = r.id AND st.start_time = TIMESTAMP '2026-01-02 20:00:00'
);
