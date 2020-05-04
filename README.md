- Quick Note
This is an unfinished project but nonetheless an interesting one that I embarked on when I first learned to program with Java.
Feel free to clone, fork or copy this, but do bear in mind that it needs a database connection code. The joys of using an IDE like Netbeans meant I didn't have to write connection code at the time to get this working to use the JPA. But you'll need one for portability. If you're still learning and you need help with or are stuck writing the connection code, feel free to send me a message and I'll try and help.
Happy coding!

# Business Manager App (unfinished...)
![image](https://user-images.githubusercontent.com/31381732/57479397-3831eb80-7295-11e9-94ad-1f6f613bcc9c.png)
This is a database backed standalone Java application that can be used to create invoices, record expenses and show self-employed annual tax estimates based on income.

## Technologies Used
1. Java 8/JavaFX
2. PDFBox for generating PDF invoice
3. JavaDB (formerly Derby)
4. JPA for interacting with the database
5. A Java Tax Calculation API, which can be found in this [repo](https://github.com/Codeama/UK_Self-Employed_Tax_Calculation_API)
6. SQL script for the database can be found [here](https://github.com/Codeama/BusinessManager/blob/master/business_manager.sql)

## Screen shots
### Invoicing
![image](https://user-images.githubusercontent.com/31381732/57479902-682dbe80-7296-11e9-928b-fa304d261634.png)

#### Manage Invoices
![image](https://user-images.githubusercontent.com/31381732/57485902-ccf01580-72a4-11e9-9ab6-e7a37f4f562b.png)

### Expenses
![image](https://user-images.githubusercontent.com/31381732/57480021-9b704d80-7296-11e9-9f62-8e2e9caf610d.png)
