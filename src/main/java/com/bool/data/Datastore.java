package com.bool.data;

/**
 * Created by Nelson on 4/16/2017.
 */

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class Datastore {

        /*Default filter here*/

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        UserService userService = UserServiceFactory.getUserService();

        public void loadTestData(){
            List<Circuit> testCircuits = new ArrayList<>();

            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "mdadurian@example.com",
                        "nelson@example.com reef@example.com",
                        "Mike's Circuit "+i,
                        "",
                        "",
                        "#public #test #sdfs"
                ));
            }

            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "nelson@example.com",
                        "mdadurian@example.com reef@example.com",
                        "Nelson's Circuit "+i,
                        "",
                        "",
                        "#public #test"
                ));
            }
            for(int i=0;i<10; i++){
                testCircuits.add(new Circuit(
                        "reef@example.com",
                        "mdadurian@example.com kenny@example.com",
                        "Reef's Circuit "+i,
                        "",
                        "",
                        "public"
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

            checkDuplicateName(circuit, circuit.getName());

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

        public void checkDuplicateName(Circuit circuit, String name){
            User currUser = userService.getCurrentUser();

            /*Get list of owned circuits to check for identical name*/
            List<Entity> ownedCircuits;



            while(true){
                Query query = new Query("Circuit");
                query.addFilter("owner", Query.FilterOperator.EQUAL, currUser.getEmail());
                query.addFilter("name", Query.FilterOperator.EQUAL, name);

                ownedCircuits = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

                if (ownedCircuits.size() >= 1){

                    name = fixDuplicateName(circuit, circuit.getName());
                    circuit.setName(name);
                }else{
                    break;
                }
            }


        }

        public String fixDuplicateName (Circuit circuit, String nameToEdit){

            int i = nameToEdit.length() - 1;
            String valueInParen;
            String nameNoNumbers;

            /*Check if name contains a number in parentheses */

            if (nameToEdit.charAt(nameToEdit.length() - 1) == ')'){

                while(i >= 0){
                    if (nameToEdit.charAt(i) == '('){
                        valueInParen = nameToEdit.substring(i+1, nameToEdit.length()-1);
                        nameNoNumbers = nameToEdit.substring(0, i);
                        if (valueInParen.matches("[0-9]+")){
                            int numericValue = Integer.parseInt(valueInParen);
                            numericValue += 1;
                            return nameNoNumbers + "(" + numericValue + ")";
                        }
                    }
                    i--;
                }
            }

            /*Default case: If no parentheses, append a (1)*/
            return circuit.getName() + "(1)";

        }

        public void cloneCircuit(Entity circuitToClone, String circuitName){

            User currUser = userService.getCurrentUser();

            Circuit circuit = new Circuit(currUser.getEmail(), "", circuitName,
                    (String)circuitToClone.getProperty("circuitContent"),
                    (String)circuitToClone.getProperty("quizletConstraints"), "");

            checkDuplicateName(circuit, circuitName);

            Key circuitKey = KeyFactory.createKey("Circuit", circuit.getName());
            Entity clonedCircuit = new Entity("Circuit", circuitKey);

            clonedCircuit.setProperty("owner", circuit.getOwner());
            clonedCircuit.setProperty("name", circuit.getName());

            clonedCircuit.setProperty("circuitContent", circuit.getCircuitContent());
            clonedCircuit.setProperty("quizletConstraints", circuit.getQuizletConstraints());

            /*Reset shared and tags*/
            clonedCircuit.setProperty("shared", circuit.getShared());
            clonedCircuit.setProperty("tags", circuit.getTags());



            datastore.put(clonedCircuit);

        }

        public void deleteCircuit(Entity circuit){
            Key circuitKey = circuit.getKey();
            datastore.delete(circuitKey);
        }

        public void removePublic(Entity circuit, String currTags){
            String editedTags = "";

            if (currTags.contains("#public;")){
                editedTags = currTags.replaceAll("#public;", "");
            }else if (currTags.contains("#public")){
                editedTags = currTags.replaceAll("#public", "");
            }

            circuit.setProperty("tags", editedTags);

            datastore.put(circuit);
        }

        public void addPublic(Entity circuit, String currTags){
            String editedTags;
            editedTags = "#public " + currTags;

            circuit.setProperty("tags", editedTags);

            datastore.put(circuit);
        }

        public List<Entity> loadYourCircuits(String owner){

            List<Entity> toLoad;
            Query query = new Query("Circuit");
            query.addFilter("owner", Query.FilterOperator.EQUAL, owner);
            toLoad = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

            return toLoad;


        }

        public Entity queryCircuitName(String name, String owner){
            Query query = new Query("Circuit");
            query.addFilter("name", Query.FilterOperator.EQUAL, name);
            query.addFilter("owner", Query.FilterOperator.EQUAL, owner);
            PreparedQuery pq = datastore.prepare(query);

            Entity circuit = pq.asSingleEntity();

            return circuit;
        }

        public void updateShared(String circuitName, String circuitOwner, String circuitShared){
            Entity circuit = queryCircuitName(circuitName, circuitOwner);
            circuit.setProperty("shared", circuitShared);

            datastore.put(circuit);

        }

        public List<Entity> loadPublicCircuits(){
            List<Entity> toLoad;
            List<Entity> publicCircuits = new ArrayList<>();

            Query query = new Query("Circuit");
            query.addFilter("tags", Query.FilterOperator.NOT_EQUAL, "");
            toLoad = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());


            for (Entity td:toLoad) {

                String tags = (String)td.getProperty("tags");
                String[] splitTags = tags.split(" ");

                for (String tag: splitTags){
                    if (tag.equals("#public")){
                        publicCircuits.add(td);
                        break;
                    }

                }
            }

            return publicCircuits;

        }

        public List<Entity> loadSharedCircuits(String sharedWith){

            List<Entity> toLoad;
            List<Entity> sharedCircuits = new ArrayList<>();

            Query query = new Query("Circuit");
            query.addFilter("shared", Query.FilterOperator.NOT_EQUAL, "");
            toLoad = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

            for (Entity td:toLoad){

                String shared = (String)td.getProperty("shared");
                String[] splitShared = shared.split(" ");

                for (String user: splitShared){
                    if (user.equals(sharedWith)){
                        sharedCircuits.add(td);

                        break;
                    }
                }
            }

            return sharedCircuits;

        }

        public List<Entity> loadNumCircuits(int num, String owner){
            List<Entity> allCircuits = new ArrayList<>();
            List<Entity> sharedCircuits = loadSharedCircuits(owner);
            List<Entity> publicCircuits = loadPublicCircuits();
            List<Entity> yourCircuits = loadYourCircuits(owner);


            allCircuits.addAll(sharedCircuits);

            int i = 0;
            /*Check for duplicates*/
            for (Entity circuit: publicCircuits){
                while (i < num){
                    if (!allCircuits.contains(circuit))
                        allCircuits.add(circuit);
                    i++;
                }
            }

            /*Check for duplicates*/
            for (Entity circuit: yourCircuits){
                while (i < num){
                    if (!allCircuits.contains(circuit))
                        allCircuits.add(circuit);
                    i++;
                }

            }

            return allCircuits;

        }

        public List<Entity> loadAllCircuits(String owner){
            List<Entity> allCircuits = new ArrayList<>();
            List<Entity> sharedCircuits = loadSharedCircuits(owner);
            List<Entity> publicCircuits = loadPublicCircuits();
            List<Entity> yourCircuits = loadYourCircuits(owner);


            allCircuits.addAll(sharedCircuits);

            /*Check for duplicates*/
            for (Entity circuit: publicCircuits){
                if (!allCircuits.contains(circuit))
                    allCircuits.add(circuit);
            }

            /*Check for duplicates*/
            for (Entity circuit: yourCircuits){
                if (!allCircuits.contains(circuit))
                    allCircuits.add(circuit);
            }


            return allCircuits;



        }

}
