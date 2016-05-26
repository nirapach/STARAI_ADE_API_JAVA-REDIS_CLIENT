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
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.http.HTTPException;
import java.beans.PropertyVetoException;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
@SuppressWarnings("unchecked")
public class Pmid_Abstract {

    Logger logger = LoggerFactory.getLogger(Pmid_Abstract.class);
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String TAB_SEPARATOR = ",";

    public void getPMID(long pmid,String csv_address) throws URISyntaxException, IOException, PropertyVetoException, SQLException, HTTPException {

        //Fields in the parameters
        String store_file_name = null;
        String PMID = Long.toString(pmid);

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
        boolean status = false;
        //getting the httpresponse
        CloseableHttpResponse httpResponse;
        //declaring the httpget request
        HttpGet httpGet = new HttpGet(builder.build());

        try {
            httpResponse = httpClient.execute(httpGet);


            System.out.println("GET Response Status: "
                    + httpResponse.getStatusLine().getStatusCode());


            httpResponse = httpClient.execute(httpGet);
            reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String inputLine;
            StringBuffer fbresponse = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                fbresponse.append(inputLine);
            }
            String feed = fbresponse.toString();

            System.out.println(feed);


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
        String inputParam = drugNameInput + " " + "AND"+ " " + eventNameInput;
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


            System.out.println("GET Response Status: "
                    + httpResponse.getStatusLine().getStatusCode());

            System.out.print(httpResponse.getEntity().getContent());
            reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            //System.out.print(reader.);
            String inputLine;
            StringBuffer responseString = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                responseString.append(inputLine);
            }
            String feed = responseString.toString();

            System.out.println(feed);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();

            Document document = docBuilder.parse( new InputSource( new StringReader( feed ) ) );

            NodeList nL = document.getElementsByTagName("Id");
            for (int i = 0; i < nL.getLength(); i++) {
                Node node = nL.item(i);
                System.out.println("\nCurrent Element :" + node.getTextContent());
                idList += node.getTextContent() + "; ";
            }
            writer.write(drugName);
            writer.append(TAB_SEPARATOR);
            writer.write(eventName);
            writer.append(TAB_SEPARATOR);
            writer.write(idList);



            /*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse( new InputSource( new StringReader( feed ) ) );
            System.out.println(doc);*/

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
    public static void main(String args[]) throws IOException, PropertyVetoException, SQLException, URISyntaxException {

        Pmid_Abstract pmid_abstract=new Pmid_Abstract();
        String inputFile = args[0];
        String OutputCsvFile = args[1];
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        FileWriter writer = new FileWriter(OutputCsvFile);
        String line;
        while ((line = br.readLine()) != null) {
            String[] word = line.split(TAB_SEPARATOR);
            //pmid_abstract.getIDOnDrugAndEvents("cilazapril", "angina", writer);
            pmid_abstract.getIDOnDrugAndEvents(word[0], word[1], writer);
            writer.append(NEW_LINE_SEPARATOR);
        }
        writer.close();

        //pmid_abstract.getPMID(9524014," ");
    }
}
