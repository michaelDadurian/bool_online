package com.bool.data;

import com.google.appengine.api.users.User;

/**
 * Created by Nelson on 4/16/2017.
 */
public class Circuit {

    private String owner;
    private String shared;
    private String name;
    private String circuitContent;
    private String quizletConstraints;


    public Circuit(
            String owner,
            String shared,
            String name,
            String circuitContent,
            String quizletConstraints
    ){

        this.owner = owner;
        this.shared = shared;
        this.name = name;
        this.circuitContent = circuitContent;
        this.quizletConstraints = quizletConstraints;
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

    public String getCircuitContent() {
        return circuitContent;
    }

    public void setCircuitContent(String circuitContent) {
        this.circuitContent = circuitContent;
    }

    public String getQuizletConstraints() {
        return quizletConstraints;
    }

    public void setQuizletConstraints(String quizletConstraints) {
        this.quizletConstraints = quizletConstraints;
    }
}
