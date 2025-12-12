CREATE OR REPLACE FUNCTION sum_worker_ratings()
RETURNS FLOAT AS $$
BEGIN
RETURN COALESCE((SELECT SUM(rating) FROM worker),0);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_workers_by_name_prefix(prefix TEXT)
RETURNS SETOF worker AS $$
BEGIN
RETURN QUERY
SELECT * FROM worker w WHERE w.name ILIKE prefix || '%';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_workers_by_end_date(after_date DATE)
RETURNS SETOF worker AS $$
BEGIN
RETURN QUERY
SELECT * FROM worker w WHERE w.end_date > after_date;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION hire_worker(
    p_person_id BIGINT,
    p_org BIGINT,
    p_name TEXT,
    p_salary FLOAT,
    p_rating INT,
    p_position VARCHAR(100),
    p_x BIGINT,
    p_y INT
)
RETURNS BOOLEAN AS $$
DECLARE active_count INT;
BEGIN
SELECT COUNT(*) INTO active_count
FROM worker
WHERE person_id = p_person_id
  AND (end_date IS NULL OR end_date > CURRENT_DATE);

IF active_count > 0 THEN
        RETURN FALSE;
END IF;

INSERT INTO worker(name,salary,rating,position,person_id,organization_id,creation_date,start_date,end_date,x,y)
VALUES (p_name,p_salary,p_rating, p_position, p_person_id, p_org,Current_timestamp, CURRENT_DATE, NULL, p_x,p_y);

RETURN TRUE;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE PROCEDURE fire_worker(p_worker_id BIGINT)
AS $$
BEGIN
UPDATE worker
SET end_date = CURRENT_DATE WHERE id = p_worker_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION find_workers_by_organization(org_id BIGINT)
RETURNS SETOF worker
AS $$
BEGIN
    RETURN QUERY
        SELECT * FROM worker w
        WHERE w.organization_id = org_id AND (w.end_date is NULL OR w.end_date > CURRENT_DATE);
END;
$$ LANGUAGE plpgsql;

