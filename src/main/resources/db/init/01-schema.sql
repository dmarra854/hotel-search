CREATE TABLE IF NOT EXISTS hotel_search (
    id        BIGSERIAL    PRIMARY KEY,
    search_id VARCHAR(36)  NOT NULL,
    hotel_id  VARCHAR(50)  NOT NULL,
    check_in  DATE         NOT NULL,
    check_out DATE         NOT NULL,
    ages      INTEGER[]    NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_hs_search_id
    ON hotel_search (search_id);

CREATE INDEX IF NOT EXISTS idx_hs_criteria
    ON hotel_search (hotel_id, check_in, check_out);
