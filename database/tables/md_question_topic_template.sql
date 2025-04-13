CREATE TABLE md_question_topic_template (
    id SERIAL PRIMARY KEY,
    seq_no INT NOT NULL,
    name TEXT NOT NULL,
    system_question TEXT NOT NULL,
    system_prompt TEXT,
    system_check_prompt TEXT,
    confirmation_question TEXT,
    response_structure JSONB
);

COMMENT ON TABLE md_question_topic_template IS 'Шаблон за въпросна тема';
COMMENT ON COLUMN md_question_topic_template.seq_no IS 'Пореден номер на темата';
COMMENT ON COLUMN md_question_topic_template.name IS 'Наименование на темата';
COMMENT ON COLUMN md_question_topic_template.system_question IS 'Откриващ въпрос от системата';
COMMENT ON COLUMN md_question_topic_template.system_prompt IS 'Системен промпт въпрос';
COMMENT ON COLUMN md_question_topic_template.system_check_prompt IS 'Системен промпт за проверка на отговор';
COMMENT ON COLUMN md_question_topic_template.confirmation_question IS 'Въпрос за потвърждение от потребителя';
COMMENT ON COLUMN md_question_topic_template.response_structure IS 'Структура на очаквания отговор';

CREATE UNIQUE INDEX uq_md_question_topic_template_name ON md_question_topic_template(name);
