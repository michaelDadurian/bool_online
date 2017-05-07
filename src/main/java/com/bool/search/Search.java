package com.bool.search;

import com.bool.data.Datastore;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.ArrayList;
import java.util.List;

/*Parse query, split into separate strings
* filter datastore, send list back to profile controller
*
*
* Search just filters the dataastore based on the parsed query*/
public class Search{

    private String query;

    private String name;
    private String tag;
    private String owner;
    private String shared;

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Datastore data = new Datastore();

    public Search(){}

    public Search(String query){
        this.query = query;
    }

    public List<Entity> parseQuery(String query){
        /*Default search: just circuit name
          <name> #<tag> owner:<owner> shared:<shared>
        * owner:john owner:mike
        * Make 4 lists that hold each one of the fields (names, tags, ownres, shared)
        * tokenize on space, if there is something unrecognizable then just filter on name
        * "Nelson's Circuit" use contains filter to search for any name containing Nelson's and Circuit
        * */
        List<String> names = new ArrayList<String>();
        List<String> tags = new ArrayList<String>();
        List<String> owners = new ArrayList<String>();
        List<String> sharedWith = new ArrayList<String>();

        String[] splitQuery = query.split(" ");

        List<Entity> defaultFilter = getDefaultCircuits();

        for (String term: splitQuery){

            if (term.charAt(0) == ('#')){
                tags.add(term);
                System.out.println("Found tag: " + term.substring(1));
            }
            else if (term.contains("owner:")){
                owners.add(term.substring(6));
                System.out.println("Found owner: " + term.substring(6));
            }
            else if (term.contains("shared:")){
                sharedWith.add(term.substring(7));
                System.out.println("Found shared: " + term.substring(7));
            }
            else{
                names.add(term);
                System.out.println("Found name: " + term);
            }

        }

        List<Entity> searchResult = new ArrayList<>();
        for (Entity circuit: defaultFilter){
            String currTags = (String)circuit.getProperty("tags");
            String currShared = (String)circuit.getProperty("shared");
            String currOwner = (String)circuit.getProperty("owner");
            String currName = (String)circuit.getProperty("name");

            //System.out.println("Curr entity: " + circuit.getProperty("name"));
            /*Search by tag only*/
            if (!tags.isEmpty() && sharedWith.isEmpty() && owners.isEmpty() && names.isEmpty()){
                System.out.println("Search by tag");
                if (checkProperty(currTags, tags)){
                    if (!searchResult.contains(circuit))
                        searchResult.add(circuit);
                }
            }
            /*Search by tag and shared with*/
            else if (!tags.isEmpty() && !sharedWith.isEmpty() && owners.isEmpty() && names.isEmpty()){
                System.out.println("Search by tag and shared");
                if (checkProperty(currTags, tags) && checkProperty(currShared, sharedWith)){
                    if (!searchResult.contains(circuit))
                        searchResult.add(circuit);
                }
            }

            /*Search by tag and shared with and owners*/
            else if (!tags.isEmpty() && !sharedWith.isEmpty() && !owners.isEmpty() && names.isEmpty()){
                System.out.println("Search by tag, shared, and owner");
                if (checkProperty(currTags, tags) && checkProperty(currShared, sharedWith) && checkProperty(currOwner, owners)){
                    if (!searchResult.contains(circuit))
                        searchResult.add(circuit);
                }
            }

            /*Search by tag and shared with and owners and name*/
            else if (!tags.isEmpty() && !sharedWith.isEmpty() && !owners.isEmpty() && !names.isEmpty()){
                System.out.println("Search by tag, shared, owner, and name");
                
                if (checkProperty(currTags, tags) && checkProperty(currShared, sharedWith) && checkProperty(currOwner, owners) && checkProperty(currName, names)){
                    if (!searchResult.contains(circuit))
                        searchResult.add(circuit);
                }
            }

            else if (tags.isEmpty() && sharedWith.isEmpty() && !owners.isEmpty() && names.isEmpty()){
                System.out.println("Search by owner");
                if (checkProperty(currOwner, owners)){
                    if (!searchResult.contains(circuit))
                        searchResult.add(circuit);
                }

            }
            else if (tags.isEmpty() && sharedWith.isEmpty() && owners.isEmpty() && !names.isEmpty()){
                System.out.println("Search by name");
                if (checkProperty(currName, names)){
                    if (!searchResult.contains(circuit))
                        searchResult.add(circuit);
                }

            }
            else if (!tags.isEmpty() && sharedWith.isEmpty() && owners.isEmpty() && !names.isEmpty()){
                System.out.println("Search by name and tags");
                if (checkProperty(currName, names) && checkProperty(currTags, tags)){
                    if (!searchResult.contains(circuit))
                        searchResult.add(circuit);
                }
            }

            else if (tags.isEmpty() && !sharedWith.isEmpty() && owners.isEmpty() && names.isEmpty()){
                System.out.println("Search by shared");
                if (checkProperty(currShared, sharedWith)){
                    if (!searchResult.contains(circuit))
                        searchResult.add(circuit);
                }
            }




        }

        return searchResult;


    }

    public List<Entity> getDefaultCircuits(){

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();


        /*Define default filter:
            owner: currentUser && (shared: currUser || tag.contains(#public))
        */

        /*Get all circuits belonging to the current user*/
        List<Entity> ownedCircuits = data.loadYourCircuits(currUser.getEmail());

        /*Get all circuits that have been shared with the current user*/
        List<Entity> sharedCircuits = data.loadSharedCircuits(currUser.getEmail());

        /*Get all public circuits*/
        List<Entity> publicTags = data.loadPublicCircuits();

        List<Entity> defaultFilter = new ArrayList<>();
        defaultFilter.addAll(ownedCircuits);
        defaultFilter.addAll(sharedCircuits);
        defaultFilter.addAll(publicTags);

        return defaultFilter;

    }


    public boolean checkProperty(String currProperty, List<String> searchTerms){
        int fullMatch = 1;

        /*Check to see if the searched property is contained within the current Circuit
        * searchTerms = ["#public", "#test", "#sdfs"]
        * currProperty= "#public;#test;#sdfs"               */


        for(String term: searchTerms){

            if (!currProperty.contains(term)){
                return false;
            }
        }

        return true;

    }


    public String getQuery(){
        return query;
    }
    public void setQuery(String query){
        this.query = query;
    }

}