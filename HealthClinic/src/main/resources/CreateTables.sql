CREATE TABLE dbo.appointment
(
  appointment_id bigint,
  person_id bigint NOT NULL,
  "from" timestamp without time zone NOT NULL,
  "to" timestamp without time zone NOT NULL,
  intervention_id bigint NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE dbo.appointment
  OWNER TO postgres;

CREATE TABLE dbo.assigned_right
(
  user_id bigint NOT NULL,
  right_id bigint NOT NULL,
  CONSTRAINT assigned_right_pkey PRIMARY KEY (user_id, right_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE dbo.assigned_right
  OWNER TO postgres;
  
CREATE TABLE dbo.intervention
(
  intervention_id bigint NOT NULL,
  name character varying(200) NOT NULL,
  info text,
  CONSTRAINT intervention_pkey PRIMARY KEY (intervention_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE dbo.intervention
  OWNER TO postgres;
  
CREATE TABLE dbo.person
(
  person_id bigint NOT NULL,
  first_name character(128) NOT NULL,
  last_name character(128) NOT NULL,
  phone character(32) NOT NULL,
  email character(128),
  CONSTRAINT person_pkey PRIMARY KEY (person_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE dbo.person
  OWNER TO postgres;
  
CREATE TABLE dbo."right"
(
  right_id bigint NOT NULL,
  type character varying(200) NOT NULL,
  CONSTRAINT right_pkey PRIMARY KEY (right_id),
  CONSTRAINT right_type_key UNIQUE (type)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE dbo."right"
  OWNER TO postgres;
  
  
CREATE TABLE dbo."user"
(
  user_id bigint NOT NULL,
  login character varying(255),
  password character varying(255),
  CONSTRAINT user_pkey PRIMARY KEY (user_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE dbo."user"
  OWNER TO postgres;

