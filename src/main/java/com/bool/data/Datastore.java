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
                        "Mike",
                        "",
                        "Mike's Circuit "+i,
                        "",
                        ""
                ));
            }

            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "Nelson",
                        "",
                        "Nelson's Circuit "+i,
                        "",
                        ""
                ));
            }
            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "Reef",
                        "",
                        "Reef's Circuit "+i,
                        "",
                        ""
                ));
            }
            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "Kenny",
                        "",
                        "Kenny's Circuit "+i,
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

            datastore.put(toPush);
        }

        public List<Entity> loadYourCircuits(String owner){

            List<Entity> toLoad = new ArrayList<>();
            Query query = new Query("Circuit");
            query.addFilter("owner", Query.FilterOperator.EQUAL, owner);
            toLoad = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(50));

            return toLoad;


        }



}
