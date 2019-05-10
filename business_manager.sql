create table EXPENSES
(
	DATE DATE not null,
	DESCRIPTION CHAR(100) not null,
	CATEGORY CHAR(50),
	RECEIPT VARCHAR(50),
	TOTAL DECIMAL(10) not null,
	RUNNING_TOTAL DECIMAL(10) not null,
	ACCOUNT VARCHAR(25),
	ID INTEGER default AUTOINCREMENT: start 1 increment 1 not null primary key
)

create table INVOICES
(
	DATE DATE not null,
	CUSTOMER_ID INTEGER not null,
	FILE_PATH VARCHAR(260) not null,
	STATUS CHAR(10) not null,
	TOTAL DECIMAL(10) not null,
	RUNNING_TOTAL DECIMAL(10) not null,
	INVOICE_NO VARCHAR(25) unique not null,
	ID INTEGER default AUTOINCREMENT: start 1 increment 1 not null primary key,
	FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMERS (CUSTOMER_ID)
)

create table CUSTOMERS
(
	CUSTOMER_ID INTEGER default AUTOINCREMENT: start 1 increment 1 not null primary key,
	CUSTOMER_NAME VARCHAR(50) not null,
	ADDRESS_LINE1 VARCHAR(30),
	ADDRESS_LINE2 VARCHAR(25),
	POST_CODE VARCHAR(15),
	CITY VARCHAR(30),
	PHONE_NUMBER SMALLINT,
	EMAIL_ADDRESS VARCHAR(60) unique not null
)



create table TAX
(
	WEEK_ENDING DATE not null primary key,
	INCOME DECIMAL(10) not null,
	EXPENSES DECIMAL(10) not null,
	PROFIT DECIMAL(10) not null,
	NI2 DECIMAL(10) not null,
	NI4 DECIMAL(10) not null,
	INCOME_TAX DECIMAL(10) not null,
	TOTAL DECIMAL(10) not null,
	RUNNING_TOTAL DECIMAL(10) not null
)

create table INVOICE_ITEMS
(
    ITEM_NO INTEGER default AUTOINCREMENT: start 1 increment 1 not null primary key,
	INVOICE_NO VARCHAR(25) not null,
	CUSTOMER_ID INTEGER not null,
	DESCRIPTION VARCHAR(50),
	PRICE DECIMAL(10) not null,
	AMOUNT DECIMAL(10) not null,
	QUANTITY DECIMAL(5),
	FOREIGN KEY (INVOICE_NO) REFERENCES INVOICES (INVOICE_NO)
)
