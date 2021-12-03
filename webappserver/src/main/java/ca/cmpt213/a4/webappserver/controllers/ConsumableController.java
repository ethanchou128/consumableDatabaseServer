package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.model.Consumable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ConsumableController {
    private ConsumableManager consumableManager = ConsumableManager.getInstance();
    private AtomicLong nextId = new AtomicLong();

    /**
     * get mapping to provide a simple ping alerting user the system is ready
     * @return a helpful message alerting the user the server is running
     */
    @GetMapping("/ping")
    public String pingUser() {
        return "System is Up!";
    }

    /**
     * command to load all the items of the array list in upon client start-up
     * @return json string containing all items of array list
     */
    @GetMapping("/load")
    public String loadConsumableList() {
        if(consumableManager.getConsumableListSize() == 0) {
            ConsumableManager.readFile("./text.json");
        }
        return consumableManager.createJSONStringOfArrayList(consumableManager.getItemsList());
    }

    //post mappings, required by project
    /**
     * post mapping to add item to the arraylist
     * @return json string containing all items of array list
     */
    @PostMapping("/addItem")
    public String addItem(@RequestBody Consumable consumable) {
        consumableManager.addConsumable(consumable);
        return consumableManager.createJSONStringOfArrayList(consumableManager.getItemsList());
    }

    /**
     * post mapping to delete item from the arraylist
     * @return json string containing all items of array list
     */
    @PostMapping("/removeItem/{index}")
    public String removeItem(
            @PathVariable("index") int index
    ) {
        consumableManager.deleteConsumable(index);
        return consumableManager.createJSONStringOfArrayList(consumableManager.getItemsList());
    }

    /**
     * get mapping to get a json string of all items in array list
     * @return json string which is passed to client side and used accordingly
     */
    @GetMapping("/listAll")
    public String listAllItems() {
        return consumableManager.createJSONStringOfArrayList(consumableManager.getItemsList());
    }

    /**
     * get mapping to get a json string of all expired items in array list
     * @return json string which is passed to client side and used accordingly
     */
    @GetMapping("/listExpired")
    public String listExpiredItems() {
        return consumableManager.createJSONStringOfArrayList(consumableManager.expiredItemsList());
    }

    /**
     * get mapping to get a json string of all non-expired items in array list
     * @return json string which is passed to client side and used accordingly
     */
    @GetMapping("/listNonExpired")
    public String listNonExpiredItems() {
        return consumableManager.createJSONStringOfArrayList(consumableManager.nonExpiredItemsList());
    }

    /**
     * get mapping to get a json string of all items expring in <= 7 days in array list
     * @return json string which is passed to client side and used accordingly
     */
    @GetMapping("/listExpiringIn7Days")
    public String listItemsExpiringIn7Days() {
        return consumableManager.createJSONStringOfArrayList(consumableManager.expiringIn7DaysList());
    }

    /**
     * get mapping that exits the program and writes the contents from the server into a .json file.
     */
    @GetMapping("/exit")
    public void exitProgram() {
        try {
            ConsumableManager.writeFile("text.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
