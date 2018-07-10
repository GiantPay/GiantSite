CREATE SEQUENCE seq_utm_sources;

CREATE TABLE utm_sources (
    id bigint NOT NULL DEFAULT nextval('seq_utm_sources'::regclass),
    url character varying(1000),
    source character varying(1000),
    CONSTRAINT utm_sources_pkey PRIMARY KEY (id)
);
