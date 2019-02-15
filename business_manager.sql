create table EXPENSES
(
	ID INTEGER not null,
	DATE DATE not null,
	DESCRIPTION CHAR(100) not null,
	CATEGORY CHAR(50),
	RECEIPT VARCHAR(50),
	TOTAL DECIMAL(10,2) not null,
	RUNNING_TOTAL DECIMAL(10,2) not null,
            PRIMARY KEY (ID)
)

create table INVOICES
(
	DATE DATE not null,
	INVOICE_NO VARCHAR(25) not null,
	CUSTOMER_ID INT not null,
	FILE_PATH VARCHAR(260) not null,
	STATUS CHAR(10) not null,
	TOTAL DECIMAL(10,2) not null,
	RUNNING_TOTAL DECIMAL(10,2) not null,
	FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMERS (CUSTOMER_ID),
        PRIMARY KEY (INVOICE_NO)
)

create table CUSTOMERS
(
	Customer_ID INT not null GENERATED ALWAYS AS IDENTITY,
	Customer_Name VARCHAR(50) not null,
	Address_Line1 VARCHAR(30),
	Address_Line2 VARCHAR(25),
	Post_Code VARCHAR(15),
	City VARCHAR(30),
	County VARCHAR(25),
        Email_Address VARCHAR(60) not null,
		PRIMARY KEY(Customer_ID)
)



create table TAX
(
	WEEK_ENDING DATE not null,
	INCOME DECIMAL(10,2) not null,
	EXPENSES DECIMAL(10,2) not null,
	PROFIT DECIMAL(10,2) not null,
	NI2 DECIMAL(10,2) not null,
	NI4 DECIMAL(10,2) not null,
	INCOME_TAX DECIMAL(10,2) not null,
	TOTAL DECIMAL(10,2) not null,
	RUNNING_TOTAL DECIMAL(10,2) not null,
            PRIMARY KEY (WEEK_ENDING)
)

create table INVOICE_ITEMS
(
        ITEM_NO INT not null GENERATED ALWAYS AS IDENTITY,
	INVOICE_NO VARCHAR(25) not null,
	Customer_ID INT not null,
	DESCRIPTION VARCHAR (50),
	QUANTITY DECIMAL(5),
	PRICE DECIMAL(10,2) not null,
	AMOUNT DECIMAL(10,2) not null,
		FOREIGN KEY (INVOICE_NO) REFERENCES INVOICES (INVOICE_NO),
		FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMERS (CUSTOMER_ID),
                PRIMARY KEY (ITEM_NO)
)