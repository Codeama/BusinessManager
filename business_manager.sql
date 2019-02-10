DROP TABLE EXPENSES;
DROP TABLE INVOICES;
DROP TABLE TAX;

create table EXPENSES
(
	ID INTEGER not null primary key,
	DATE DATE not null,
	DESCRIPTION CHAR(100) not null,
	CATEGORY CHAR(50),
	RECEIPT VARCHAR(50),
	TOTAL DECIMAL(10) not null,
	RUNNINGTOTAL DECIMAL(10) not null
)

create table INVOICES
(
	DATE DATE not null,
	INVOICE_NO VARCHAR(25) not null primary key,
	FILEPATH VARCHAR(260) not null,
	STATUS CHAR(10) not null,
	TOTAL DECIMAL(10) not null,
	RUNNINGTOTAL DECIMAL(10) not null
)

create table TAX
(
	WEEK_ENDING DATE not null primary key,
	INCOME DECIMAL(10) not null,
	EXPENSES DECIMAL(10) not null,
	EARNINGS DECIMAL(10) not null,
	NI2 DECIMAL(10) not null,
	NI4 DECIMAL(10) not null,
	INCOME_TAX DECIMAL(10) not null,
	TOTAL DECIMAL(10) not null,
	RUNNINGTOTAL DECIMAL(10) not null
)