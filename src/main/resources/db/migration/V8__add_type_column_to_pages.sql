ALTER TABLE pages ADD COLUMN type character varying(20);

UPDATE pages SET type = 'html';