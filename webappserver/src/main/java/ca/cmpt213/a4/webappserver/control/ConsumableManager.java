package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.Drink;
import ca.cmpt213.a4.webappserver.model.Food;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * server's consumable manager that generates the filtered consumable lists,
 * and performs any logic that is called upon in the ConsumableController class.
 */
public class ConsumableManager {
    private static List<Consumable> consumableList = new ArrayList<>();
    private static List<Consumable> unfilteredConsumableList = new ArrayList<>();
    private static ConsumableManager instance;

    public static ConsumableManager getInstance() {
        if(instance == null) {
            instance = new ConsumableManager();
        }
        return instance;
    }

    /**
     * method to get the size of the arraylist
     * @return the size of list
     */
    public int getConsumableListSize() {
        return consumableList.size();
    }

    /**
     * method to add a new consumable into the server
     * @param newConsumable the consumable in question (passed from
     *                      client side) to be added into the array list
     */
    public void addConsumable (Consumable newConsumable) {
        consumableList.add(newConsumable);
    }

    /**
     * method to add a new consumable into the server
     * @param index the index of the consumable to deleted from
     *              the array list
     */
    public void deleteConsumable(int index) {
        consumableList.remove(index);
    }

    /**
     * helper method to generate a JSON string object of an arraylist with
     * the corresponding filtered array list that is requested by the client
     * in the GUI
     * @param filteredConsumableList the filtered array list which has a JSON string
     *                               generated for it
     * @return the string object which is then passed back into the client and
     * then read/parsed accordingly.
     */
    public String createJSONStringOfArrayList(List<Consumable> filteredConsumableList) {
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).setPrettyPrinting().create();
        return myGson.toJson(filteredConsumableList);
    }

    /**
     * method to return an arraylist of all items regardless of
     * the expiry date
     * @return array list of all items
     */
    public List<Consumable> getItemsList() {
        Collections.sort(consumableList);
        return consumableList;
    }

    /**
     * method to return an arraylist of all expired items
     * @return list of expired items
     */
    public List<Consumable> expiredItemsList() {
        List<Consumable> expiredItems = new ArrayList<>();
        Collections.sort(expiredItems);
        for (Consumable consumable : consumableList) {
            if(consumable.getExpiryDate().isBefore(LocalDateTime.now())) {
                expiredItems.add(consumable);
            }
        }
        return expiredItems;
    }

    /**
     * method to return an arraylist of all non-expired items
     * @return list of non-expired items
     */
    public List<Consumable> nonExpiredItemsList() {
        List<Consumable> nonExpiredItems = new ArrayList<>();
        Collections.sort(nonExpiredItems);
        for (Consumable consumable : consumableList) {
            if (consumable.getExpiryDate().isAfter(LocalDateTime.now())) {
                nonExpiredItems.add(consumable);
            }
        }
        return nonExpiredItems;
    }

    /**
     * method to return arraylist of all items expiring in <= 7 days
     * @return list of all items expiring in <= 7 days
     */
    public List<Consumable> expiringIn7DaysList() {
        List<Consumable> itemsExpiringIn7Days = new ArrayList<>();
        Collections.sort(itemsExpiringIn7Days);
        LocalDateTime expiryDatePlusSevenDays = LocalDateTime.now().plusDays(7);
        for (Consumable consumable : consumableList) {
            if(expiryDatePlusSevenDays.isAfter(consumable.getExpiryDate())
                    && LocalDateTime.now().isBefore(consumable.getExpiryDate())) {
                itemsExpiringIn7Days.add(consumable);
            }
        }
        return itemsExpiringIn7Days;
    }

    /**
     * method to read in the .json file accordingly
     * @param fileName the filename passed in
     */
    public static void readFile(String fileName) {
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).setPrettyPrinting().create();

        try {
            File inputFile = new File(fileName);
            Reader consumableReader = new FileReader(inputFile);
            Type listType = new TypeToken<ArrayList<Consumable>>(){}.getType();
            unfilteredConsumableList = myGson.fromJson(consumableReader, listType);
            repairConsumableList();
            consumableReader.close();
        } catch (Exception e) {
            System.out.println("File not found");
        }
    }

    /**
     * method that writes a new file (or overrides the passed in file)
     * @param outputFileName the file that is overwritten or created; as per the switch
     *                       statement, if a new file is written it is named newFile.json by
     *                       default.
     * @throws IOException program should never have to throw this, as a file will always be written to.
     */
    public static void writeFile(String outputFileName) throws IOException{
        FileWriter fileWriter = new FileWriter(outputFileName);
        separateConsumableList();
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).setPrettyPrinting().create();
        myGson.toJson(unfilteredConsumableList, fileWriter);
        fileWriter.close();
    }

    //helper methods used to parse the content into a json file accordingly.
    /**
     * helper method used to help the writeFile write the Json file accordingly.
     */
    public static void separateConsumableList(){
        unfilteredConsumableList.clear();
        for(Consumable c : consumableList) {
            String consumableType = c.getConsumableType();
            String consumableName = c.getName();
            String consumableNotes = c.getNotes();
            double consumablePrice = c.getPrice();
            double consumableMass = c.getMass();
            LocalDateTime expiryDate = c.getExpiryDate();

            Consumable separatedItem = new Consumable();
            separatedItem.setConsumableType(consumableType);
            separatedItem.setName(consumableName);
            separatedItem.setNotes(consumableNotes);
            separatedItem.setPrice(consumablePrice);
            separatedItem.setMass(consumableMass);
            separatedItem.setExpiryDate(expiryDate);
            unfilteredConsumableList.add(separatedItem);
        }
    }

    /**
     * helper method used to help the readFile parse the information into a Json file
     * and split the items into the subclasses.
     */
    public static void repairConsumableList() {
        for(Consumable c : unfilteredConsumableList) {
            String consumableType = c.getConsumableType();
            String consumableName = c.getName();
            String consumableNotes = c.getNotes();
            double consumablePrice = c.getPrice();
            double consumableMass = c.getMass();
            LocalDateTime expiryDate = c.getExpiryDate();
            Consumable newConsumable;
            if(consumableType.equals("Food")) {
                newConsumable = new Food();
                ((Food)newConsumable).setWeight(consumableMass);
            } else {
                newConsumable = new Drink();
                ((Drink)newConsumable).setVolume(consumableMass);
            }
            newConsumable.setConsumableType(consumableType);
            newConsumable.setName(consumableName);
            newConsumable.setNotes(consumableNotes);
            newConsumable.setPrice(consumablePrice);
            newConsumable.setMass(consumableMass);
            newConsumable.setExpiryDate(expiryDate);
            consumableList.add(newConsumable);
        }
    }
}
