set schema 'dbo';

Create table "user"
(
	"id" BigSerial NOT NULL,
	"login" Varchar(64) NOT NULL UNIQUE,
	"password" Varchar(128) NOT NULL,
	"party_role_id" Bigint NOT NULL,
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
	"physician_id" Bigint NOT NULL,
	"from" Timestamp NOT NULL,
	"to" Timestamp NOT NULL,
	"treatment_id" Bigint NOT NULL,
	"created_by" Bigint NOT NULL,
	"created_on" Timestamp NOT NULL,
	"state" Varchar(128) NOT NULL,
	"individual_id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "individual"
(
	"first_name" Varchar(128) NOT NULL,
	"phone" Varchar(32) NOT NULL,
	"email" Varchar(128),
	"party_id" Bigint NOT NULL,
	"birthdate" Timestamp,
 primary key ("party_id")
) Without Oids;


Create table "assigned_permission_profile"
(
	"user_id" Bigint NOT NULL,
	"permission_profile_id" Bigint NOT NULL,
 primary key ("user_id","permission_profile_id")
) Without Oids;


Create table "treatment_type"
(
	"id" BigSerial NOT NULL,
	"modified_by" Bigint,
	"created_by" Bigint NOT NULL,
	"name" Varchar(200) NOT NULL,
	"info" Text,
	"type" Varchar(1024) NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;


Create table "patient"
(
	"id" BigSerial NOT NULL,
	"party_role_id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "physician"
(
	"party_role_id" Bigint NOT NULL,
	"id" BigSerial NOT NULL,
 primary key ("id")
) Without Oids;


Create table "nurse"
(
	"party_role_id" Bigint NOT NULL,
	"id" BigSerial NOT NULL,
 primary key ("id")
) Without Oids;


Create table "receptionist"
(
	"party_role_id" Bigint NOT NULL,
	"id" BigSerial NOT NULL,
 primary key ("id")
) Without Oids;


Create table "company"
(
	"id" BigSerial NOT NULL,
	"party_id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "party"
(
	"id" BigSerial NOT NULL,
	"modified_by" Bigint,
	"created_by" Bigint NOT NULL,
	"name" Varchar(256) NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;


Create table "party_role"
(
	"id" BigSerial NOT NULL,
	"modified_by" Bigint,
	"target" Bigint NOT NULL,
	"source" Bigint NOT NULL,
	"created_by" Bigint NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;


Create table "permission_profile"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(256) NOT NULL,
 primary key ("id")
) Without Oids;


/* Create Foreign Keys */

Alter table "assigned_permission_profile" add  foreign key ("user_id") references "user" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("created_by") references "user" ("id") on update restrict on delete restrict;

Alter table "treatment_type" add  foreign key ("created_by") references "user" ("id") on update restrict on delete restrict;

Alter table "treatment_type" add  foreign key ("modified_by") references "user" ("id") on update restrict on delete restrict;

Alter table "party" add  foreign key ("created_by") references "user" ("id") on update restrict on delete restrict;

Alter table "party" add  foreign key ("modified_by") references "user" ("id") on update restrict on delete restrict;

Alter table "party_role" add  foreign key ("created_by") references "user" ("id") on update restrict on delete restrict;

Alter table "party_role" add  foreign key ("modified_by") references "user" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("individual_id") references "individual" ("party_id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("treatment_id") references "treatment_type" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("physician_id") references "physician" ("id") on update restrict on delete restrict;

Alter table "party_role" add  foreign key ("source") references "party" ("id") on update restrict on delete restrict;

Alter table "party_role" add  foreign key ("target") references "party" ("id") on update restrict on delete restrict;

Alter table "company" add  foreign key ("party_id") references "party" ("id") on update restrict on delete restrict;

Alter table "individual" add  foreign key ("party_id") references "party" ("id") on update restrict on delete restrict;

Alter table "patient" add  foreign key ("party_role_id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "physician" add  foreign key ("party_role_id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "nurse" add  foreign key ("party_role_id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "receptionist" add  foreign key ("party_role_id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "user" add  foreign key ("party_role_id") references "party_role" ("id") on update restrict on delete restrict;

Alter table "assigned_permission_profile" add  foreign key ("permission_profile_id") references "permission_profile" ("id") on update restrict on delete restrict;

Alter table "permission" add  foreign key ("permission_profile_id") references "permission_profile" ("id") on update restrict on delete restrict;
