

insert into stayconnected.userAccount VALUES('R01241335','john','doe','johndoe@mail.com','31 adams drive, oldbridge, NJ-08857 ','7329870091');

insert into stayconnected.userAccount VALUES('R01341335','jack','william','jackwilliam@mail.com','621 lincon drive, philadelphia, PA-09812 ','9088764354');

insert into stayconnected.userAccount VALUES('R01241366','David ','Adler','davidadler@mail.com','78 adom drive, edison, NJ-1234 ','8797864567');

insert into stayconnected.userAccount VALUES('R01241235','oliver','carlin','olivercarlin@mail.com','432 taylor avenue, philadelphia, PA-5467 ','5709871234');

insert into stayconnected.userAccount VALUES('R02221325','george','harrison','georgeharrison@mail.com','345 adam street, pittsuburg, PA-09987 ','9321459876');


insert into stayconnected.jobHistory VALUES('SoftwareDeveloper','Google','123 mack ave ny-1234',' 2014-10-23',' 2016-11-20',false,'R01241335');
insert into stayconnected.jobHistory VALUES('SoftwareDeveloper','MICROSOFT','123 lake drive ny-1909',' 2016-11-23',' 2017-11-20',false,'R01241335');
insert into stayconnected.jobHistory VALUES('SoftwareDeveloper','IBM','123 adam ave ca-0987',' 2014-10-23',null,true,'R01241335');
insert into stayconnected.jobHistory VALUES('SoftwareDeveloper Intern','Samsung','123 mark drive ny-0998',' 2014-10-23',' 2016-11-20',false,'R02221325');
insert into stayconnected.jobHistory VALUES('SoftwareDeveloper','GOldman Sachs','123 adam ave ny-9098',' 2016-11-23',null,true,'R02221325');

insert into stayconnected.skills VALUES(1,'SoftwareDeveloper');
insert into stayconnected.skills VALUES(2,'Python Developer');
insert into stayconnected.skills VALUES(3,'Java Developer');
insert into stayconnected.skills VALUES(4,'Web Developer');
insert into stayconnected.skills VALUES(5,'Software Architecture');

insert into stayconnected.jobOpening VALUES(101,'software Developer','Sanfransisco,CA','2018-11-24',null,'Required SoftwareDeveloper with 3 years of working experience',40,null,'Google');
insert into stayconnected.jobOpening VALUES(102,'software Developer Intern','Manhattan,NY','2018-11-24','2019-02-30','Required SoftwareDeveloper with 3 years of computer science experience',20,null,'Google');
insert into stayconnected.jobOpening VALUES(103,'software Developer','Sandiego,CA','2018-11-25',null,'Required SoftwareDeveloper with 3 years of experience',40,null,'IBM');
insert into stayconnected.jobOpening VALUES(104,'software Developer Intern','Sanfransisco,CA','2018-11-24',null,'Required SoftwareDeveloper with 3 years of computer science experience',40,null,'IBM');
insert into stayconnected.jobOpening VALUES(105,'Software Architecture','Santa Clara,CA','2018-11-27',null,'Required SoftwareDeveloper with 8 years of experience',40,null,'MICROSOFT');

insert into stayconnected.jobQualification VALUES('I',101,1);
insert into stayconnected.jobQualification VALUES('E',102,1);
insert into stayconnected.jobQualification VALUES('I',103,1);
insert into stayconnected.jobQualification VALUES('E',104,1);
insert into stayconnected.jobQualification VALUES('S',105,5);

insert into stayconnected.company VALUES('Google','Sanfransisco,CA',null);
insert into stayconnected.company VALUES('MICROSOFT','Santa Clara,CA',null);
insert into stayconnected.company VALUES('IBM','Sanfransisco,CA',null);
insert into stayconnected.company VALUES('GOldman Sachs','Sanfransisco,CA',null);
insert into stayconnected.company VALUES('Samsung','SanJose,CA',null);

INSERT INTO stayconnected.AccountStatus VALUES (true , 'R01241335');

INSERT INTO stayconnected.AccountStatus VALUES (false , 'R01241235');

INSERT INTO stayconnected.AccountStatus VALUES (true , 'R01341335');

INSERT INTO stayconnected.AccountStatus VALUES (false , 'R01241366');

INSERT INTO stayconnected.AccountStatus VALUES (
  true , 'R02221325'
);


INSERT INTO stayconnected.UserActivation VALUES (
  'JEK29EFB4C83U' , '2019-11-04', 'R01241335'
);

INSERT INTO stayconnected.UserActivation VALUES (
  'JEK2JE0OC83U' , '2019-12-04' , 'R01241235'
);

INSERT INTO stayconnected.UserActivation VALUES (
  'JEK29QOVM383U' , '2019-01-04' , 'R01341335'
);

INSERT INTO stayconnected.UserActivation VALUES (
  'JEK292KFM4ID0' , '2019-02-04' , 'R01241366'
);

INSERT INTO stayconnected.UserActivation VALUES (
  'JEK22KFMDJQ0P' , '2019-03-04' , 'R02221325'
);



INSERT INTO stayconnected.UserLogin VALUES(
  'R01241335' , 'P3jw2#j3KK'
);

INSERT INTO stayconnected.UserLogin VALUES(
  'R01241235' , 'P3j2lQj3KK'
);

INSERT INTO stayconnected.UserLogin VALUES(
  'R01341335' , 'L09w2#j3KK'
);

INSERT INTO stayconnected.UserLogin VALUES(
  'R01241366' , 'P3jw2Qaz5m'
);

INSERT INTO stayconnected.UserLogin VALUES(
  'R02221325' , 'P3jAL4$5PL'
);



INSERT INTO stayconnected.Authority VALUES(
  'R01241335' , 2
);

INSERT INTO stayconnected.Authority VALUES(
  'R01241335' , 3
);
INSERT INTO stayconnected.Authority VALUES(
  'R01241235' , 1
);
INSERT INTO stayconnected.Authority VALUES(
  'R01341335' , 2
);
INSERT INTO stayconnected.Authority VALUES(
  'R01241366' , 3
);
INSERT INTO stayconnected.Authority VALUES(
  'R02221325' , 1
);



INSERT INTO stayconnected.UserRoles VALUES(
  1 , 'ROLE_CURR'
);

INSERT INTO stayconnected.UserRoles VALUES(
  2 , 'ROLE_ALUM'
);

INSERT INTO stayconnected.UserRoles VALUES(
  3 , 'ROLE_FACULTY'
);

INSERT INTO stayconnected.UserSkills VALUES(
  'R01241335' , 1 , 'E'
);

INSERT INTO stayconnected.UserSkills VALUES(
  'R01241335' , 2 , 'I'
);

INSERT INTO stayconnected.UserSkills VALUES(
  'R01241235' , 1 , 'S'
);

INSERT INTO stayconnected.UserSkills VALUES(
  'R01341335' , 3 , 'E'
);
INSERT INTO stayconnected.UserSkills VALUES(
  'R01241366' , 1 , 'S'
);
INSERT INTO stayconnected.UserSkills VALUES(
  'R02221325' , 4 , 'I'
);
