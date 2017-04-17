package com.bool.data;

/**
 * Created by Nelson on 4/16/2017.
 */

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class Datastore {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        UserService userService = UserServiceFactory.getUserService();

        public void loadTestData(){
            List<Circuit> testCircuits = new ArrayList<>();

            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "mdadurian@example.com",
                        "nelson@gmail.com;reef@gmail.com",
                        "Mike's Circuit "+i,
                        "",
                        "",
                        "#public;#test;#sdfs"
                ));
            }

            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "nelson@example.com",
                        "",
                        "Nelson's Circuit "+i,
                        "",
                        "",
                        "#public;#test"
                ));
            }
            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "reef@example.com",
                        "",
                        "Reef's Circuit "+i,
                        "",
                        "",
                        "#public"
                ));
            }
            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "kenny@example.com",
                        "",
                        "Kenny's Circuit "+i,
                        "",
                        "",
                        ""
                ));
            }

            for (Circuit tc:testCircuits) {
                pushData(tc);
            }
        }



        public void pushData(Circuit circuit){

            Key circuitKey = KeyFactory.createKey("Circuit", circuit.getName());
            Entity toPush = new Entity("Circuit", circuitKey);

            toPush.setProperty("owner", circuit.getOwner());
            toPush.setProperty("shared", circuit.getShared());
            toPush.setProperty("name", circuit.getName());
            toPush.setProperty("circuitContent", circuit.getCircuitContent());
            toPush.setProperty("quizletConstraints", circuit.getQuizletConstraints());
            toPush.setProperty("tags", circuit.getTags());

            datastore.put(toPush);
        }

        public List<Entity> loadYourCircuits(String owner){

            List<Entity> toLoad;
            Query query = new Query("Circuit");
            query.addFilter("owner", Query.FilterOperator.EQUAL, owner);
            toLoad = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));

            return toLoad;


        }

        public List<Entity> loadPublicCircuits(){
            List<Entity> toLoad;
            List<Entity> publicCircuits = new ArrayList<>();

            Query query = new Query("Circuit");
            query.addFilter("tags", Query.FilterOperator.NOT_EQUAL, "");
            toLoad = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));


            for (Entity td:toLoad) {

                String tags = (String)td.getProperty("tags");
                String[] splitTags = tags.split(";");

                for (String tag: splitTags){
                    if (tag.equals("#public")){
                        publicCircuits.add(td);
                        break;
                    }

                }
            }

            return publicCircuits;

        }



}
