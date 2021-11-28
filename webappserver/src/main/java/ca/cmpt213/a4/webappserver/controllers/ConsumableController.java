package ca.cmpt213.a4.webappserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumableController {

    //testing method, remove later
    @RequestMapping
    public String testTestTesticles() {
        return "Who's Joe?";
    }

    //post mappings, required by project
    @PostMapping("/addItem")
    public void addItem() {

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
    public void listAllItems() {

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
