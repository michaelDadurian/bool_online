package com.bool.data;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Nelson on 4/16/2017.
 */
public class Circuit {

    private String owner;
    private String shared;
    private String name;
    private String circuitContent;
    private String quizletConstraints;
    private String tags;

    public Circuit(){}


    public Circuit(
            String owner,
            String shared,
            String name,
            String circuitContent,
            String quizletConstraints,
            String tags
    ){

        this.owner = owner;
        this.shared = shared;
        this.name = name;
        this.circuitContent = circuitContent;
        this.quizletConstraints = quizletConstraints;
        this.tags = tags;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString(){
        String ret = owner + " " + shared + " " + name + " " + circuitContent + " " + quizletConstraints + " " + tags;

        return ret;
    }

    public static Circuit jsonToObject(String everything){
        Circuit ret;
        ObjectMapper mapper = new ObjectMapper();
        try{
            Circuit jsonCircuitFile = mapper.readValue(everything, Circuit.class);

            System.out.println(jsonCircuitFile);
            ret = jsonCircuitFile;
        }
        catch (JsonGenerationException e) {
            //e.printStackTrace();
            System.out.println("jsonToObject JsonGenerationException!");
            ret = null;
        } catch (JsonMappingException e) {
            //e.printStackTrace();
            System.out.println("jsonToObject JsonMappingException!");
            ret = null;
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("jsonToObject IOException!");
            ret = null;
        }

        return ret;
    }

}
