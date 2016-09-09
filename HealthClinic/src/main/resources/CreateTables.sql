set schema 'dbo';

/* Create Tables */

Create table "attachment"
(
	"id" BigSerial NOT NULL,
	"treatment" Bigint NOT NULL,
	"name" Varchar(1024) NOT NULL,
	"path" Varchar(2048) NOT NULL,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
	"createdby" Bigint NOT NULL,
	"modifiedby" Bigint,
 primary key ("id")
) Without Oids;

Create table "executoraccount"
(
	"id" BigSerial NOT NULL,
	"executor" Bigint NOT NULL,
	"from" Timestamp NOT NULL,
	"to" Timestamp NOT NULL,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
 primary key ("id")
) Without Oids;

Create table "accountitem"
(
	"id" BigSerial NOT NULL,
	"paymentchannel" Bigint NOT NULL,
	"executoraccount" Bigint NOT NULL,
	"from" Timestamp NOT NULL,
	"to" Timestamp NOT NULL,
	"treatmenttypename" Varchar(512) NOT NULL,
	"treatmentgroupname" Varchar(512) NOT NULL,
	"executorprovisionpercentage" Double precision NOT NULL,
	"subjectfirstname" Varchar(256) NOT NULL,
	"subjectlastname" Varchar(256) NOT NULL,
	"treatmentprice" Bigint NOT NULL,
	"withdph" Boolean NOT NULL,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
 primary key ("id")
) Without Oids;

Create table "communicationchannel"
(
	"id" BigSerial NOT NULL,
	"validfrom" Timestamp NOT NULL,
	"validto" Timestamp,
	"value" Varchar(512) NOT NULL,
	"discriminator" Integer NOT NULL,
    "createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
	"createdby" Bigint NOT NULL,
	"modifiedby" Bigint,
	"party" Bigint NOT NULL,
 primary key ("id")
) Without Oids;

Create table "money"
(
	"id" BigSerial NOT NULL,
	"amount" Numeric NOT NULL,
	"currency" Varchar(32) NOT NULL,
	 primary key ("id")
) Without Oids;

Create table "systemuser"
(
	"name" Varchar(64) NOT NULL UNIQUE,
	"password" Varchar(128) NOT NULL,
	"id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;

Create table "waitinglist"
(
	"id" BigSerial NOT NULL,
	"treatmenttype" Bigint NOT NULL,
	"individual" Bigint NOT NULL,
	"from" Timestamp NOT NULL,
	"to" Timestamp NOT NULL,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
	"createdby" Bigint NOT NULL,
	"modifiedby" Bigint,
 primary key ("id")
) Without Oids;

Create table "permission"
(
	"id" BigSerial NOT NULL,
	"permissionprofile" Bigint NOT NULL,
	"type" Varchar(256) NOT NULL UNIQUE,
 primary key ("id")
) Without Oids;

Create table "appointment"
(
	"id" BigSerial NOT NULL,
	"previous" Bigint,
	"next" Bigint,
	"createdby" Bigint NOT NULL,
	"from" Timestamp NOT NULL,
	"to" Timestamp NOT NULL,
	"createdon" Timestamp NOT NULL,
	"individual" Bigint NOT NULL,
 primary key ("id")
) Without Oids;

Create table "appointmentstate"
(
	"id" BigSerial NOT NULL,
	"modifiedby" Bigint,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
	"value" Varchar(128) NOT NULL,
	"modifiedon" Timestamp,
 primary key ("id")
) Without Oids;

Create table "appointment_treatmenttype"
(
	"appointment" Bigint NOT NULL,
	"treatmenttype" Bigint NOT NULL,
 primary key ("appointment","treatmenttype")
) Without Oids;


Create table "individual"
(
	"firstname" Varchar(128) NOT NULL,
	"id" Bigint NOT NULL,
	"birthdate" Timestamp,
 primary key ("id")
) Without Oids;


Create table "assignedpermissionprofile"
(
	"permissionprofile" Bigint NOT NULL,
	"id" Bigint NOT NULL,
 primary key ("permissionprofile","id")
) Without Oids;

Create table "treatment"
(
	"id" BigSerial NOT NULL,
	"appointment" Bigint NOT NULL,
	"treatmenttype" Bigint NOT NULL,
    "payment" Bigint,
	"price" Bigint NOT NULL,
    "from" Timestamp NOT NULL,
	"to" Timestamp NOT NULL,
	"modifiedby" Bigint,
    "modifiedon" Timestamp,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
 primary key ("id")
) Without Oids;

Create table "resource_treatment"
(
	"resource" Bigint NOT NULL,
	"treatment" Bigint NOT NULL,
 primary key ("resource","treatment")
) Without Oids;

Create table "treatmenttype"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(200) NOT NULL,
	"info" Text,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
	"createdby" Bigint NOT NULL,
	"modifiedby" Bigint,
	"price" Bigint NOT NULL,
	"duration" Integer NOT NULL,
	"treatmentgroup" Bigint,
	"defaultprovision" Double precision NOT NULL,
 primary key ("id")
) Without Oids;

Create table "treatmentgroup"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(256) NOT NULL,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
	"createdby" Bigint NOT NULL,
	"modifiedby" Bigint,
 primary key ("id")
) Without Oids;

Create table "executor_treatmenttype"
(
    "id" BigSerial NOT NULL,
	"executor" Bigint NOT NULL,
	"treatmenttype" Bigint NOT NULL,
	"provision" Double precision NOT NULL,
	"createdby" Bigint NOT NULL,
	"modifiedby" Bigint,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
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
	"physiciantype" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "physiciantype"
(
	"id" BigSerial NOT NULL,
 primary key ("id")
) Without Oids;

Create table "nurse"
(
	"id" Bigint NOT NULL,
	"nursetype" Bigint NOT NULL,
 primary key ("id")
) Without Oids;

Create table "nursetype"
(
	"id" BigSerial NOT NULL,
 primary key ("id")
) Without Oids;

Create table "skill"
(
	"id" BigSerial NOT NULL,
	"createdby" Bigint NOT NULL,
	"name" Varchar(256) NOT NULL,
	"createdon" Timestamp NOT NULL Default CURRENT_DATE,
	"modifiedby" Bigint,
	"modifiedon" Timestamp,
 primary key ("id")
) Without Oids;


Create table "skillassignment"
(
	"resourcetypewithskills" Bigint NOT NULL,
	"skill" Bigint NOT NULL,
 primary key ("resourcetypewithskills","skill")
) Without Oids;

Create table "roomtype_treatmenttype"
(
	"treatmenttype" Bigint NOT NULL,
	"roomtype" Bigint NOT NULL,
 primary key ("treatmenttype","roomtype")
) Without Oids;

Create table "devicetype_treatmenttype"
(
	"devicetype" Bigint NOT NULL,
	"treatmenttype" Bigint NOT NULL,
 primary key ("devicetype","treatmenttype")
) Without Oids;

Create table "receptionist"
(
	"id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;


Create table "company"
(
	"id" Bigint NOT NULL,
	"registrationnumber" Varchar(256) NOT NULL,
 primary key ("id")
) Without Oids;


Create table "party"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(256) NOT NULL,
    "modifiedby" Bigint,
	"createdby" Bigint NOT NULL,
    "createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
 primary key ("id")
) Without Oids;


Create table "partyrole"
(
	"id" BigSerial NOT NULL,
	"target" Bigint NOT NULL,
	"source" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL Default CURRENT_DATE,
	"modifiedon" Timestamp,
	"createdby" Bigint NOT NULL,
	"modifiedby" Bigint,
 primary key ("id")
) Without Oids;

Create table "resourcepartyrole"
(
	"id" Bigint NOT NULL,
	"target" Bigint NOT NULL,
	"source" Bigint NOT NULL,
 primary key ("id")
) Without Oids;

Create table "permissionprofile"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(256) NOT NULL,
 primary key ("id")
) Without Oids;

Create table "room"
(
	"roomtype" Bigint NOT NULL,
	"name" Varchar(256) NOT NULL,
	"id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;

Create table "roomtype"
(
	"id" BigSerial NOT NULL,
 primary key ("id")
) Without Oids;

Create table "device"
(
	"name" Varchar(512) NOT NULL,
	"id" Bigint NOT NULL,
	"devicetype" Bigint NOT NULL,
 primary key ("id")
) Without Oids;

Create table "devicetype"
(
	"id" Bigint NOT NULL,
	"name" Varchar(512) NOT NULL,
 primary key ("id")
) Without Oids;

Create table "resourcetype"
(
	"id" BigSerial NOT NULL,
	"treatmenttype" Bigint,
	"modifiedby" Bigint,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
 primary key ("id")
) Without Oids;

Create table "resourcetypewithskills"
(
	"id" Bigint NOT NULL,
 primary key ("id")
) Without Oids;

Create table "resource"
(
	"id" BigSerial NOT NULL,
	"availablefrom" Timestamp,
	"availableto" Timestamp,
	"modifiedby" Bigint,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
 primary key ("id")
) Without Oids;

Create table "appointment_resource"
(
	"resource" Bigint NOT NULL,
	"appointment" Bigint NOT NULL,
 primary key ("resource","appointment")
) Without Oids;

Create table "payment"
(
	"id" BigSerial NOT NULL,
	"appointment" Bigint NOT NULL,
	"paidamount" Bigint NOT NULL,
	"paymentchannel" Bigint NOT NULL,
	"modifiedby" Bigint,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
 primary key ("id")
) Without Oids;

Create table "paymentchannel"
(
	"id" BigSerial NOT NULL,
	"party" Bigint NOT NULL,
	"paymentchanneltype" Varchar(128) NOT NULL,
	"modifiedby" Bigint,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,	
 primary key ("id")
) Without Oids;

Create table "voucher"
(
	"id" BigSerial NOT NULL,
	"patient" Bigint NOT NULL,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
	"price" Bigint NOT NULL,
	"value" Bigint NOT NULL,
	"expirationdate" Timestamp NOT NULL,
 primary key ("id")
) Without Oids;

Create table "creditcard"
(
	"id" Bigint NOT NULL,
	"cardnumber" Varchar(32) NOT NULL,
	"cardexpiry" Timestamp NOT NULL,
 primary key ("id")
) Without Oids;

Create table "banktransfer"
(
	"id" Bigint NOT NULL,
	"accountnumber" Char(8) NOT NULL,
	"sortcode" Char(6) NOT NULL,
 primary key ("id")
) Without Oids;

Create table "worktime"
(
	"id" BigSerial NOT NULL,
	"modifiedby" Bigint,
	"createdby" Bigint NOT NULL,
    "createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
 primary key ("id")
) Without Oids;

Create table "day"
(
	"id" BigSerial NOT NULL,
	"name" Varchar(126) NOT NULL,
	"startworktime" Integer NOT NULL,
	"endworktime" Integer NOT NULL,
	"worktime" Bigint NOT NULL,
	"modifiedby" Bigint,
	"createdby" Bigint NOT NULL,
	"createdon" Timestamp NOT NULL,
	"modifiedon" Timestamp,
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
insert into config (name, value) values ('default_user_name', 'admin');
insert into config (name, value) values ('default_user_password', 'admin');

-- init
DO $$
DECLARE 
  user_id bigint;
  company_id bigint;
  individual_id bigint;
	
BEGIN
  -- inserting admin (default user)
  insert into partyrole (createdon, createdby, source, target) values(CURRENT_DATE, -1, -1, -1)
  RETURNING id INTO user_id;
  insert into systemuser (name, "password", id) 
  values ((select "value" from config where name = 'default_user_name'), (select "value" from config where name = 'default_user_password'), user_id);
  update partyrole set createdby = user_id where id = user_id;
  
  -- inserting owner company
  insert into party (name, createdon, createdby) values ((select "value" from config where name = 'default_company_name'), CURRENT_DATE, user_id)
  RETURNING id INTO company_id;
  insert into company (registrationnumber, id) values ('00000000', company_id);

  -- inserting individual representing admin user
  insert into party (name, createdon, createdby) values ((select "value" from config where name = 'default_user_name'), CURRENT_DATE, user_id)
  RETURNING id INTO individual_id;
  insert into individual (firstname, id) values ((select "value" from config where name = 'default_user_name'), individual_id);  

  -- update target and source for party_role (default user)
  update partyrole set target = company_id, source = individual_id where id = user_id;
  
END $$;

/* Create Foreign Keys */

Alter table "assignedpermissionprofile" add  foreign key ("id") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "treatment" add  foreign key ("appointment") references "appointment" ("id") on update restrict on delete restrict;

Alter table "treatment" add  foreign key ("treatmenttype") references "treatmenttype" ("id") on update restrict on delete restrict;

Alter table "treatment" add  foreign key ("payment") references "payment" ("id") on update restrict on delete restrict;

Alter table "treatmenttype" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "treatmenttype" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "treatmenttype" add  foreign key ("treatmentgroup") references "treatmentgroup" ("id") on update restrict on delete restrict;

Alter table "treatmentgroup" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "treatmentgroup" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "resource" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "resource" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "partyrole" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "partyrole" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "resourcepartyrole" add  foreign key ("id") references "resource" ("id") on update restrict on delete restrict;

Alter table "resourcepartyrole" add  foreign key ("source") references "party" ("id") on update restrict on delete restrict;

Alter table "resourcepartyrole" add  foreign key ("target") references "party" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("individual") references "individual" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("next") references "appointment" ("id") on update restrict on delete restrict;

Alter table "appointment" add  foreign key ("previous") references "appointment" ("id") on update restrict on delete restrict;

Alter table "appointmentstate" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "appointmentstate" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "appointmentstate" add  foreign key ("id") references "appointment" ("id") on update restrict on delete restrict;

Alter table "partyrole" add  foreign key ("source") references "party" ("id") on update restrict on delete restrict;

Alter table "partyrole" add  foreign key ("target") references "party" ("id") on update restrict on delete restrict;

Alter table "individual" add  foreign key ("id") references "party" ("id") on update restrict on delete restrict;

Alter table "company" add  foreign key ("id") references "party" ("id") on update restrict on delete restrict;

Alter table "physician" add  foreign key ("id") references "resourcepartyrole" ("id") on update restrict on delete restrict;

Alter table "physician" add  foreign key ("physiciantype") references "physiciantype" ("id") on update restrict on delete restrict;

Alter table "nurse" add  foreign key ("id") references "resourcepartyrole" ("id") on update restrict on delete restrict;

Alter table "nurse" add  foreign key ("nursetype") references "nursetype" ("id") on update restrict on delete restrict;

Alter table "receptionist" add  foreign key ("id") references "partyrole" ("id") on update restrict on delete restrict;

Alter table "patient" add  foreign key ("id") references "partyrole" ("id") on update restrict on delete restrict;

Alter table "systemuser" add  foreign key ("id") references "partyrole" ("id") on update restrict on delete restrict;

Alter table "assignedpermissionprofile" add  foreign key ("permissionprofile") references "permissionprofile" ("id") on update restrict on delete restrict;

Alter table "permission" add  foreign key ("permissionprofile") references "permissionprofile" ("id") on update restrict on delete restrict;

Alter table "roomtype_treatmenttype" add  foreign key ("roomtype") references "roomtype" ("id") on update restrict on delete restrict;

Alter table "roomtype_treatmenttype" add  foreign key ("treatmenttype") references "treatmenttype" ("id") on update restrict on delete restrict;

Alter table "devicetype_treatmenttype" add  foreign key ("devicetype") references "devicetype" ("id") on update restrict on delete restrict;

Alter table "devicetype_treatmenttype" add  foreign key ("treatmenttype") references "treatmenttype" ("id") on update restrict on delete restrict;

Alter table "skillassignment" add  foreign key ("skill") references "skill" ("id") on update restrict on delete restrict;

Alter table "skillassignment" add  foreign key ("resourcetypewithskills") references "resourcetypewithskills" ("id") on update restrict on delete restrict;

Alter table "room" add  foreign key ("roomtype") references "roomtype" ("id") on update restrict on delete restrict;

Alter table "device" add  foreign key ("devicetype") references "devicetype" ("id") on update restrict on delete restrict;

Alter table "nursetype" add  foreign key ("id") references "resourcetype" ("id") on update restrict on delete restrict;

Alter table "physiciantype" add  foreign key ("id") references "resourcetype" ("id") on update restrict on delete restrict;

Alter table "roomtype" add  foreign key ("id") references "resourcetype" ("id") on update restrict on delete restrict;

Alter table "devicetype" add  foreign key ("id") references "resourcetype" ("id") on update restrict on delete restrict;

Alter table "resourcetype" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "resourcetype" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "resourcetype" add  foreign key ("treatmenttype") references "treatmenttype" ("id") on update restrict on delete restrict;

Alter table "resourcetypewithskills" add  foreign key ("id") references "resourcetype" ("id") on update restrict on delete restrict;

Alter table "resource_treatment" add  foreign key ("resource") references "resource" ("id") on update restrict on delete restrict;

Alter table "resource_treatment" add  foreign key ("treatment") references "treatment" ("id") on update restrict on delete restrict;

Alter table "skill" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "skill" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "appointment_resource" add  foreign key ("appointment") references "appointment" ("id") on update restrict on delete restrict;

Alter table "appointment_resource" add  foreign key ("resource") references "resource" ("id") on update restrict on delete restrict;

Alter table "appointment_treatmenttype" add  foreign key ("treatmenttype") references "treatmenttype" ("id") on update restrict on delete restrict;

Alter table "appointment_treatmenttype" add  foreign key ("appointment") references "appointment" ("id") on update restrict on delete restrict;

Alter table "room" add  foreign key ("id") references "resource" ("id") on update restrict on delete restrict;

Alter table "device" add  foreign key ("id") references "resource" ("id") on update restrict on delete restrict;

Alter table "party" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "party" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "waitinglist" add  foreign key ("individual") references "individual" ("id") on update restrict on delete restrict;

Alter table "waitinglist" add  foreign key ("treatmenttype") references "treatmenttype" ("id") on update restrict on delete restrict;

Alter table "waitinglist" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "waitinglist" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "executor_treatmenttype" add  foreign key ("treatmenttype") references "treatmenttype" ("id") on update restrict on delete restrict;

Alter table "executor_treatmenttype" add  foreign key ("executor") references "resourcepartyrole" ("id") on update restrict on delete restrict;

Alter table "payment" add  foreign key ("appointment") references "appointment" ("id") on update restrict on delete restrict;

Alter table "payment" add  foreign key ("paymentchannel") references "paymentchannel" ("id") on update restrict on delete restrict;

Alter table "payment" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "payment" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "attachment" add  foreign key ("treatment") references "treatment" ("id") on update restrict on delete restrict;

Alter table "attachment" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "attachment" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "communicationchannel" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "communicationchannel" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "communicationchannel" add  foreign key ("party") references "party" ("id") on update restrict on delete restrict;

Alter table "paymentchannel" add  foreign key ("party") references "party" ("id") on update restrict on delete restrict;

Alter table "paymentchannel" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "paymentchannel" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "banktransfer" add  foreign key ("id") references "paymentchannel" ("id") on update restrict on delete restrict;

Alter table "creditcard" add  foreign key ("id") references "paymentchannel" ("id") on update restrict on delete restrict;

Alter table "day" add  foreign key ("worktime") references "worktime" ("id") on update restrict on delete restrict;

Alter table "day" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "day" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "worktime" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "worktime" add  foreign key ("modifiedby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "payment" add  foreign key ("paidamount") references "money" ("id") on update restrict on delete restrict;

Alter table "treatmenttype" add  foreign key ("price") references "money" ("id") on update restrict on delete restrict;

Alter table "treatment" add  foreign key ("price") references "money" ("id") on update restrict on delete restrict;

Alter table "accountitem" add  foreign key ("treatmentprice") references "money" ("id") on update restrict on delete restrict;

Alter table "accountitem" add  foreign key ("paymentchannel") references "paymentchannel" ("id") on update restrict on delete restrict;

Alter table "accountitem" add  foreign key ("executoraccount") references "executoraccount" ("id") on update restrict on delete restrict;

Alter table "accountitem" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "executoraccount" add  foreign key ("createdby") references "systemuser" ("id") on update restrict on delete restrict;

Alter table "executoraccount" add  foreign key ("executor") references "resourcepartyrole" ("id") on update restrict on delete restrict;