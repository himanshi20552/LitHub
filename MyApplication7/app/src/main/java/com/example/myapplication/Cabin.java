package com.example.myapplication;

public class Cabin {
    public String name;
    public String capacity;
    public boolean isBooked;

    public Cabin() {} // Required for Firebase

    public Cabin(String name, String capacity, boolean isBooked) {
        this.name = name;
        this.capacity = capacity;
        this.isBooked = isBooked;
    }

    // Add setter for capacity to handle Long values from Firebase
    public void setCapacity(Object capacity) {
        if (capacity instanceof Long) {
            this.capacity = String.valueOf(capacity);
        } else if (capacity instanceof String) {
            this.capacity = (String) capacity;
        }
    }
}
