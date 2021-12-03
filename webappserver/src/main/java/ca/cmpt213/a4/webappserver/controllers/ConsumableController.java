package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.model.Consumable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ConsumableController {
    private ConsumableManager consumableManager = ConsumableManager.getInstance();
    private AtomicLong nextId = new AtomicLong();

    @GetMapping("/ping")
    public String pingUser() {
        return "System is Up!";
    }

    @GetMapping("/load")
    public String loadConsumableList() {
        if(consumableManager.getConsumableListSize() == 0) {
            ConsumableManager.readFile("./text.json");
        }
        return consumableManager.createJSONStringOfArrayList(consumableManager.getItemsList());
    }

    //post mappings, required by project
    @PostMapping("/addItem")
    public String addItem(@RequestBody Consumable consumable) {
        //set consumable to have next id
        consumable.setId(nextId.incrementAndGet());
        consumableManager.addConsumable(consumable);
        return consumableManager.createJSONStringOfArrayList(consumableManager.getItemsList());
    }

    @PostMapping("/removeItem/{index}")
    public String removeItem(
            @PathVariable("index") int index
    ) {
        consumableManager.deleteConsumable(index);
        return consumableManager.createJSONStringOfArrayList(consumableManager.getItemsList());
    }

    @GetMapping("/listAll")
    public String listAllItems() {
        return consumableManager.createJSONStringOfArrayList(consumableManager.getItemsList());
    }

    @GetMapping("/listExpired")
    public String listExpiredItems() {
        return consumableManager.createJSONStringOfArrayList(consumableManager.expiredItemsList());
    }

    @GetMapping("/listNonExpired")
    public String listNonExpiredItems() {
        return consumableManager.createJSONStringOfArrayList(consumableManager.nonExpiredItemsList());
    }

    @GetMapping("/listExpiringIn7Days")
    public String listItemsExpiringIn7Days() {
        return consumableManager.createJSONStringOfArrayList(consumableManager.expiringIn7DaysList());
    }

    @GetMapping("/exit")
    public void exitProgram() {
        try {
            ConsumableManager.writeFile("text.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
