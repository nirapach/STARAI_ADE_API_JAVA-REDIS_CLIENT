package com.ade.jedis.pubmed_extracts;

/**
 * Created by Niranjan on 5/23/2016.
 */

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.ws.http.HTTPException;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Service
@SuppressWarnings("unchecked")
public class PMID_Abstract {

    Logger logger = LoggerFactory.getLogger(PMID_Abstract.class);

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

    public static void main(String args[]) throws IOException, PropertyVetoException, SQLException, URISyntaxException {

        PMID_Abstract pmid_abstract=new PMID_Abstract();


        pmid_abstract.getPMID(9524014," ");
    }
}
