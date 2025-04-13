CREATE TABLE od_user (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    secret_key TEXT NOT NULL,
    remaining_requests INT NOT NULL DEFAULT 10
);

COMMENT ON TABLE od_user IS 'Потребител в системата';
COMMENT ON COLUMN od_user.name IS 'Име на потребителя';
COMMENT ON COLUMN od_user.email IS 'Имейл адрес';
COMMENT ON COLUMN od_user.secret_key IS 'Секретен ключ за идентификация';
COMMENT ON COLUMN od_user.remaining_requests IS 'Оставащи заявки за потребителя';
