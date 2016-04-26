package com.ade.jedis.views;

/**
 * Created by Niranjan on 4/18/2016.
 */

import com.ade.jedis.api.LoadingInitialDataDrugEventPairOverallDatabase;
import com.ade.jedis.api.LoadingOffsidesInitialDataDrugEventPairNewDatabase;
import com.ade.jedis.api.LoadingInitialDataDrugIndicationDrugPairOverallDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;


@Service
@SuppressWarnings("unchecked")
public class APIDelegator {

        /*
        objects for all the stats created here statically
         */
        Logger logger = LoggerFactory.getLogger(APIDelegator.class);

    @Autowired
    LoadingInitialDataDrugEventPairOverallDatabase loadingInitialDataDrugEventPairOverallDatabase;
    @Autowired
    LoadingInitialDataDrugIndicationDrugPairOverallDatabase loadingInitialDataDrugIndicationDrugPairOverallDatabase;
    @Autowired
    LoadingOffsidesInitialDataDrugEventPairNewDatabase loadingOffsidesInitialDataDrugEventPairNewDatabase;

    public void loadingInitialDataDrugEventPairStats(int databaseIndex,String fileAddress,String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = loadingInitialDataDrugEventPairOverallDatabase.loading(databaseIndex,fileAddress,resultFileAddress);

        if(finishedLoading){
            System.out.println("FDA Drug Event Pair Loading Completed Successfully");
        }

    }

    public void loadingInitialDataDrugIndicationEventPairStats(int databaseIndex,String fileAddress,String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = loadingInitialDataDrugIndicationDrugPairOverallDatabase.loading(databaseIndex,fileAddress,resultFileAddress);

        if(finishedLoading){
            System.out.println(" FDA Drug Indication Drug Name Pair Loading Completed Successfully");
        }

    }

    public void loadingOffsidesInitialDataDrugEventPairNewDatabaseStats(int databaseIndex,String fileAddress,String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = loadingOffsidesInitialDataDrugEventPairNewDatabase.loading(databaseIndex,fileAddress,resultFileAddress);

        if(finishedLoading){
            System.out.println(" FDA Drug Indication Drug Name Pair Loading Completed Successfully");
        }

    }

}
