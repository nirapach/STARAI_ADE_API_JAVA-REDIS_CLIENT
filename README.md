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



Important References:
----------------------

What is TwoSides Database:
---------------------------
"The Twosides databases is a resource of polypharmacy side effects for pairs of drugs. This database contains 868,221 significant associations between 59,220 pairs of drugs and 1301 adverse events. These associations are limited to only those that cannot be clearly attributed to either drug alone (that is, those associations covered in OFFSIDES). The database contains an additional 3,782,910 significant associations for which the drug pair has a higher side-effect association score, determined using the proportional reporting ratio (PRR), than those of the individual drugs alone."

What is Offsides Database:
----------------------------

"The Offsides database is a resource of 438,801 off-label -- those effects not listed on the FDA's official drug label -- side effects for 1332 drugs and 10,097 adverse events. The average drug label lists 69 "on-label" adverse events. We list an average of 329 high-confidence off-label adverse events for each drug. For comparison, the SIDER database, extracted from drug package inserts, lists 48,577 drug-event associations for 620 drugs and 1092 adverse events that are also covered by the data mining. OFFSIDES recovers 38.8% (18,842 drug-event associations) of SIDER associations from the adverse event reports. Thus, OFFSIDES finds different associations from those reported during clinical trials before drug approval."

Reference:
----------
Data-Driven Prediction of Drug Effects and Interactions
Nicholas P. Tatonetti, Patrick P. Ye, Roxana Daneshjou, and Russ B. Altman
Sci Transl Med 14 March 2012 4:125ra31. [DOI:10.1126/scitranslmed.3003377]




Guide for the input arguments:
----------------------------
There are four arguments: 

[First Argument: Options]
[Second Argument: Database]
[Third Argument: Input FIle Address]
[Fourth Argument: Result File Address]


     /*
        Options:
        -------------------------------------------------------------------------------
        Option 1: Initial Load of Drug Event Data Pairs to the database

        Example: key -> "Drug Name" value -> ["Adverse Event 1","Adverse Event 2"]

        Option 2: Initial Load of Drug Indication and Corresponding Drugs Admiminstered Pairs

        Example: key -> "Drug Indication" value -> ["Drug Name 1","Drug Name 2"]

        Option 3: Initial retrieval of Drug/Event Pairs Database from FDA database

         Example: input: key -> "DrugName" value -> output: json resposnse: {"DrugName":["Adverse Event 1","Adverse Event 2"]}

        Option 4: Initial loading of Offsides database into database index "2"

         Example: key -> "Drug Name" value -> ["Adverse Event 1","Adverse Event 2"]

        Option 5: Initial loading of Twosides database into database index "2"

         Example: key -> "Drug Name + Drug Name" value -> ["Adverse Event 1","Adverse Event 2"]

        Note: In case if this is the first time execution it is mandatory to give option as "1". Database always needs to flushed first time to eliminate other data. On option 4 also database is flushed, but the database used for "Clinical Trials stage Adverse Events is '2'". So, dtaabse index '2' clinical trials data. So, while loading and retriving not FDA approved adverse events use databse index as '2'.

         Database:
         ----------------------------------------------------------------------------------
         Redis Stores database as indexes. So , "1" means use the database at index "1" on the redis server.
         Careful as this may cause different results with different options.


         Input File Address:
         -----------------------------------------------------------------------------------
         Input file directory from where the data needs to be loaded to the redis database

         Result File Address:
         -----------------------------------------------------------------------------------
         Where you want the results of query to be stored


         */
