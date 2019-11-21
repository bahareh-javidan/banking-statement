# Banking Statement
This is a sample project in Java 8 to demonstrate banking statements.

## Build the project
It is a J2SE 8 application which has a main method to run the project. Maven is used to 
build the project so to package the jar file simply use the following command.

`mvn clean package`

## Run the project
After a successful build, you can run the project with the following command.

`java -jar target/statement-1.0-SNAPSHOT.jar`

Then you have to enter the following data:

* AccountId
* FromDate (in the format: 20/10/2018 12:47:55)
* ToDate (in the format: 21/10/2018 12:47:55)

The program tries to read the data.csv file in the packed jar file to retrieve transactions.  
Then it shows the result in two lines:

`Relative balance for this period is: -$32.25`

`Number of transactions included is: 2`
