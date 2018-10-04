ALTER TABLE pages ADD COLUMN cover_url character varying(1000);

UPDATE pages SET cover_url = '/images/cover.jpg';