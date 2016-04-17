 CREATE SEQUENCE dbo.appointment_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE dbo.appointment_id_seq
  OWNER TO postgres;
  
 CREATE SEQUENCE dbo.user_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 8
  CACHE 1;
ALTER TABLE dbo.user_id_seq
  OWNER TO postgres;
  
 CREATE SEQUENCE dbo.person_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE dbo.person_id_seq
  OWNER TO postgres;
  
 CREATE SEQUENCE dbo.intervention_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE dbo.intervention_id_seq
  OWNER TO postgres;