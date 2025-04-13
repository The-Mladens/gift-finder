CREATE TABLE od_communication (
    id SERIAL PRIMARY KEY,
    od_conversation_topic_id INT NOT NULL REFERENCES od_conversation_topic(id),
    started_at TIMESTAMP NOT NULL DEFAULT now(),
    system_prompt TEXT NOT NULL,
    user_response TEXT,
    processing_duration_ms INT,
    llm_result JSONB
);

COMMENT ON TABLE od_communication IS 'Комуникация по тема в разговор';
COMMENT ON COLUMN od_communication.od_conversation_topic_id IS 'Референция към тема в разговор';
COMMENT ON COLUMN od_communication.started_at IS 'Начало на комуникацията';
COMMENT ON COLUMN od_communication.system_prompt IS 'Системен промпт';
COMMENT ON COLUMN od_communication.user_response IS 'Потребителски отговор';
COMMENT ON COLUMN od_communication.processing_duration_ms IS 'Продължителност на обработката (ms)';
COMMENT ON COLUMN od_communication.llm_result IS 'Резултат от LLM обработката';

CREATE INDEX idx_od_communication_topic_id ON od_communication(od_conversation_topic_id);
