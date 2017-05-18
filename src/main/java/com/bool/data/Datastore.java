package com.bool.data;

/**
 * Created by Nelson on 4/16/2017.
 */

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Text;

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



        /*Takes a Circuit object and creates a Circuit Entity to push to the datastore*/
        public void pushData(Circuit circuit){

            checkDuplicateName(circuit, circuit.getName());

            Key circuitKey = KeyFactory.createKey("Circuit", circuit.getName());
            Entity toPush = new Entity("Circuit", circuitKey);

            toPush.setProperty("owner", circuit.getOwner());
            toPush.setProperty("shared", circuit.getShared());
            toPush.setProperty("name", circuit.getName());
            toPush.setProperty("circuitContent", new Text(circuit.getCircuitContent()));
            toPush.setProperty("quizletConstraints", circuit.getQuizletConstraints());
            toPush.setProperty("tags", circuit.getTags());

            datastore.put(toPush);
        }

        /*Checks for duplicate Circuit names created by the currently logged in user*/
        public void checkDuplicateName(Circuit circuit, String name){
            User currUser = userService.getCurrentUser();

            /*Get list of owned circuits to check for identical name*/
            List<Entity> ownedCircuits;


            while(true){
                Query query = new Query("Circuit");
                query.addFilter("owner", Query.FilterOperator.EQUAL, currUser.getEmail());
                query.addFilter("name", Query.FilterOperator.EQUAL, name);

                ownedCircuits = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

                /*If there is an owned circuit with a duplicate name*/
                if (ownedCircuits.size() >= 1){
                    /*Fix and set Circuit name*/
                    name = fixDuplicateName(circuit, circuit.getName());
                    circuit.setName(name);
                }
                /*There are no duplicates, break out of look*/
                else{
                    break;
                }
            }


        }

        /*Appends a "(1)" to a Circuit that has a duplicate name
        * If the Circuit's name ends with (<number>) then increment the number within the parentheses
        *
        * Example:
        *   name = exCircuit
        *   name = exCircuit(1)
        *   name = exCircuit(2) */
        public String fixDuplicateName (Circuit circuit, String nameToEdit){

            int i = nameToEdit.length() - 1;
            String valueInParen;
            String nameNoNumbers;

            /*Check if name contains a number in parentheses
             * First check if last character of name is a closed parentheses */
            if (nameToEdit.charAt(nameToEdit.length() - 1) == ')'){

                /*Start from end of String*/
                while(i >= 0){
                    /*Search for open parentheses*/
                    if (nameToEdit.charAt(i) == '('){

                        /*Get the value in the parentheses*/
                        valueInParen = nameToEdit.substring(i+1, nameToEdit.length()-1);

                        /*Get the name without the parentheses*/
                        nameNoNumbers = nameToEdit.substring(0, i);

                        /*Check if value in parentheses is all numbers*/
                        if (valueInParen.matches("[0-9]+")){

                            /*Get the numeric value and increment it by 1*/
                            int numericValue = Integer.parseInt(valueInParen);
                            numericValue += 1;

                            /*Return the Circuit name appended with (<number>)*/
                            return nameNoNumbers + "(" + numericValue + ")";
                        }
                    }
                    i--;
                }
            }

            /*Default case: If no parentheses, append a (1)*/
            return circuit.getName() + "(1)";

        }

        /*Clones a Circuit Entity, sets current user as owner and resets the shared and tags attribute*/
        public void cloneCircuit(Entity circuitToClone, String circuitName){

            User currUser = userService.getCurrentUser();

            /*Create Circuit object with current user as owner
            * Reset shared and tags field
            * Copy circuit name, content, and constraints*/
            Circuit circuit = new Circuit(currUser.getEmail(), "", circuitName,
                    ((Text)circuitToClone.getProperty("circuitContent")).getValue(),
                    (String)circuitToClone.getProperty("quizletConstraints"), "");

            /*Check to see if new Circuit has a duplicate name*/
            checkDuplicateName(circuit, circuitName);

            /*Create key for new Circuit Entity*/
            Key circuitKey = KeyFactory.createKey("Circuit", circuit.getName());
            Entity clonedCircuit = new Entity("Circuit", circuitKey);

            clonedCircuit.setProperty("owner", circuit.getOwner());
            clonedCircuit.setProperty("name", circuit.getName());

            clonedCircuit.setProperty("circuitContent", circuit.getCircuitContent());
            clonedCircuit.setProperty("quizletConstraints", circuit.getQuizletConstraints());

            /*Reset shared and tags*/
            clonedCircuit.setProperty("shared", circuit.getShared());
            clonedCircuit.setProperty("tags", circuit.getTags());


            pushData(circuit);

        }

        /*Deletes a Circuit Entity from the datastore*/
        public void deleteCircuit(Entity circuit){
            Key circuitKey = circuit.getKey();
            datastore.delete(circuitKey);
        }

        /*Removes public tag from a Circuit Entity*/
        public void removePublic(Entity circuit, String currTags){
            String editedTags = "";

            /*Check if tag contains either "#public " or " #public" and replace it with empty string*/
            if (currTags.contains("#public ")){
                editedTags = currTags.replaceAll("#public;", "");
            }else if (currTags.contains(" #public")){
                editedTags = currTags.replaceAll("#public", "");
            }

            /*Reset tag*/
            circuit.setProperty("tags", editedTags);

            datastore.put(circuit);
        }

        /*Adds a "#public " tag to a Circuit Entity*/
        public void addPublic(Entity circuit, String currTags){
            String editedTags;
            editedTags = "#public " + currTags;

            circuit.setProperty("tags", editedTags);

            datastore.put(circuit);
        }

        /*Loads all Circuits that are owned by a specific owner*/
        public List<Entity> loadYourCircuits(String owner){

            List<Entity> toLoad;
            Query query = new Query("Circuit");
            query.addFilter("owner", Query.FilterOperator.EQUAL, owner);
            toLoad = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

            return toLoad;


        }

        /*Retrieves a Circuit Entity by querying for name and owner*/
        public Entity queryCircuitName(String name, String owner){
            Query query = new Query("Circuit");
            query.addFilter("name", Query.FilterOperator.EQUAL, name);
            query.addFilter("owner", Query.FilterOperator.EQUAL, owner);
            PreparedQuery pq = datastore.prepare(query);

            Entity circuit = pq.asSingleEntity();

            return circuit;
        }

        /*Updates the shared attribute of a Circuit Entity*/
        public void updateShared(String circuitName, String circuitOwner, String circuitShared){
            Entity circuit = queryCircuitName(circuitName, circuitOwner);
            circuit.setProperty("shared", circuitShared);

            datastore.put(circuit);

        }

        /*Loads all Circuit Entities with #public tag
        *   Returns List of Circuit Entities that are public*/
        public List<Entity> loadPublicCircuits(){
            List<Entity> toLoad;
            List<Entity> publicCircuits = new ArrayList<>();

            /*Query datastore to get all Circuits that do not have an empty "tags" attribute*/
            Query query = new Query("Circuit");
            query.addFilter("tags", Query.FilterOperator.NOT_EQUAL, "");
            toLoad = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());


            /*For each Circuit with non empty "tags"*/
            for (Entity td:toLoad) {

                /*Get the tags, split them based on space*/
                String tags = (String)td.getProperty("tags");
                String[] splitTags = tags.split(" ");

                /*For each tag found, check if it is "#public
                *   If so, add it to List and break*/
                for (String tag: splitTags){
                    if (tag.equals("#public")){
                        publicCircuits.add(td);
                        break;
                    }

                }
            }

            return publicCircuits;

        }

        /*Loads all Circuit Entities that have been shared with a certain user
        *   Returns List of Circuit Entities */
        public List<Entity> loadSharedCircuits(String sharedWith){

            List<Entity> toLoad;
            List<Entity> sharedCircuits = new ArrayList<>();

            /*Query datastore for all Circuit Entities with non-empty "shared" attribute*/
            Query query = new Query("Circuit");
            query.addFilter("shared", Query.FilterOperator.NOT_EQUAL, "");
            toLoad = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

            /*For each Circuit with non empty "shared*/
            for (Entity td:toLoad){

                /*Get users that have been shared with, split based on space*/
                String shared = (String)td.getProperty("shared");
                String[] splitShared = shared.split(" ");

                /*For each user found, check if they are the one to be shared with
                *   If so, add to List*/
                for (String user: splitShared){
                    if (user.equals(sharedWith)){
                        sharedCircuits.add(td);

                        break;
                    }
                }
            }

            return sharedCircuits;

        }


        /*Loads all circuits to be displayed on profile page
        *   Returns List of Circuit Entities*/
        public List<Entity> loadAllCircuits(String owner){
            List<Entity> allCircuits = new ArrayList<>();
            List<Entity> sharedCircuits = loadSharedCircuits(owner);
            List<Entity> publicCircuits = loadPublicCircuits();
            List<Entity> yourCircuits = loadYourCircuits(owner);

            /*Get all owned Circuits, Circuits that have been shared with you, and Circuits that are public*/

            /*Add all shared Circuits*/
            allCircuits.addAll(sharedCircuits);

            /*Check for duplicates
            *   Only add public Circuits if they aren't in the List already*/
            for (Entity circuit: publicCircuits){
                if (!allCircuits.contains(circuit))
                    allCircuits.add(circuit);
            }

            /*Check for duplicates
            *   Only add owned Circuits if they aren't in the List already*/
            for (Entity circuit: yourCircuits){
                if (!allCircuits.contains(circuit))
                    allCircuits.add(circuit);
            }


            return allCircuits;



        }

}
