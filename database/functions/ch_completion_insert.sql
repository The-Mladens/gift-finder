CREATE OR REPLACE PROCEDURE ch_completion_insert(
    p_request TEXT,
    p_response TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO ch_completions(request, response)
    VALUES (p_request, p_response);
END;
$$;
