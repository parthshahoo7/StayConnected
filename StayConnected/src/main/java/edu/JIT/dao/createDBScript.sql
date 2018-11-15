create SCHEMA stayconnected;

create table stayconnected.userAccount(
  RID varchar(50) primary key not null,
  Fname varchar(40) not null,
  Lname varchar(40),
  email varchar(60) not null,
  address varchar(200),
  phone_number varchar(25)
);

create table stayconnected.jobHistory(
  position varchar(40),
  companyName varchar(80),
  address varchar(200),
  startDate date,
  endDate date,
  currentlyEmplyed boolean,
  RID varchar(50) references stayconnected.userAccount(RID) ON DELETE CASCADE
);

create table stayconnected.skills(
  skillID int primary key not null,
  skillName varchar(100)
);

create table stayconnected.jobOpening(
  jobID int primary key not null,
  position varchar(50) not null,
  location varchar(200),
  startDate date,
  payRate varchar(150),
  jobDescription varchar(200) not null,
  hoursperWeek int,
  endDate date,
  companyName varchar(80) references stayconnected.company(name)
);

create table stayconnected.jobQualification(
  proficiancy varchar(40),
  jobID int references stayconnected.jobOpening(jobID),
  skillID int references stayconnected.skills(skillID)
);

create table stayconnected.company(
  name varchar(80) primary key not null,
  address varchar(200),
  phone_number varchar(25)
);

CREATE TABLE stayconnected.AccountStatus(
  status boolean, RID varchar(30),
  FOREIGN KEY(RID) REFERENCES stayconnected.userAccount(RID) ON DELETE CASCADE
);

CREATE TABLE stayconnected.UserActivation(
  code varchar(60), expiration date,
  RID varchar(30), FOREIGN KEY(RID) REFERENCES stayconnected.userAccount(RID)
                    ON DELETE CASCADE
);

CREATE TABLE stayconnected.UserLogin(
  RID varchar(30) , password varchar(200),
  FOREIGN KEY(RID) REFERENCES stayconnected.userAccount(RID) ON DELETE CASCADE
);

CREATE TABLE stayconnected.Authority(
  RID varchar(30) , UserRoleID int,
   FOREIGN KEY(RID) REFERENCES stayconnected.userAccount(RID) ON DELETE CASCADE,
   FOREIGN KEY(UserRoleID) REFERENCES stayconnected.UserRoles(UID)
);

CREATE TABLE stayconnected.UserRoles(
  UID int PRIMARY KEY, role varchar(30)

);

CREATE TABLE stayconnected.UserSkills(
  RID varchar(30) ,skillID int, proficiency varchar(15),
  FOREIGN KEY(RID) REFERENCES stayconnected.userAccount(RID) ON DELETE CASCADE,
  FOREIGN KEY(skillID) REFERENCES stayconnected.Skills(skillID)
);
