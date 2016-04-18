package com.ade.jedis.api;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by Niranjan on 4/18/2016.
 */
@Component
@SuppressWarnings("unchecked")
public class LoadingInitialDataDrugIndicationEventPair {

    //address of your redis server
    private static final String redisHost = "127.0.0.1";
    private static final Integer redisPort = 6379;

    //the jedis connection pool..
    //private static JedisPool pool = null;

    public static void behaveAsMapOfSets(String input_file_address, Jedis jedis) throws InterruptedException {

        //configure our pool connection
        //pool = new JedisPool(redisHost, redisPort);
        //get a jedis connection jedis connection pool
        //Jedis jedis = pool.getResource();
        File datadirectory = new File(input_file_address);
        System.out.println("\nLoading data in this format [Map<String, List<String>>] = [Map<Drug, List<Event>>]");

        //Jedis jedis = null;
        //csv files to get the wuery terms
        File[] files = datadirectory.listFiles();
        System.out.println(files.length);
        BufferedReader fileReader = null;

        try {

            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory() && !files[i].isHidden() && files[i].canRead() && files[i].exists()) {
                    fileReader = new BufferedReader(new FileReader(files[i]));
                    //skipping the header
                    fileReader.readLine();
                    String line = "";

                    //adding the drug events to a hashmap
                    HashMap<String, HashSet<String>> drugEventList = new HashMap<String, HashSet<String>>();
                    while ((line = fileReader.readLine()) != null) {

                        String[] inputData = line.split(",");

                        if (drugEventList.containsKey(inputData[1])) {

                            HashSet<String> oldValue = drugEventList.get(inputData[1]);
                            oldValue.add(inputData[2]);
                            drugEventList.put(inputData[1], oldValue);
                        } else {
                            if (!drugEventList.containsKey(inputData[1])) {
                                HashSet<String> newValue = new HashSet<String>();
                                newValue.add(inputData[2]);
                                drugEventList.put(inputData[1],newValue);
                            }
                        }

                    }

                    for(Map.Entry<String, HashSet<String>> entry : drugEventList.entrySet()) {

                        HashSet<String> updatedSet = entry.getValue();
                        //System.out.println(entry.getKey()+","+updatedSet.size());
                        for(String eventValue:updatedSet){
                            jedis.sadd(entry.getKey(), eventValue);
                        }
                    }
                }
            }
        } catch (JedisException e) {
            //if something wrong happen, return it back to the pool
           /* if (null != jedis) {
                //pool.returnBrokenResource(jedis);
                jedis = null;
            }*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //jedis.disconnect();
            try {
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/Writer !!!");
                e.printStackTrace();
            }
            ///it's important to return the Jedis instance to the pool once you've finished using it
            /*if (null != jedis)
                pool.returnResource(jedis);*/
        }

    }

    public static boolean loading(int databaseIndex,String fileAddress,String resultFileAddress) throws InterruptedException {


        Jedis jedis = new Jedis(redisHost, redisPort);
        jedis.connect();

        //need to get the index of the database from the user next time
        jedis.select(databaseIndex);
        //please be careful this will erase all previously existing data
        //jedis.flushDB();

        System.out.println("Connected jedis client");
        try{
            //calling the function
           behaveAsMapOfSets(fileAddress,jedis);
        }finally {
            jedis.disconnect();
            System.out.println("\nDisconnected jedis client");
        }
        System.out.println("Database_Loaded");
        return true;
    }
}
