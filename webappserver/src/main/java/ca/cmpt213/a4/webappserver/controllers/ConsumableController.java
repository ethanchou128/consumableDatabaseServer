package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.model.Consumable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ConsumableController {
    private ArrayList<Consumable> consumables = new ArrayList<>();
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

    //post mappings, required by project
    @PostMapping("/addItem")
    public Consumable addItem(@RequestBody Consumable consumable) {
        //set pledge to have next id
        consumable.setId(nextId.incrementAndGet());
        consumables.add(consumable);
        return consumable;
    }

    @PostMapping("/removeItem/{name}")
    public ArrayList<Consumable> removeItem(
            @PathVariable("name") String itemName
    ) {
        for(Consumable consumable : consumables) {
            if (consumable.getName().equals(itemName)) {
                consumables.remove(consumable);
                return consumables;
            }
        }
        throw new IllegalArgumentException();
    }

    @GetMapping("/listAll")
    public ArrayList<Consumable> listAllItems() {
        return consumables;
    }

    @GetMapping("/listExpired")
    public ArrayList<Consumable> listExpiredItems() {
        ArrayList<Consumable> expiredItems = new ArrayList<>();
        for (Consumable consumable : consumables) {
            if(consumable.getExpiryDate().isBefore(LocalDateTime.now())) {
                expiredItems.add(consumable);
            }
        }
        return expiredItems;
    }

    @GetMapping("/listNonExpired")
    public ArrayList<Consumable> listNonExpiredItems() {
        ArrayList<Consumable> nonExpiredItems = new ArrayList<>();
        for (Consumable consumable : consumables) {
            if (consumable.getExpiryDate().isAfter(LocalDateTime.now())) {
                nonExpiredItems.add(consumable);
            }
        }
        return nonExpiredItems;
    }

    @GetMapping("/listExpiringIn7Days")
    public ArrayList<Consumable> listItemsExpiringIn7Days() {
        ArrayList<Consumable> itemsExpiringIn7Days = new ArrayList<>();
        LocalDateTime expiryDatePlusSevenDays = LocalDateTime.now().plusDays(7);
        for (Consumable consumable : consumables) {
            if(expiryDatePlusSevenDays.isAfter(consumable.getExpiryDate())
                    && LocalDateTime.now().isBefore(consumable.getExpiryDate())) {
                itemsExpiringIn7Days.add(consumable);
            }
        }
        return itemsExpiringIn7Days;
    }

    @GetMapping("/exit")
    public void exitProgram() {

    }
}
