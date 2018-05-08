CREATE SEQUENCE seq_users;
CREATE SEQUENCE seq_presale_settings;
CREATE SEQUENCE seq_presale_requests;

CREATE TABLE users (
    id bigint NOT NULL DEFAULT nextval('seq_users'::regclass),
    username character varying(100),
    password character varying(100),
    email character varying(100),
    role character varying(20),
    status character varying(20),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE presale_settings (
    id bigint NOT NULL DEFAULT nextval('seq_presale_settings'::regclass),
    enabled boolean DEFAULT false,
    btc_price numeric NOT NULL DEFAULT 0,
    gic_amount numeric NOT NULL DEFAULT 0,
    awaiting_time int DEFAULT 0,
    CONSTRAINT coins_pkey PRIMARY KEY (id)
);

CREATE TABLE presale_requests (
    id bigint NOT NULL DEFAULT nextval('seq_presale_requests'::regclass),
    guid character varying(100),
    status character varying(20),
    email character varying(1000),
    created_at timestamp without time zone,
    finished_at timestamp without time zone,
    btc_address character varying(100),
    btc_account character varying(100),
    btc_tx character varying(100),
    gic_address character varying(100),
    gic_tx character varying(100),
    btc_tx_at timestamp without time zone,
    gic_tx_at timestamp without time zone,
    btc_amount numeric NOT NULL DEFAULT 0,
    gic_amount numeric NOT NULL DEFAULT 0,
    CONSTRAINT exchanges_pkey PRIMARY KEY (id)
);
