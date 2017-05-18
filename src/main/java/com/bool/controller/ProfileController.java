package com.bool.controller;

/**
 * Created by Nelson on 4/16/2017.
 */

import com.bool.data.Circuit;
import com.bool.data.Datastore;
import com.bool.data.Notification;
import com.bool.data.NotificationDatastore;
import com.bool.search.Search;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.repackaged.com.google.appengine.api.search.SearchServicePb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller

public class ProfileController {

    Datastore datastore = new Datastore();
    NotificationDatastore notificationData = new NotificationDatastore();

    /*Loads test data*/
    @RequestMapping("/loadtestdata")
    public String pcloadTestData() {
        datastore.loadTestData();
        notificationData.loadTestData();
        return "pages/profile";
    }

    @RequestMapping(value = "profile/openCircuit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView openCircuit (@RequestParam(required = true, value = "circuitName") String circuitName,
                               @RequestParam(required = true, value = "circuitOwner") String circuitOwner,
                                    HttpServletRequest request){

        ModelAndView mv;

        String url = request.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

        String editedName = circuitName.replaceAll(" ", "+");

        String toRedirect = "redirect: /workspace/" + circuitOwner + "&"  + editedName;

        mv = new ModelAndView(toRedirect);

        System.out.println(circuitName);
        System.out.println(circuitOwner);
        System.out.println(toRedirect);

        return mv;
    }

    /*Receives search query from jsp, parses it, and displays the search results
    * Returns a new model and view with search results*/
    @RequestMapping(value = "profile/submitSearch", method = RequestMethod.GET)
    @ModelAttribute("searchParams")
    public ModelAndView submitSearch(Model model, HttpServletRequest request){

        /*Get current user*/
        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        /*Create Search object and add it to the model as an attribute*/
        Search searchParams = new Search(request.getParameter("searchParams"));
        model.addAttribute("searchParams", searchParams);

        /*Add search query as an object*/
        ModelAndView mv = new ModelAndView("pages/profile");
        mv.addObject("searchParams", request.getParameter("searchParams"));

        List<Entity> searchResults = searchParams.parseQuery(searchParams.getQuery());
        List<String> circuitNames = new ArrayList<>();
        List<String> circuitOwners = new ArrayList<>();

        List<String> canDelete = new ArrayList<>();
        List<String> canShare = new ArrayList<>();
        List<String> canGetLink = new ArrayList<>();
        List<String> canClone = new ArrayList<>();
        List<String> canOpen = new ArrayList<>();

        /*For each entity in list of search results, get the name and owner of the circuit.

        * Also determine whether the currently logged in user can share, get a link, delete, clone, or open the circuit
        * Users can share, delete, and open a circuit file if they are the owner of the circuit.
        * If the circuit has been shared or has the #public tag, the user can get a shareable link to it or clone it.*/
        for (Entity searchResult: searchResults){

            circuitNames.add((String)searchResult.getProperty("name"));
            circuitOwners.add((String)searchResult.getProperty("owner"));

            /*Determine if currently logged in user owns the circuit*/
            if(currUser.getEmail().equals(searchResult.getProperty("owner"))){
                canShare.add("true");
                canDelete.add("true");
                canClone.add("false");
                canOpen.add("true");
            }else{
                canShare.add("false");
                canDelete.add("false");
                canClone.add("true");
                canOpen.add("false");
            }

            String currShared = (String)searchResult.getProperty("shared");
            String currTags = (String)searchResult.getProperty("tags");

            if (currShared.contains(currUser.getEmail()) || currTags.contains("#public")){
                canGetLink.add("true");
            }else{
                canGetLink.add("false");
            }
        }

        /*Reload notifications*/
        List<Entity> notifications = notificationData.loadSharedNotification(currUser.getEmail());
        List<String> notificationNames = new ArrayList<>();
        List<String> notificationOwners = new ArrayList<>();

        for (Entity notification: notifications){
            notificationNames.add((String) notification.getProperty("name"));
            notificationOwners.add((String) notification.getProperty("owner"));

        }

        /*Add all objects to model*/
        mv.addObject("notificationNames",notificationNames);
        mv.addObject("notificationOwners",notificationOwners);

        mv.addObject("circuitNames", circuitNames);
        mv.addObject("circuitOwners", circuitOwners);
        mv.addObject("currUser", currUser);


        mv.addObject("canDelete", canDelete);
        mv.addObject("canShare", canShare);
        mv.addObject("canGetLink", canGetLink);
        mv.addObject("canClone", canClone);
        mv.addObject("canOpen", canOpen);


        return mv;
    }



    /*Receives current circuit from share.js and sends back the shared and tags property
    * Return JSON String*/
    @RequestMapping(value = "profile/share", method = RequestMethod.GET)
    @ResponseBody
    public String shareCircuit(@RequestParam(required = true, value ="circuitName") String circuitName,
                             @RequestParam(required = true, value = "circuitOwner") String circuitOwner
                             ){

        /*Query for circuit*/
        Entity currCircuit = datastore.queryCircuitName(circuitName, circuitOwner);

        /*Get circuit properties*/
        String currShared = (String)currCircuit.getProperty("shared");
        String currName = (String)currCircuit.getProperty("name");
        String currOwner = (String)currCircuit.getProperty("owner");
        String currTags = (String)currCircuit.getProperty("tags");



        return "{" +  "\"pCircuitName\":" + "\"" + currName + "\"" + "," + "\"pCircuitOwner\":" + "\"" + currOwner + "\"" +  "," + "\"pCircuitShared\":" + "\"" + currShared + "\"" +
                "," + "\"pCircuitTags\":" + "\"" + currTags + "\"" + "}";


    }

    /*When shared edit is confirmed, update datastore
    * Receives updated shared field, and adds/removes #public based on radio button selection
    * Return SUCCESS if datastore was successfully updated. */
    @RequestMapping(value = "profile/submitEdit", method = RequestMethod.GET)
    @ResponseBody
    public String confirmEdit(@RequestParam(required = true, value = "circuitName") String circuitName,
                              @RequestParam(required = true, value = "circuitOwner") String circuitOwner,
                              @RequestParam(required = true, value = "circuitTags") String circuitTags,
                              @RequestParam(required = true, value = "circuitShared") String circuitShared){

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        if (circuitOwner.equals(currUser.getEmail())){

            /*Query for circuit*/
            Entity circuit = datastore.queryCircuitName(circuitName,circuitOwner);
            String currTags = (String)circuit.getProperty("tags");

            /*If circuit is already public*/
            if (currTags.contains("#public")){
                /*If changing it to private, remove the public tag
                * If not changing to private, do nothing*/
                if (circuitTags.equals("private")){
                    datastore.removePublic(circuit, currTags);
                }

            }
            /*If circuit is not public*/
            else{
                /*If changing to public, add #public tag*/
                if (circuitTags.equals("public")){
                    datastore.addPublic(circuit, currTags);
                }
            }

            datastore.updateShared(circuitName, circuitOwner, circuitShared);

            /*Create a notification with updated share*/
            Notification notification = new Notification(currUser.getEmail(), circuitShared, circuitName);
            notificationData.pushData(notification);

            return "SUCCESS";
        }

        return "FAILURE";

    }


    /*Deletes a circuit from the datastore
    * Receives name, owner, and the row that was selected from delete.js
    * Returns the current row*/
    @RequestMapping(value = "profile/delete", method = RequestMethod.GET)
    @ResponseBody
    public String deleteCircuit (@RequestParam(required = true, value ="circuitName") String circuitName,
                                 @RequestParam(required = true, value = "circuitOwner") String circuitOwner,
                                 @RequestParam(required = false, value = "currRow") String currRow){

        Entity circuitToDelete = datastore.queryCircuitName(circuitName, circuitOwner);
        datastore.deleteCircuit(circuitToDelete);

        return currRow;

    }

    /*Receives circuit name and owner from get-link.js and creates/sends a link to it
    * Returns the link to be displayed in text area*/
    @RequestMapping(value = "profile/getLink", method = RequestMethod.GET)
    @ResponseBody
    public String getShareableLink (@RequestParam(required = true, value = "circuitName") String circuitName,
                                    @RequestParam(required = true, value = "circuitOwner") String circuitOwner,
                                    HttpServletRequest request){


        String url = request.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

        String editedName = circuitName.replaceAll(" ", "%20");


        return baseURL + "workspace/" + circuitOwner + "/"  + editedName;
    }

    /*Receives circuit name and owner from clone.js and calls Datastore.java to clone the circuit
    * Returns success message to be displayed in alert box*/
    @RequestMapping(value = "profile/cloneCircuit", method = RequestMethod.GET)
    @ResponseBody
    public String cloneCircuit(@RequestParam(required = true, value = "circuitName") String circuitName,
                               @RequestParam(required = true, value = "circuitOwner") String circuitOwner){


        Entity circuitToClone = datastore.queryCircuitName(circuitName,circuitOwner);
        datastore.cloneCircuit(circuitToClone, circuitName);


        return "Successfuly cloned " + circuitName + " created by " + circuitOwner;
    }

    /*Receives circuit owner, shared, name, content, constraints and tags from file object send by upload-local.js
    * Creates circuit Entity and pushes to datastore.
    * Returns SUCCESS upon completion*/
    @RequestMapping(value = "profile/uploadLocal", method = RequestMethod.POST)
    @ResponseBody
    public String upload(
            /*@RequestParam(required = false, value = "owner") String owner,
                       @RequestParam(required = false, value = "shared") String shared,
                       @RequestParam(required = false, value = "name") String name,
                       @RequestParam(required = false, value = "circuitContent") String circuitContent,
                       @RequestParam(required = false, value = "quizletConstraints") String constraints,
                       @RequestParam(required = false, value = "tags") String tags
             */
            @RequestBody String circuitFile
                       ){
        System.out.println(circuitFile);
        Circuit jsonCircuitFile = Circuit.jsonToObject(circuitFile);
        System.out.println("JSON CIRCUIT FILE!!!! "+jsonCircuitFile);

        if(jsonCircuitFile != null) {
            UserService userService = UserServiceFactory.getUserService();
            User currUser = userService.getCurrentUser();

            Circuit circuitToUpload = new Circuit(currUser.getEmail(), "", jsonCircuitFile.getName(), jsonCircuitFile.getCircuitContent(), jsonCircuitFile.getQuizletConstraints(), jsonCircuitFile.getTags());
            datastore.pushData(circuitToUpload);
            return "SUCCESS";
        }
        else{
            return "Invalid file upload";
        }

    }



    /*Prompts user to log in if not already logged in, then displays all public, shared, and owned circuits, along with notifications for the user*/
    @RequestMapping("/profile")
    public ModelAndView profileLogin() {

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        /*If the user is logged in*/
        if (userService.isUserLoggedIn()) {
            ModelAndView mv = new ModelAndView("pages/profile");

            /*Retrieve all notifications and public/shared/owned circuits*/
            List<Entity> toDisplay = datastore.loadAllCircuits(currUser.getEmail());
            List<Entity> notifications = notificationData.loadSharedNotification(currUser.getEmail());

            /*Declare lists to hold all circuit names and their owners, to be displayed in profile.jsp*/
            List<String> circuitNames = new ArrayList<>();
            List<String> circuitOwners = new ArrayList<>();

            /*Declare lists to hold all notification names and owners*/
            List<String> notificationNames = new ArrayList<>();
            List<String> notificationOwners = new ArrayList<>();

            /*Declare lists to store data to access in jsp*/
            List<String> canDelete = new ArrayList<>();
            List<String> canShare = new ArrayList<>();
            List<String> canGetLink = new ArrayList<>();
            List<String> canClone = new ArrayList<>();
            List<String> canOpen = new ArrayList<>();

            /*For each circuit to be displayed*/
            for (Entity td : toDisplay) {

                /*Add circuit name and owner to lists*/
                circuitNames.add((String) td.getProperty("name"));
                circuitOwners.add((String) td.getProperty("owner"));

                /*If circuit is owned by the current user, display share and delete buttons. Cannot clone owned circuits*/
                if(currUser.getEmail().equals(td.getProperty("owner"))){
                    canOpen.add("true");
                    canDelete.add("true");
                    canShare.add("true");
                    canClone.add("false");
                }
                /*If circuit is not owned by the current user*/
                else{
                    canOpen.add("false");
                    canDelete.add("false");
                    canShare.add("false");
                    canClone.add("true");
                }

                /*Get circuit shared and tags property to check if user can get the link */
                String currShared = (String)td.getProperty("shared");
                String currTags = (String)td.getProperty("tags");

                if (currShared.contains(currUser.getEmail()) || currTags.contains("#public")){
                    canGetLink.add("true");
                }else{
                    canGetLink.add("false");
                }
            }

            /*Add each notification name and owner to list*/
            for (Entity notification : notifications){
                notificationNames.add((String) notification.getProperty("name"));
                notificationOwners.add((String) notification.getProperty("owner"));
            }


            /*Add objects to model and view to access in profile.jsp*/
            mv.addObject("circuitNames", circuitNames);
            mv.addObject("circuitOwners", circuitOwners);
            mv.addObject("currUser", currUser);

            mv.addObject("notificationNames", notificationNames);
            mv.addObject("notificationOwners", notificationOwners);

            mv.addObject("canDelete", canDelete);
            mv.addObject("canShare", canShare);
            mv.addObject("canGetLink", canGetLink);
            mv.addObject("canClone", canClone);
            mv.addObject("canOpen", canOpen);


            return mv;
        }
        /*If user is not logged in, redirect to login url*/
        else {
            return new ModelAndView("redirect:" + userService.createLoginURL("/profile"));
        }
    }

    /*Logs a user out from profile page*/
    @RequestMapping(value = "/profile/logout")
    public ModelAndView profileLogout(){

        UserService userService = UserServiceFactory.getUserService();
        ModelAndView mv;

        mv = new ModelAndView("redirect:" + userService.createLogoutURL("/"));

        return mv;

    }



    /*When a notification is clicked, circuit name and owner is displayed*/
    @RequestMapping(value = "profile/loadCircuitFromNotification", method = RequestMethod.GET)
    @ModelAttribute("searchParams")
    public ModelAndView loadCircuitFromNotification(Model model, HttpServletRequest request) {

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        /*Get previous search query*/
        Search searchParams = new Search(request.getParameter("searchParams"));
        model.addAttribute("searchParams", searchParams);

        ModelAndView mv = new ModelAndView("pages/profile");
        mv.addObject("searchParams", request.getParameter("searchParams"));

        /*Get search results by parsing previous query*/
        List<Entity> searchResults = searchParams.parseQuery(searchParams.getQuery());

        /*Declare lists to hold all circuit names and their owners, to be displayed in profile.jsp*/
        List<String> circuitNames = new ArrayList<>();
        List<String> circuitOwners = new ArrayList<>();

        /*Declare lists to store data to access in jsp*/
        List<String> canDelete = new ArrayList<>();
        List<String> canShare = new ArrayList<>();
        List<String> canGetLink = new ArrayList<>();
        List<String> canClone = new ArrayList<>();
        List<String> canOpen = new ArrayList<>();

        /*For each circuit in search result*/
        for (Entity searchResult : searchResults) {

            /*Add circuit name and owner to lists*/
            circuitNames.add((String) searchResult.getProperty("name"));
            circuitOwners.add((String) searchResult.getProperty("owner"));

            /*If circuit is owned by the current user, display share and delete buttons. Cannot clone owned circuits*/
            if(currUser.getEmail().equals(searchResult.getProperty("owner"))){
                canOpen.add("true");
                canDelete.add("true");
                canShare.add("true");
                canClone.add("false");
            }

            /*If circuit is not owned by the current user*/
            else{
                canOpen.add("false");
                canDelete.add("false");
                canShare.add("false");
                canClone.add("true");
            }

            /*Get circuit shared and tags property*/
            String currShared = (String)searchResult.getProperty("shared");
            String currTags = (String)searchResult.getProperty("tags");

            /*Check to see if current user can get a link to the circuit. Needs to be shared with the user OR public*/
            if (currShared.contains(currUser.getEmail()) || currTags.contains("#public")){
                canGetLink.add("true");
            }else{
                canGetLink.add("false");
            }

        }


        /*Get circuit owner from notification*/
        StringBuilder circuitBuild = new StringBuilder();
        for (String a : circuitNames) {
            circuitBuild.append(a);
            circuitBuild.append(" ");
        }
        String circuitName = circuitBuild.toString();
        circuitName = circuitName.substring(0, circuitName.length() - (" ").length());


        StringBuilder ownerBuild = new StringBuilder();
        for (String a : circuitOwners) {
            ownerBuild.append(a);
            ownerBuild.append(" ");
        }
        String ownerName = ownerBuild.toString();
        ownerName = ownerName.substring(0, ownerName.length() - (" ").length());


        /*Delete notification from datastore once circuit has been displayed*/
        deleteNotification(circuitName, ownerName);


        /*Reload notifications*/
        List<Entity> notifications = notificationData.loadSharedNotification(currUser.getEmail());
        List<String> notificationNames = new ArrayList<>();
        List<String> notificationOwners = new ArrayList<>();

        /*Get notification names and owners*/
        for (Entity notification : notifications) {
            notificationNames.add((String) notification.getProperty("name"));
            notificationOwners.add((String) notification.getProperty("owner"));


        }

        /*Add objects to model and view*/
        mv.addObject("notificationNames", notificationNames);
        mv.addObject("notificationOwners", notificationOwners);

        mv.addObject("circuitNames", circuitNames);
        mv.addObject("circuitOwners", circuitOwners);
        mv.addObject("currUser", currUser);

        mv.addObject("canDelete", canDelete);
        mv.addObject("canShare", canShare);
        mv.addObject("canGetLink", canGetLink);
        mv.addObject("canClone", canClone);
        mv.addObject("canOpen", canOpen);



        return mv;
    }

    /*Gets Notification Entity then sends to NotificationDatastore.java to delete the Entity*/
    public void deleteNotification (String notificationName, String notificationOwner){

        Entity notificationToDelete = notificationData.queryNotificationName(notificationName,notificationOwner);
        notificationData.deleteNotification(notificationToDelete);

    }

    /*Displays notification names and owners*/
    @RequestMapping("/profile/notifications")
    public ModelAndView profileNotifications() {

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        ModelAndView mv = new ModelAndView("pages/profile");
        List<Entity> toDisplay = notificationData.loadSharedNotification(currUser.getEmail());
        List<String> notificationNames = new ArrayList<>();
        List<String> notificationOwners = new ArrayList<>();

        for (Entity td : toDisplay) {
            notificationNames.add((String) td.getProperty("name"));
            notificationOwners.add((String) td.getProperty("owner"));
        }

        mv.addObject("notificationNames", notificationNames);
        mv.addObject("notificationOwners", notificationOwners);

        return mv;

    }





}
