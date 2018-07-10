CREATE SEQUENCE seq_pages;

CREATE TABLE pages (
    id bigint NOT NULL DEFAULT nextval('seq_pages'::regclass),
    created_id bigint,
    created_at timestamp without time zone,
    url character varying(1000),
    seo_title character varying(1000),
    seo_description character varying(4000),
    seo_keywords character varying(1000),
    title character varying(1000),
    announcement character varying(4000),
    text character varying(1000000),
    tags character varying(1000),
    visible boolean DEFAULT false,
    CONSTRAINT articles_pkey PRIMARY KEY (id),
    CONSTRAINT articles_created_id_fkey FOREIGN KEY (created_id) REFERENCES users (id)
);
