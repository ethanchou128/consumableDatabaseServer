package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.model.Consumable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ConsumableController {
    private ArrayList<Consumable> consumables = new ArrayList<>();
    private ConsumableManager consumableManager = ConsumableManager.getInstance();
    private AtomicLong nextId = new AtomicLong();

    //testing method, remove later
    @RequestMapping
    public String testTestTesticles() {
        return "Who's Joe?";
    }

    @GetMapping("/ping")
    public String pingUser() {
        return "System is Up!";
    }

    @GetMapping("/load")
    public void loadConsumableList() {
        if(consumableManager.getConsumableListSize() == 0) {
            ConsumableManager.readFile("./text.json");
        }
    }

    //post mappings, required by project
    @PostMapping("/addItem")
    public List<Consumable> addItem(@RequestBody Consumable consumable) {
        //set pledge to have next id
        consumable.setId(nextId.incrementAndGet());
        consumableManager.addConsumable(consumable);
        return consumableManager.getConsumablesList();
    }

    @PostMapping("/removeItem/{index}")
    public List<Consumable> removeItem(
            @PathVariable("index") int index
    ) {
        consumableManager.deleteConsumable(index);
        return consumableManager.getConsumablesList();
    }

    @GetMapping("/listAll")
    public List<Consumable> listAllItems() {
        return consumableManager.getConsumablesList();
    }

    @GetMapping("/listExpired")
    public List<Consumable> listExpiredItems() {
        return consumableManager.expiredItemsList();
    }

    @GetMapping("/listNonExpired")
    public List<Consumable> listNonExpiredItems() {
        return consumableManager.nonExpiredItemsList();
    }

    @GetMapping("/listExpiringIn7Days")
    public List<Consumable> listItemsExpiringIn7Days() {
        return consumableManager.expiringIn7DaysList();
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
