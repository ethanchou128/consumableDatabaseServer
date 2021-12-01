package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsumableManager {
    private static ArrayList<Consumable> consumableList = new ArrayList<>();
    private static ConsumableManager instance;

    public static ConsumableManager getInstance() {
        if(instance == null) {
            instance = new ConsumableManager();
        }
        return instance;
    }

    public ArrayList<Consumable> getConsumablesList() {
        return consumableList;
    }

    public void addConsumable (Consumable newConsumable) {
        consumableList.add(newConsumable);
    }

    public void deleteConsumable(int index) {
        consumableList.remove(index);
    }

    public ArrayList<Consumable> expiredItemsList() {
        ArrayList<Consumable> expiredItems = new ArrayList<>();
        for (Consumable consumable : consumableList) {
            if(consumable.getExpiryDate().isBefore(LocalDateTime.now())) {
                expiredItems.add(consumable);
            }
        }
        return expiredItems;
    }

    public ArrayList<Consumable> nonExpiredItemsList() {
        ArrayList<Consumable> nonExpiredItems = new ArrayList<>();
        for (Consumable consumable : consumableList) {
            if (consumable.getExpiryDate().isAfter(LocalDateTime.now())) {
                nonExpiredItems.add(consumable);
            }
        }
        return nonExpiredItems;
    }

    public ArrayList<Consumable> expiringIn7DaysList() {
        ArrayList<Consumable> itemsExpiringIn7Days = new ArrayList<>();
        LocalDateTime expiryDatePlusSevenDays = LocalDateTime.now().plusDays(7);
        for (Consumable consumable : consumableList) {
            if(expiryDatePlusSevenDays.isAfter(consumable.getExpiryDate())
                    && LocalDateTime.now().isBefore(consumable.getExpiryDate())) {
                itemsExpiringIn7Days.add(consumable);
            }
        }
        return itemsExpiringIn7Days;
    }

}
