package com.ade.jedis.api;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Niranjan on 4/18/2016.
 */
@Component
@SuppressWarnings("unchecked")
public class OffsidesLoadingInitialDataDrugEventPairNewDatabase {

    //address of your redis server
    private static final String redisHost = "127.0.0.1";
    private static final Integer redisPort = 6379;

    public static String dataCleaning(String inputString) {

        if (inputString != null && inputString != " " && inputString != "") {
            inputString = inputString.replaceAll("MG", " ");
            inputString = inputString.replaceAll("ML", " ");
            inputString = inputString.replaceAll("[^a-zA-Z]+", " ");
            inputString = inputString.replaceAll("[^a-zA-Z]+", " ");
            inputString = inputString.trim();
            inputString = inputString.toLowerCase();
        }
        return inputString;
    }

    public static void behaveAsMapOfSets(String input_file_address, Jedis jedis) throws InterruptedException {

        File datadirectory = new File(input_file_address);
        System.out.println("\nLoading Offsides data in this format [Map<String, List<String>>] = [Map<Drug, List<Event>>]");

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
                        if (line.indexOf(":") != -1) {

                            String[] inputData = line.split(":");

                            if ((inputData.length > 1) && (inputData[0] != null && inputData[0] != " ") && (inputData[1] != null && inputData[1] != " ")) {
                                String eventSubstring = inputData[1].substring(inputData[1].indexOf("["), inputData[1].lastIndexOf("]"));
                                String eventData[] = eventSubstring.split(",");

                                String drugName = inputData[0];
                                drugName = dataCleaning(drugName);

                                String eventPair;
                                for (int e = 0; e < eventData.length; e++) {

                                    eventPair = dataCleaning(eventData[e]);

                                    if (drugEventList.containsKey(drugName) && drugName != null && drugName != "") {

                                        HashSet<String> oldValue = drugEventList.get(drugName);
                                        if (eventPair != " " && eventPair != null && eventPair != "") {
                                            oldValue.add(eventPair);
                                        } else {
                                            oldValue.add("Not Specified");
                                        }
                                        drugEventList.put(drugName, oldValue);
                                    } else {
                                        if (!drugEventList.containsKey(drugName) && drugName != null && drugName != "") {
                                            HashSet<String> newValue = new HashSet<String>();

                                            if (eventPair != " " && eventPair != null && eventPair != "") {
                                                newValue.add(eventPair);
                                            } else {
                                                newValue.add("Not Specified");
                                            }
                                            drugEventList.put(drugName, newValue);
                                        }
                                    }
                                }
                            }

                        }

                        for (Map.Entry<String, HashSet<String>> entry : drugEventList.entrySet()) {

                            HashSet<String> updatedSet = entry.getValue();

                            for (String eventValue : updatedSet) {

                                jedis.sadd(entry.getKey(), eventValue);
                            }
                        }
                    }
                }
            }
        } catch (JedisException e) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/Writer !!!");
                e.printStackTrace();
            }

        }

    }

    public static boolean loading(int databaseIndex, String fileAddress, String resultFileAddress) throws InterruptedException {


        Jedis jedis = new Jedis(redisHost, redisPort);
        jedis.connect();


        //need to get the index of the database from the user next time
        jedis.select(databaseIndex);
        //please be careful this will erase all previously existing data
        jedis.flushDB();

        System.out.println("Connected jedis client");
        try {
            //calling the function
            behaveAsMapOfSets(fileAddress, jedis);
        } finally {
            jedis.disconnect();
            jedis.save();
            System.out.println("\nDisconnected jedis client");
        }
        System.out.println("Database_Loaded");

        return true;
    }
}
