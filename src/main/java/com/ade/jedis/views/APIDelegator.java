package com.ade.jedis.views;

/**
 * Created by Niranjan on 4/18/2016.
 */

import com.ade.jedis.api.LoadingInitialDataDrugEventPair;
import com.ade.jedis.api.LoadingInitialDataDrugIndicationEventPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
@SuppressWarnings("unchecked")
public class APIDelegator {

        /*
        objects for all the stats created here statically
         */
        Logger logger = LoggerFactory.getLogger(APIDelegator.class);

    @Autowired
    LoadingInitialDataDrugEventPair loadingInitialDataDrugEventPair;
    @Autowired
    LoadingInitialDataDrugIndicationEventPair loadingInitialDataDrugIndicationEventPair;

    public void loadingInitialDataDrugEventPairStats(int databaseIndex,String fileAddress,String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = loadingInitialDataDrugEventPair.loading(databaseIndex,fileAddress,resultFileAddress);

        if(finishedLoading){
            System.out.println("Drug Event Pair Loading Completed Successfully");
        }

    }

    public void loadingInitialDataDrugIndicationEventPairStats(int databaseIndex,String fileAddress,String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = loadingInitialDataDrugIndicationEventPair.loading(databaseIndex,fileAddress,resultFileAddress);

        if(finishedLoading){
            System.out.println("Drug Indication Drug Name Pair Loading Completed Successfully");
        }

    }

}
