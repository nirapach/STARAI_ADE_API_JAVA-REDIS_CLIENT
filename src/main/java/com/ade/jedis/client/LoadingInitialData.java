package com.ade.jedis.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.*;
import java.util.*;

/**
 * Created by Niranjan on 4/18/2016.
 */
public class LoadingInitialData {

    //address of your redis server
    private static final String redisHost = "127.0.0.1";
    private static final Integer redisPort = 6379;

    //the jedis connection pool..
    //private static JedisPool pool = null;


    public void behaveAsMapOfSets(String input_file_address,Jedis jedis) throws InterruptedException {

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
                        //Read the CSV file header to skip it

                        String[] inputData = line.split(",");

                        if (drugEventList.containsKey(inputData[0])) {

                            HashSet<String> oldValue = drugEventList.get(inputData[0]);
                            oldValue.add(inputData[2]);
                            drugEventList.put(inputData[0], oldValue);
                        } else {
                            if (!drugEventList.containsKey(inputData[0])) {
                                HashSet<String> newValue = new HashSet<String>();
                                newValue.add(inputData[2]);
                                drugEventList.put(inputData[0],newValue);
                            }
                        }

                    }

                    for(Map.Entry<String, HashSet<String>> entry : drugEventList.entrySet()) {

                        //jedis.hset(entry.getKey(), "Drug_Name", entry.getKey());

                        HashSet<String> updatedSet = entry.getValue();
                        //System.out.println(entry.getKey()+","+updatedSet.size());
                        for(String eventValue:updatedSet){
                            jedis.sadd(entry.getKey(), eventValue);
                        }
                    }

                    System.out.println("Test Result");
                    System.out.println(jedis.smembers("SYNTHROID TAB"));
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

    public static void main(String args[]) throws InterruptedException {


        LoadingInitialData loadingInitialData = new LoadingInitialData();
        // this is the path from which the documents to be queried
        String input_files_address=args[0];

        Jedis jedis = new Jedis(redisHost, redisPort);
        jedis.connect();

        //careful this will erase all previously existing data
        //need to get the index of the database from the user next time
        jedis.select(1);

        jedis.flushDB();

        System.out.println("Connected jedis client");
        try{
            //calling the function
            loadingInitialData.behaveAsMapOfSets(input_files_address,jedis);
        }finally {
            jedis.disconnect();
            System.out.println("\nDisconnected jedis client");
        }
        System.out.println("Database_Loaded");
    }
}
