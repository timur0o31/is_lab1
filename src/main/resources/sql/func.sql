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

CREATE OR REPLACE FUNCTION find_available_workers()
RETURNS SETOF worker AS $$
BEGIN
RETURN QUERY
SELECT w.* FROM worker w WHERE w.end_date IS NOT NULL
          AND NOT EXISTS (SELECT 1 FROM worker w2
            WHERE w2.person_id = w.person_id
            AND (w2.end_date IS NULL OR w2.end_date > CURRENT_DATE)
    );
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE hire_worker(p_worker_id INT,p_organization_id BIGINT)
AS $$
BEGIN
UPDATE worker
SET organization_id = p_organization_id,
    start_date = CURRENT_DATE,
    end_date = NULL WHERE id = p_worker_id;
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
        WHERE w.organization_id = org_id AND w.end_date is NULL;
END;
$$ LANGUAGE plpgsql;

