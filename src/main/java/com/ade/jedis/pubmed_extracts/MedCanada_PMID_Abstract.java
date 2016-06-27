package com.ade.jedis.pubmed_extracts;

/**
 * Created by Niranjan on 5/23/2016.
 */

import org.apache.http.Consts;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.http.HTTPException;
import java.beans.PropertyVetoException;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Component
@SuppressWarnings("unchecked")
public class MedCanada_PMID_Abstract {

    Logger logger = LoggerFactory.getLogger(MedCanada_PMID_Abstract.class);
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String CSV_SEPARATOR = ",";
    private static final String COLON_SEPARATOR = ":";

    public void getPMIDAbstract(FileWriter writer, String drugName, String eventName, String PMID) throws URISyntaxException, IOException, PropertyVetoException, SQLException, HTTPException {

        //Fields in the parameters

        //url for the get request
        String Stats_Get_URL = "eutils.ncbi.nlm.nih.gov/entrez/eutils/";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //setting parameters for the get request
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(Stats_Get_URL).setPath("/efetch.fcgi")
                .setParameter("db", "pubmed")
                // .setParameter("time_ranges", time_ranges)
                .setParameter("id", PMID)
                .setParameter("rettype", "fasta")
                .setParameter("retmode", "text");


        BufferedReader reader = null;
        //getting the httpresponse
        CloseableHttpResponse httpResponse;
        //declaring the httpget request
        HttpGet httpGet = new HttpGet(builder.build());

        try {
            httpResponse = httpClient.execute(httpGet);
            reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String inputLine;
            StringBuffer fbresponse = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                fbresponse.append(inputLine);
            }
            String feed = fbresponse.toString();

            //System.out.println(feed);
            drugName = drugName.replaceAll("MG", " ");
            drugName = drugName.replaceAll("ML", " ");
            eventName = eventName.replaceAll("MG", " ");
            eventName = eventName.replaceAll("ML", " ");

            writer.write(drugName);
            writer.append(CSV_SEPARATOR);
            writer.write(eventName);
            writer.append(CSV_SEPARATOR);
            writer.write(PMID);
            writer.append(CSV_SEPARATOR);
            writer.write("(" + feed + ")");

            reader.close();


        } catch (ClientProtocolException e) {
            logger.info("ClientProtocolException ");
            logger.info(String.valueOf(e));
            e.printStackTrace();
        } catch (IOException e) {
            logger.info(" HTTP Response IO Exception");
            logger.info(String.valueOf(e));
            e.printStackTrace();
        } catch (NullPointerException e) {
            logger.info("HTTP Response NullPointerException");
            logger.info(String.valueOf(e));
            e.printStackTrace();
        }
        httpClient.close();

    }

    public void getIDOnDrugAndEvents(String drugName, String eventName, FileWriter writer) throws URISyntaxException, IOException {
        String Stats_Get_URL = "eutils.ncbi.nlm.nih.gov/entrez/eutils/";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String idList = "";
        drugName = drugName.toLowerCase();
        eventName = eventName.toLowerCase();
        String drugNameInput = drugName.replaceAll(" ", "+");
        String eventNameInput = eventName.replaceAll(" ", "+");
        String inputParam = drugNameInput + " " + "AND" + " " + eventNameInput;
        System.out.println(inputParam);

        //setting parameters for the get request
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(Stats_Get_URL).setPath("esearch.fcgi").setCharset(Consts.UTF_8)
                .setParameter("db", "pubmed")
                .setParameter("term", inputParam);

        BufferedReader reader = null;
        boolean status = false;
        //getting the httpresponse
        CloseableHttpResponse httpResponse;
        //declaring the httpget request
        HttpGet httpGet = new HttpGet(builder.build());
        try {
            httpResponse = httpClient.execute(httpGet);



            reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String inputLine;
            StringBuffer responseString = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                responseString.append(inputLine);
            }
            String feed = responseString.toString();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();

            Document document = docBuilder.parse(new InputSource(new StringReader(feed)));

            NodeList nL = document.getElementsByTagName("Id");
            for (int i = 0; i < nL.getLength(); i++) {
                Node node = nL.item(i);
                idList += node.getTextContent() + ";";
            }
            //System.out.println(idList);
            writer.write(drugName);
            writer.append(CSV_SEPARATOR);
            writer.write(eventName);
            writer.append(CSV_SEPARATOR);
            writer.write(idList);

            //reader.close();


        } catch (ClientProtocolException e) {
            logger.info("ClientProtocolException ");
            logger.info(String.valueOf(e));
            e.printStackTrace();
        } catch (IOException e) {
            logger.info(" HTTP Response IO Exception");
            logger.info(String.valueOf(e));
            e.printStackTrace();
        } catch (NullPointerException e) {
            logger.info("HTTP Response NullPointerException");
            logger.info(String.valueOf(e));
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        httpClient.close();

    }

    public boolean getPMID(String inputFileaAddress, String outputFileAddress, String abstractFileAddress) throws IOException, PropertyVetoException, SQLException, URISyntaxException {

        try {
            //this snippet is for getting the ID's

            File inputfilesList = new File(inputFileaAddress);

            File[] inputfiles = inputfilesList.listFiles();

            File inputFile = inputfiles[0];
            String OutputCsvFileAddress = outputFileAddress;
            String OutputAbstractFileAddress = abstractFileAddress;
           // File OutputCsvFile = new File(outputFileAddress + "/medCanadaPubmedPMIDFile.csv");
            File OutputCsvFile = new File(OutputCsvFileAddress + "medCanadaPubmedPMIDFile.csv");
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            FileWriter writer = new FileWriter(OutputCsvFile);
            String line;
            while ((line = br.readLine()) != null) {
                String[] word = line.split(COLON_SEPARATOR);
                //pmid_abstract.getIDOnDrugAndEvents("cilazapril", "angina", writer);
                getIDOnDrugAndEvents(word[0], word[1], writer);
                writer.append(NEW_LINE_SEPARATOR);
            }
            writer.close();

            File textFile = new File(OutputAbstractFileAddress + "medCanadaPubmedAbstractFile.csv");
            FileWriter textWriter = new FileWriter(textFile);
            BufferedReader abs_br = new BufferedReader(new FileReader(OutputCsvFile));
            String textLine;
            //skipping header
            String headerLine = abs_br.readLine();
            while ((textLine = abs_br.readLine()) != null) {

                String[] PMID_Pairs = textLine.split(CSV_SEPARATOR);
                if (PMID_Pairs != null && PMID_Pairs.length > 2) {
                    //System.out.println(PMID_Pairs[2]);

                    if (PMID_Pairs[2].indexOf(";") > 0) {
                        String[] PMIDS = PMID_Pairs[2].split(";");
                        if (PMIDS != null) {
                            for (String id : PMIDS) {
                                getPMIDAbstract(textWriter, PMID_Pairs[0], PMID_Pairs[1], id);
                                textWriter.append(NEW_LINE_SEPARATOR);
                            }
                        }
                    } else {
                        getPMIDAbstract(textWriter, PMID_Pairs[0], PMID_Pairs[1], PMID_Pairs[2]);
                        textWriter.append(NEW_LINE_SEPARATOR);
                    }
                }
            }

            textWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
