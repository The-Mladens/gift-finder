CREATE OR REPLACE PROCEDURE ad_user_insert(
    p_name TEXT,
    p_email TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO ad_users(name, email)
    VALUES (p_name, p_email);
END;
$$;
