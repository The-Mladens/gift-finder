CREATE TABLE md_question_topic_element_template (
    id SERIAL PRIMARY KEY,
    md_topic_template_id INT NOT NULL REFERENCES md_question_topic_template(id),
    seq_no INT NOT NULL,
    name TEXT NOT NULL,
    extraction_question TEXT NOT NULL
);

COMMENT ON TABLE md_question_topic_element_template IS 'Шаблон за елементи към въпросна тема';
COMMENT ON COLUMN md_question_topic_element_template.md_topic_template_id IS 'Референция към шаблон на тема';
COMMENT ON COLUMN md_question_topic_element_template.seq_no IS 'Пореден номер на елемент';
COMMENT ON COLUMN md_question_topic_element_template.name IS 'Наименование на елемент';
COMMENT ON COLUMN md_question_topic_element_template.extraction_question IS 'Въпрос за извличане на отговор';

CREATE INDEX idx_md_qte_template_id ON md_question_topic_element_template(md_topic_template_id);
