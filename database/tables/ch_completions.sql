CREATE TABLE ch_completions (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    request TEXT,
    response TEXT
);
