package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.model.Consumable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ConsumableController {
    private List<Consumable> consumables = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong();

    //testing method, remove later
    @RequestMapping
    public String testTestTesticles() {
        return "Who's Joe?";
    }

    //post mappings, required by project
    @PostMapping("/addItem")
    public Consumable addItem(@RequestBody Consumable consumable) {
        //set pledge to have next id
        consumable.setId(nextId.incrementAndGet());
        consumables.add(consumable);
        return consumable;
    }

    @PostMapping("/removeItem")
    public void removeItem() {
        //i suspect i will have to add a parameter to this later?
    }

    @GetMapping("/hello")
    public String sugma() {
        return "Sugma Cack";
    }

    @GetMapping("/listAll")
    public List<Consumable> listAllItems() {
        return consumables;
    }

    @GetMapping("/listExpired")
    public void listExpiredItems() {

    }

    @GetMapping("/listNonExpired")
    public void listNonExpiredItems() {

    }

    @GetMapping("/listExpiringIn7Days")
    public void listItemsExpiringIn7Days() {

    }

    @GetMapping("/exit")
    public void exitProgram() {

    }
}
