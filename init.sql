-- -- Create db 'authors'
-- CREATE DATABASE authors;
--
-- -- Connect to 'authors' database
-- \c authors;

-- Create the 'author' table
CREATE TABLE IF NOT EXISTS public.author(
    id serial PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255)
);