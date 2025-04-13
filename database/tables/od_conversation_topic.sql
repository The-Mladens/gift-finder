CREATE TABLE od_conversation_topic (
    id SERIAL PRIMARY KEY,
    od_conversation_id INT NOT NULL REFERENCES od_conversation(id),
    md_topic_template_id INT NOT NULL REFERENCES md_question_topic_template(id),
    user_response TEXT,
    structured_response JSONB,
    status TEXT CHECK (status IN ('new', 'open', 'closed')) DEFAULT 'new'
);

COMMENT ON TABLE od_conversation_topic IS 'Теми в разговори';
COMMENT ON COLUMN od_conversation_topic.od_conversation_id IS 'Референция към разговор';
COMMENT ON COLUMN od_conversation_topic.md_topic_template_id IS 'Референция към шаблон на тема';
COMMENT ON COLUMN od_conversation_topic.user_response IS 'Потребителски отговор';
COMMENT ON COLUMN od_conversation_topic.structured_response IS 'Структуриран отговор';
COMMENT ON COLUMN od_conversation_topic.status IS 'Статус на темата в разговора';

CREATE INDEX idx_od_conv_topic_conv_id ON od_conversation_topic(od_conversation_id);
