# STARAI_ADE_API_JAVA-REDIS_CLIENT
API for JAVA-REDIS client database holding Drug and Adverse Event Pair Information
----------------------------------------------------------------------------------------

o Execution steps: 

1) Clone the repo

2) To Create the jar file 
  a) mvn clean 
  b) mvn generate-resources
  c) mvn install 
  d) mvn package
  
3) Execute the jar file on the target folder with the arguments given below



Guide for the input arguments:

There are four arguments: 

First Argument: Options
Second Argument: Database
Third Argument: Input FIle Address
Fourth Argument: Result File Address


        /*
        Options:
        -------------------------------------------------------------------------------
        Option 1: Initial Load of Drug Event Data Pairs to the database

        Example: key -> "Drug Name" value -> ["Adverse Event 1","Adverse Event 2"]

        Option 2: Initial Load of Drug Indication and Corresponding Drugs Admiminstered Pairs

        Example: key -> "Drug Indication" value -> ["Drug Name 1","Drug Name 2"]

        Option 3: Initial Load of Drug Indication and Drug/Event Pairs Database load

         Example: key -> "Drug Indication" value -> ["Drug Name","HYDROCODONE"]
                                                    ["Adverse Event 1", "something"]
                                                    ["Adverse Event 2","something"]


        Note: In case if this is the first time execution it is mandatory to give option as "1". Database always needs to flushed first time to eliminate other data.

         Database:
         ----------------------------------------------------------------------------------
         Redis Stores database as indexes. So , "1" means use the database at index "1" on the redis server.
         Careful as this may cause different results with different options.


         Input File Address:
         -----------------------------------------------------------------------------------
         Iput file directory from where the data needs to be loaded to the redis database

         Result File Address:
         -----------------------------------------------------------------------------------
         Where you want the results of query to be stored

         */
