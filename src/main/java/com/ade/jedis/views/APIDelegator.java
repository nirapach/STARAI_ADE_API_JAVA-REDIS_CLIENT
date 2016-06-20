package com.ade.jedis.views;

/**
 * Created by Niranjan on 4/18/2016.
 */

import com.ade.jedis.api.*;
import com.ade.jedis.pubmed_extracts.FDA_PMID_Abstract;
import com.ade.jedis.pubmed_extracts.MedCanada_PMID_Abstract;
import com.ade.jedis.pubmed_extracts.OffSides_PMID_Abstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyVetoException;
import java.io.File;
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

   /* @Autowired
    FDA_PMID_Abstract pmid_abstract;*/
    @Autowired
    FDALoadingInitialDataDrugEventPairOverallDatabase FDALoadingInitialDataDrugEventPairOverallDatabase;
    @Autowired
    FDALoadingInitialDataDrugIndicationDrugPairOverallDatabase FDALoadingInitialDataDrugIndicationDrugPairOverallDatabase;
    @Autowired
    RetrieveData retrieveData;
    @Autowired
    OffsidesLoadingInitialDataDrugEventPairNewDatabase offsidesLoadingInitialDataDrugEventPairNewDatabase;
    @Autowired
    TwosidesLoadingInitialDataDrugEventPairNewDatabase twosidesLoadingInitialDataDrugEventPairNewDatabase;
    @Autowired
    MedCanadaLoadingInitialDataDrugEventPairOverallDatabase medCanadaLoadingInitialDataDrugEventPairOverallDatabase;


    public void getPMIDAbstracts(String fileAddress, String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        //System.out.println(fileAddress+"-----------"+resultFileAddress);
        FDA_PMID_Abstract FDAPmid_abstract = new FDA_PMID_Abstract();

        boolean finishedLoading = FDAPmid_abstract.getPMID(fileAddress,resultFileAddress);

        if (finishedLoading) {
            System.out.println("Pubmed Abstracts succesffuly extracted");
        }

    }

    public void getMedCanadaAbstracts(String database,String fileAddress, String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        //System.out.println(fileAddress+"-----------"+resultFileAddress);
        MedCanada_PMID_Abstract medCanada_pmid_abstract = new MedCanada_PMID_Abstract();

        boolean finishedLoading = medCanada_pmid_abstract.getPMID(database,fileAddress,resultFileAddress);

        if (finishedLoading) {
            System.out.println("MedCanada Pubmed Abstracts succesffuly extracted");
        }

    }

    public void getOffsidesAbstracts(String database,String fileAddress, String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        //System.out.println(fileAddress+"-----------"+resultFileAddress);
        OffSides_PMID_Abstract offSides_pmid_abstract = new OffSides_PMID_Abstract();

        boolean finishedLoading = offSides_pmid_abstract.getPMID(database,fileAddress,resultFileAddress);

        if (finishedLoading) {
            System.out.println("Offsides Pubmed Abstracts succesffuly extracted");
        }

    }



    public void loadingInitialDataDrugEventPairStats(int databaseIndex, String fileAddress, String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = FDALoadingInitialDataDrugEventPairOverallDatabase.loading(databaseIndex, fileAddress, resultFileAddress);

        if (finishedLoading) {
            System.out.println("FDA Drug Event Pair Loading Completed Successfully");
        }

    }

    public void loadingInitialDataDrugIndicationEventPairStats(int databaseIndex, String fileAddress, String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = FDALoadingInitialDataDrugIndicationDrugPairOverallDatabase.loading(databaseIndex, fileAddress, resultFileAddress);

        if (finishedLoading) {
            System.out.println(" FDA Drug Indication Drug Name Pair Loading Completed Successfully");
        }

    }

    public void loadingOffsidesInitialDataDrugEventPairNewDatabaseStats(int databaseIndex, String fileAddress, String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = offsidesLoadingInitialDataDrugEventPairNewDatabase.loading(databaseIndex, fileAddress, resultFileAddress);

        if (finishedLoading) {
            System.out.println(" Offsides Drug Name Pair Loading Completed Successfully");
        }

    }

    public void loadingTwosidesInitialDataDrugEventPairNewDatabaseStats(int databaseIndex, String fileAddress, String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = twosidesLoadingInitialDataDrugEventPairNewDatabase.loading(databaseIndex, fileAddress, resultFileAddress);

        if (finishedLoading) {
            System.out.println(" Twosides Drug Name Pair and Event store Loading Completed Successfully");
        }

    }

    public void loadingMedCanadaInitialDataDrugEventPairNewDatabaseStats(int databaseIndex, String fileAddress, String resultFileAddress) throws IOException, URISyntaxException, PropertyVetoException, SQLException, InterruptedException {

        boolean finishedLoading = medCanadaLoadingInitialDataDrugEventPairOverallDatabase.loading(databaseIndex, fileAddress, resultFileAddress);

        if (finishedLoading) {
            System.out.println(" MedCanada Drug Name Pair and Event store Loading Completed Successfully");
        }

    }

    public void retrieveData(String input, String result_files_address) {
        File dataFile = new File(input);
        boolean finishedLoading = false;
        if (!dataFile.isDirectory() && !dataFile.isHidden() && dataFile.canRead() && dataFile.exists())
            finishedLoading = retrieveData.retrieveOnDruginFile(dataFile, result_files_address);
        else
            finishedLoading = retrieveData.retrieveOnDrugName(input);

        if (finishedLoading) {
            System.out.println("Drug Retrieval Completed Successfully");
        } else {
            System.out.println("Failure. Please check your input");
        }
    }


}
