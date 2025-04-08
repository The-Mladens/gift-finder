-- SQL function to select user data based on a secret key
CREATE OR REPLACE FUNCTION getSystemMessage(secret_key TEXT)
RETURNS TEXT AS $$
DECLARE
    result TEXT;
BEGIN
    SELECT STRING_AGG(ch_c.request || ' ' || ch_c.response, ' ') INTO result
    FROM ch_completions ch_c;

    RETURN result;
END;
$$ LANGUAGE plpgsql;