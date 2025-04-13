CREATE TABLE od_conversation (
    id SERIAL PRIMARY KEY,
    od_user_id INT NOT NULL REFERENCES od_user(id),
    name TEXT,
    started_at TIMESTAMP NOT NULL DEFAULT now(),
    ended_at TIMESTAMP
);

COMMENT ON TABLE od_conversation IS 'Разговор с потребител';
COMMENT ON COLUMN od_conversation.od_user_id IS 'Референция към потребителя';
COMMENT ON COLUMN od_conversation.name IS 'Наименование на разговора';
COMMENT ON COLUMN od_conversation.started_at IS 'Начален момент на разговора';
COMMENT ON COLUMN od_conversation.ended_at IS 'Краен момент на разговора';

CREATE INDEX idx_od_conversation_user_id ON od_conversation(od_user_id);
