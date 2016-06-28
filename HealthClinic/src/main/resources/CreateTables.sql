set schema 'dbo';

/* Create Tables */
Create table "system_user"
(
	"name" Varchar(64) NOT NULL UNIQUE,
	"password" Varchar(128) NOT NULL,
	"id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "permission"
(
	"id" BigSerial NOT NULL,
	"permission_profile_id" Bigint NOT NULL,
	"type" Varchar(256) NOT NULL UNIQUE,
 primary key ("id")
) Without Oids;


Create table "appointment"
(
	"id" BigSerial NOT NULL,
	"created_by" Bigint NOT NULL,
	"from" Timestamp NOT NULL,
	"to" Timestamp NOT NULL,
	"treatment_id" Bigint NOT NULL,
	"created_on" Timestamp NOT NULL,
	"state" Varchar(128) NOT NULL,
	"individual_id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "individual"
(
	"first_name" Varchar(128) NOT NULL,
	"id" Bigint NOT NULL,
	"birthdate" Timestamp,
 primary key ("id")
) Without Oids;


Create table "assigned_permission_profile"
(
	"permission_profile_id" Bigint NOT NULL,
	"id" Bigint NOT NULL,
 primary key ("permission_profile_id","id")
) Without Oids;


Create table "treatment_type"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(200) NOT NULL,
	"info" Text,
	"category" Varchar(1024) NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
	"created_by" Bigint NOT NULL,
	"modified_by" Bigint,
	"physician_type_id" Bigint NOT NULL,
	"price" Double precision NOT NULL,
 primary key ("id")
) Without Oids;


Create table "patient"
(
	"id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "physician"
(
	"id" Bigint NOT NULL,
    "physician_type_id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;

Create table "physician_type"
(
	"id" BigSerial NOT NULL,
	"modified_by" Bigint,
	"created_by" Bigint NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;

Create table "skill"
(
	"id" BigSerial NOT NULL,
	"created_by" Bigint NOT NULL,
	"name" Varchar(256) NOT NULL,
	"created_on" Timestamp NOT NULL Default CURRENT_DATE,
 primary key ("id")
) Without Oids;


Create table "skill_assignment"
(
	"id" BigSerial NOT NULL,
	"created_by" Bigint NOT NULL,
	"physician_type_id" Bigint NOT NULL,
	"skill_id" Bigint NOT NULL,
	"created_on" Timestamp NOT NULL Default CURRENT_DATE,
 primary key ("id")
) Without Oids;


Create table "nurse"
(
	"id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "receptionist"
(
	"id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "company"
(
	"id" Bigint NOT NULL,
	"registration_number" Varchar(256) NOT NULL,
 primary key ("id")
) Without Oids;


Create table "party"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(256) NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
	"created_by" Bigint NOT NULL,
	"modified_by" Bigint,
 primary key ("id")
) Without Oids;


Create table "party_role"
(
	"id" BigSerial NOT NULL,
	"target" Bigint NOT NULL,
	"source" Bigint NOT NULL,
	"created_on" Timestamp NOT NULL Default CURRENT_DATE,
	"modified_on" Timestamp,
	"created_by" Bigint NOT NULL,
	"modified_by" Bigint,
 primary key ("id")
) Without Oids;


Create table "permission_profile"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(256) NOT NULL,
 primary key ("id")
) Without Oids;

Create table "room"
(
	"id" BigSerial NOT NULL,
	"modified_by" Bigint,
	"created_by" Bigint NOT NULL,
	"name" Varchar(256) NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;


Create table "device"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(512) NOT NULL,
 primary key ("id")
) Without Oids;


Create table "room_assignment"
(
	"id" BigSerial NOT NULL,
	"treatment_type_id" Bigint NOT NULL,
	"room_id" Bigint NOT NULL,
	"created_by" Bigint NOT NULL,
	"created_on" Timestamp NOT NULL,
 primary key ("id")
) Without Oids;


Create table "communication_channel"
(
	"id" BigSerial NOT NULL,
	"value" Varchar(256) NOT NULL,
	"discriminator" Integer NOT NULL UNIQUE,
 primary key ("id")
) Without Oids;


Create table "config"
(
	"name" Varchar(256) NOT NULL,
	"value" Varchar(256),
 primary key ("name")
) Without Oids;

-- inserting default settings
insert into config (name, value) values ('default_company_name', 'Company name');
insert into config (name, value) values ('default_individual_name', 'admin');
insert into config (name, value) values ('default_user_login', 'admin');
insert into config (name, value) values ('default_user_password', 'admin');

-- init
DO $$
DECLARE 
  user_id bigint;
  company_id bigint;
  individual_id bigint;
	
BEGIN
  -- inserting admin (default user)
  insert into party_role (created_on, created_by, source, target) values(CURRENT_DATE, -1, -1, -1)
  RETURNING id INTO user_id;
  insert into system_user (name, "password", id) 
  values ((select "value" from config where name = 'default_user_login'), (select "value" from config where name = 'default_user_password'), user_id);
  update party_role set created_by = user_id where id = user_id;
  
  -- inserting owner company
  insert into party (name, created_on, created_by) values ((select "value" from config where name = 'default_company_name'), CURRENT_DATE, user_id)
  RETURNING id INTO company_id;
  insert into company (registration_number, id) values ('00000000', company_id);

  -- inserting individual representing admin user
  insert into party (name, created_on, created_by) values ((select "value" from config where name = 'default_user_login'), CURRENT_DATE, user_id)
  RETURNING id INTO individual_id;
  insert into individual (first_name, id) values ((select "value" from config where name = 'default_user_login'), individual_id); 

  -- update target and source for party_role (default user)
  update party_role set target = company_id, source = individual_id where id = user_id;
  
END $$;


/* Create Foreign Keys */

Alter table "assigned_permission_profile" add  foreign key ("id") references "system_user" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("created_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "treatment_type" add  foreign key ("created_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "treatment_type" add  foreign key ("modified_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "party" add  foreign key ("created_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "party" add  foreign key ("modified_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "party_role" add  foreign key ("created_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "party_role" add  foreign key ("modified_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("individual_id") references "individual" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("treatment_id") references "treatment_type" ("id") on update restrict on delete restrict;

Alter table "party_role" add  foreign key ("source") references "party" ("id") on update restrict on delete restrict;

Alter table "party_role" add  foreign key ("target") references "party" ("id") on update restrict on delete restrict;

Alter table "individual" add  foreign key ("id") references "party" ("id") on update restrict on delete restrict;

Alter table "company" add  foreign key ("id") references "party" ("id") on update restrict on delete restrict;

Alter table "physician" add  foreign key ("id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "nurse" add  foreign key ("id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "receptionist" add  foreign key ("id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "patient" add  foreign key ("id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "system_user" add  foreign key ("id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "assigned_permission_profile" add  foreign key ("permission_profile_id") references "permission_profile" ("id") on update restrict on delete restrict;

Alter table "permission" add  foreign key ("permission_profile_id") references "permission_profile" ("id") on update restrict on delete restrict;

Alter table "skill_assignment" add  foreign key ("skill_id") references "skill" ("id") on update restrict on delete restrict;

Alter table "physician" add  foreign key ("physician_type_id") references "physician_type" ("id") on update restrict on delete restrict;

Alter table "skill_assignment" add  foreign key ("physician_type_id") references "physician_type" ("id") on update restrict on delete restrict;

Alter table "treatment_type" add  foreign key ("physician_type_id") references "physician_type" ("id") on update restrict on delete restrict;

Alter table "room_assignment" add  foreign key ("treatment_type_id") references "treatment_type" ("id") on update restrict on delete restrict;

Alter table "room_assignment" add  foreign key ("created_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "room" add  foreign key ("created_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "room" add  foreign key ("modified_by") references "system_user" ("id") on update restrict on delete restrict;

Alter table "room_assignment" add  foreign key ("room_id") references "room" ("id") on update restrict on delete restrict;

