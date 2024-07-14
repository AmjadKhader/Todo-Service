-- Create the User table
CREATE TABLE IF NOT EXISTS users (
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL
);

ALTER TABLE tasks ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;