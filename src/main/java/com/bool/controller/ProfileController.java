package com.bool.controller;

/**
 * Created by Nelson on 4/16/2017.
 */

import com.bool.data.Circuit;
import com.bool.data.Datastore;
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

    @RequestMapping("/loadtestdata")
    public String pcloadTestData() {
        datastore.loadTestData();
        return "pages/profile";
    }

    //load entire string passed from form in profile.jsp
    //psas over initial url and append the string
    //parse string and query database
    @RequestMapping(value = "profile/submitSearch", method = RequestMethod.GET)
    @ModelAttribute("searchParams")
    public ModelAndView submitSearch(Model model, HttpServletRequest request){

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();


        Search searchParams = new Search(request.getParameter("searchParams"));
        model.addAttribute("searchParams", searchParams);

        ModelAndView mv = new ModelAndView("pages/profile");
        mv.addObject("searchParams", request.getParameter("searchParams"));

        System.out.println(searchParams.getQuery());
        List<Entity> searchResults = searchParams.parseQuery(searchParams.getQuery());
        List<String> circuitNames = new ArrayList<>();
        List<String> circuitOwners = new ArrayList<>();

        List<String> canDelete = new ArrayList<>();
        List<String> canShare = new ArrayList<>();
        List<String> canGetLink = new ArrayList<>();

        for (Entity searchResult: searchResults){
            circuitNames.add((String)searchResult.getProperty("name"));
            circuitOwners.add((String)searchResult.getProperty("owner"));

            if(currUser.getEmail().equals(searchResult.getProperty("owner"))){
                canShare.add("true");
                canDelete.add("true");
            }else{
                canShare.add("false");
                canDelete.add("false");
            }

            String currShared = (String)searchResult.getProperty("shared");
            String currTags = (String)searchResult.getProperty("tags");

            if (currShared.contains(currUser.getEmail()) || currTags.contains("#public")){
                canGetLink.add("true");
            }else{
                canGetLink.add("false");
            }
        }

        mv.addObject("circuitNames", circuitNames);
        mv.addObject("circuitOwners", circuitOwners);
        mv.addObject("currUser", currUser);
        mv.addObject("canDelete", canDelete);
        mv.addObject("canShare", canShare);
        mv.addObject("canGetLink", canGetLink);


        return mv;
    }

    @RequestMapping(value = "profile/share", method = RequestMethod.GET)
    @ResponseBody
    public String shareCircuit(@RequestParam(required = true, value ="circuitName") String circuitName,
                             @RequestParam(required = true, value = "circuitOwner") String circuitOwner
                             ){

        Entity currCircuit = datastore.queryCircuitName(circuitName, circuitOwner);

        String currShared = (String)currCircuit.getProperty("shared");
        String currName = (String)currCircuit.getProperty("name");
        String currOwner = (String)currCircuit.getProperty("owner");
        String currTags = (String)currCircuit.getProperty("tags");


        return "{" +  "\"pCircuitName\":" + "\"" + currName + "\"" + "," + "\"pCircuitOwner\":" + "\"" + currOwner + "\"" +  "," + "\"pCircuitShared\":" + "\"" + currShared + "\"" +
                "," + "\"pCircuitTags\":" + "\"" + currTags + "\"" + "}";


    }

    @RequestMapping(value = "profile/submitEdit", method = RequestMethod.GET)
    @ResponseBody
    public String confirmEdit(@RequestParam(required = true, value = "circuitName") String circuitName,
                              @RequestParam(required = true, value = "circuitOwner") String circuitOwner,
                              @RequestParam(required = true, value = "circuitTags") String circuitTags,
                              @RequestParam(required = true, value = "circuitShared") String circuitShared){

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        if (circuitOwner.equals(currUser.getEmail())){
            System.out.println("name: " + circuitName + " shared with: " + circuitShared + " tags: " + circuitTags);

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

            return "SUCCESS";
        }

        return "FAILURE";

    }



    @RequestMapping(value = "profile/delete", method = RequestMethod.GET)
    @ResponseBody
    public String deleteCircuit (@RequestParam(required = true, value ="circuitName") String circuitName,
                                 @RequestParam(required = true, value = "circuitOwner") String circuitOwner,
                                 @RequestParam(required = false, value = "currRow") String currRow){

        Entity circuitToDelete = datastore.queryCircuitName(circuitName, circuitOwner);
        datastore.deleteCircuit(circuitToDelete);

        return currRow;

    }

    @RequestMapping(value = "profile/getLink", method = RequestMethod.GET)
    @ResponseBody
    public String getShareableLink (@RequestParam(required = true, value = "circuitName") String circuitName,
                                    @RequestParam(required = true, value = "circuitOwner") String circuitOwner,
                                    HttpServletRequest request){


        String url = request.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";

        String editedName = circuitName.replaceAll(" ", "+");
        System.out.println(baseURL + "workspace/" + circuitOwner + "+" + editedName);

        return baseURL + "workspace/" + "owner=" + circuitOwner + "&" + "name=" + editedName;
    }



    @RequestMapping("/profile")
    public ModelAndView profileLogin() {

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();


        System.out.println("currUser " + currUser);

        if (userService.isUserLoggedIn()) { //signed in
            ModelAndView mv = new ModelAndView("pages/profile");
            List<Entity> toDisplay = datastore.loadAllCircuits(currUser.getEmail());

            List<String> circuitNames = new ArrayList<>();
            List<String> circuitOwners = new ArrayList<>();

            List<String> canDelete = new ArrayList<>();
            List<String> canShare = new ArrayList<>();
            List<String> canGetLink = new ArrayList<>();

            for (Entity td : toDisplay) {

                circuitNames.add((String) td.getProperty("name"));
                circuitOwners.add((String) td.getProperty("owner"));

                if(currUser.getEmail().equals(td.getProperty("owner"))){
                    canDelete.add("true");
                    canShare.add("true");
                }else{
                    canDelete.add("false");
                    canShare.add("false");
                }

                String currShared = (String)td.getProperty("shared");
                String currTags = (String)td.getProperty("tags");

                if (currShared.contains(currUser.getEmail()) || currTags.contains("#public")){
                    canGetLink.add("true");
                }else{
                    canGetLink.add("false");
                }
            }

            mv.addObject("circuitNames", circuitNames);
            mv.addObject("circuitOwners", circuitOwners);
            mv.addObject("currUser", currUser);
            mv.addObject("canDelete", canDelete);
            mv.addObject("canShare", canShare);
            mv.addObject("canGetLink", canGetLink);


            return mv;
        } else { //not signed in
            return new ModelAndView("redirect:" + userService.createLoginURL("/profile"));
        }
    }





}
