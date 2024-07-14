-- Create the Board table
CREATE TABLE IF NOT EXISTS boards (
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     TEXT
);

-- Create the Task table
CREATE TABLE IF NOT EXISTS tasks (
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    user_id         UUID NULL,
    status          VARCHAR(50) NOT NULL,
    board_id        UUID,
    FOREIGN KEY     (board_id) REFERENCES boards(id) ON DELETE CASCADE
);

-- Create indexes for Task table
CREATE INDEX idx_board_id ON tasks(board_id);
CREATE INDEX user_id ON tasks(user_id);
