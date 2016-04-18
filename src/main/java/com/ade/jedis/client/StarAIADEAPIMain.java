package com.ade.jedis.client;

/**
 * Created by Niranjan on 4/18/2016.
 */

import com.ade.jedis.views.APIDelegator;
import org.springframework.stereotype.Component;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Component
@SuppressWarnings("unchecked")
public class StarAIADEAPIMain {

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


    public static void main(String args[]) throws IOException, PropertyVetoException, URISyntaxException, SQLException, InterruptedException {

        String options = args[0];
        String database = args[1];
        String input_file_address = args[2];
        String result_files_address = args[3];

        APIDelegator apiDelegator = new APIDelegator();
        int OperationProvided = Integer.valueOf(options);
        int databaseIndex = Integer.valueOf(database);
        switch (OperationProvided) {

            case 1:
                apiDelegator.loadingInitialDataDrugEventPairStats(databaseIndex, input_file_address, result_files_address);
                break;

            case 2:
                apiDelegator.loadingInitialDataDrugIndicationEventPairStats(databaseIndex, input_file_address, result_files_address);
                break;

            default:
                break;


        }


    }
}
