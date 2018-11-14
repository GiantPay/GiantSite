CREATE SEQUENCE seq_roadmap;

CREATE TABLE roadmap (
  id bigint NOT NULL DEFAULT nextval('seq_roadmap'::regclass) PRIMARY KEY ,
  event       TEXT NOT NULL,
  date        TIMESTAMP WITH TIME ZONE not null,
  ready_value INT  NOT NULL
);


ALTER TABLE roadmap
  ADD CONSTRAINT percent_value_constraint CHECK
(ready_value >= 0 AND ready_value <= 100);