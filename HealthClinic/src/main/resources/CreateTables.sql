Create table dbo."user"
(
	"id" BigSerial NOT NULL,
	"modified_by" Bigint NOT NULL,
	"created_by" Bigint,
	"login" Varchar(64) NOT NULL UNIQUE,
	"password" Varchar(128) NOT NULL,
	"assigned_permission_profile" Bigint,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;

Create table dbo."permission"
(
	"id" BigSerial NOT NULL,
	"permission_profile_id" Bigint NOT NULL,
	"type" Varchar(200) NOT NULL UNIQUE,
 primary key ("id")
) Without Oids;


Create table dbo."appointment"
(
	"id" BigSerial NOT NULL,
	"treatment_id" Bigint NOT NULL,
	"from" Timestamp NOT NULL,
	"to" Timestamp NOT NULL,
	"individual_id" Bigint NOT NULL,
	"created_by" Bigint NOT NULL,
	"created_on" Timestamp NOT NULL,
	"actual" Boolean NOT NULL
) Without Oids;


Create table dbo."individual"
(
	"id" BigSerial NOT NULL,
	"first_name" Char(128) NOT NULL,
	"phone" Char(32) NOT NULL,
	"email" Char(128),
	"party_id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table dbo."assigned_permission_profile"
(
	"user_id" Bigint NOT NULL,
	"permission_profile_id" Bigint NOT NULL,
 primary key ("user_id","permission_profile_id")
) Without Oids;


Create table dbo."treatment"
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


Create table dbo."patient"
(
	"id" BigSerial NOT NULL,
	"modified_by" Bigint,
	"created_by" Bigint NOT NULL,
	"party_role_id" Bigint NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;


Create table dbo."physician"
(
	"party_role_id" Bigint NOT NULL,
	"modified_by" Bigint,
	"created_by" Bigint NOT NULL,
	"id" BigSerial NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;


Create table dbo."nurse"
(
	"party_role_id" Bigint NOT NULL,
	"modified_by" Bigint,
	"created_by" Bigint NOT NULL,
	"id" BigSerial NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;


Create table dbo."receptionist"
(
	"party_role_id" Bigint NOT NULL,
	"modified_by" Bigint,
	"created_by" Bigint NOT NULL,
	"id" BigSerial NOT NULL,
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;


Create table dbo."company"
(
	"id" BigSerial NOT NULL,
	"party_id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table dbo."party"
(
	"id" BigSerial NOT NULL,
	"modified_by" Bigint,
	"created_by" Bigint NOT NULL,
	"name" Varchar(256),
	"created_on" Timestamp NOT NULL,
	"modified_on" Timestamp,
 primary key ("id")
) Without Oids;


Create table dbo."party_role"
(
	"id" BigSerial NOT NULL,
	"target" Bigint NOT NULL,
	"source" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table dbo."permission_profile"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(256) NOT NULL,
 primary key ("id")
) Without Oids;

/* Create Foreign Keys */



Alter table dbo."assigned_permission_profile" add  foreign key ("user_id") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."appointment" add  foreign key ("created_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."treatment" add  foreign key ("created_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."patient" add  foreign key ("created_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."receptionist" add  foreign key ("created_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."nurse" add  foreign key ("created_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."physician" add  foreign key ("created_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."treatment" add  foreign key ("modified_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."user" add  foreign key ("created_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."user" add  foreign key ("modified_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."party" add  foreign key ("created_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."physician" add  foreign key ("modified_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."nurse" add  foreign key ("modified_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."receptionist" add  foreign key ("modified_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."patient" add  foreign key ("modified_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."party" add  foreign key ("modified_by") references dbo."user" ("id") on update restrict on delete restrict;

Alter table dbo."appointment" add  foreign key ("treatment_id") references dbo."individual" ("id") on update restrict on delete restrict;

Alter table dbo."appointment" add  foreign key ("individual_id") references dbo."treatment" ("id") on update restrict on delete restrict;

Alter table dbo."party_role" add  foreign key ("source") references dbo."party" ("id") on update restrict on delete restrict;

Alter table dbo."party_role" add  foreign key ("target") references dbo."party" ("id") on update restrict on delete restrict;

Alter table dbo."company" add  foreign key ("party_id") references dbo."party" ("id") on update restrict on delete restrict;

Alter table dbo."individual" add  foreign key ("party_id") references dbo."party" ("id") on update restrict on delete restrict;

Alter table dbo."patient" add  foreign key ("party_role_id") references dbo."party_role" ("id") on update restrict on delete restrict;

Alter table dbo."physician" add  foreign key ("party_role_id") references dbo."party_role" ("id") on update restrict on delete restrict;

Alter table dbo."nurse" add  foreign key ("party_role_id") references dbo."party_role" ("id") on update restrict on delete restrict;

Alter table dbo."receptionist" add  foreign key ("party_role_id") references dbo."party_role" ("id") on update restrict on delete restrict;

Alter table dbo."user" add  foreign key ("assigned_permission_profile") references dbo."party_role" ("id") on update restrict on delete restrict;

Alter table dbo."assigned_permission_profile" add  foreign key ("permission_profile_id") references dbo."permission_profile" ("id") on update restrict on delete restrict;

Alter table dbo."permission" add  foreign key ("permission_profile_id") references dbo."permission_profile" ("id") on update restrict on delete restrict;

