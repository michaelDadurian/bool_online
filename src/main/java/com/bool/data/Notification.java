package com.bool.data;

/**
 * Created by Refath Hossan on 5/9/17.
 */


/*Notification Object used to store Notification Entities
*   owner: the owner of the circuit to be shared
*   shared: who the circuit will be shared with. Emails separated by a space
*   name: name of the circuit */
public class Notification {
    private String owner;
    private String shared;
    private String name;

    public Notification(
            String owner,
            String shared,
            String name
    ){

        this.owner = owner;
        this.shared = shared;
        this.name = name;
    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
