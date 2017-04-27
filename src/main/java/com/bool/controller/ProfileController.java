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
@RequestMapping(value = "/profile")
public class ProfileController {

    SearchService searchService;

    Datastore datastore = new Datastore();

    @RequestMapping("/loadtestdata")
    public String pcloadTestData() {
        datastore.loadTestData();
        return "pages/profile";
    }

    //load entire string passed from form in profile.jsp
    //psas over initial url and append the string
    //parse string and query database
    @RequestMapping(value = "submitSearch", method = RequestMethod.POST)
    @ModelAttribute("searchParams")
    public ModelAndView submitSearch(Model model, HttpServletRequest request){


        String searchParams = new String();
        model.addAttribute("searchParams", searchParams);

        System.out.println(request.getParameter("searchParams"));

        ModelAndView mv = new ModelAndView("redirect:/profile/");
        mv.addObject("searchParams", request.getParameter("searchParams"));

        return mv;
    }




    @RequestMapping("/")
    public ModelAndView profileLogin() {

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();


        System.out.println("currUser " + currUser);

        if (userService.isUserLoggedIn()) { //signed in
            ModelAndView mv = new ModelAndView("pages/profile");
            List<Entity> toDisplay = datastore.loadAllCircuits(currUser.getEmail());
            List<String> circuitNames = new ArrayList<>();
            List<String> circuitOwners = new ArrayList<>();

            for (Entity td : toDisplay) {
                circuitNames.add((String) td.getProperty("name"));
                circuitOwners.add((String) td.getProperty("owner"));
            }

            mv.addObject("circuitNames", circuitNames);
            mv.addObject("circuitOwners", circuitOwners);
            mv.addObject("currUser", currUser);


            return mv;
        } else { //not signed in
            return new ModelAndView("redirect:" + userService.createLoginURL("/profile"));
        }
    }

    @RequestMapping("profile/public")
    public ModelAndView profilePublicCircuits() {

        ModelAndView mv = new ModelAndView("pages/profile");
        List<Entity> toDisplay = datastore.loadPublicCircuits();
        List<String> circuitNames = new ArrayList<>();
        List<String> circuitOwners = new ArrayList<>();


        for (Entity td : toDisplay) {
            circuitNames.add((String) td.getProperty("name"));
            circuitOwners.add((String) td.getProperty("owner"));
        }


        mv.addObject("circuitNames", circuitNames);
        mv.addObject("circuitOwners", circuitOwners);
        mv.addObject("circuitObjects", toDisplay);

        return mv;
    }

    @RequestMapping("profile/shared")
    public ModelAndView profileSharedCircuits() {

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        ModelAndView mv = new ModelAndView("pages/profile");
        List<Entity> toDisplay = datastore.loadSharedCircuits(currUser.getEmail());
        List<String> circuitNames = new ArrayList<>();
        List<String> circuitOwners = new ArrayList<>();

        for (Entity td : toDisplay) {
            circuitNames.add((String) td.getProperty("name"));
            circuitOwners.add((String) td.getProperty("owner"));
        }

        mv.addObject("circuitNames", circuitNames);
        mv.addObject("circuitOwners", circuitOwners);

        return mv;

    }

    @RequestMapping("profile/profile")
    public ModelAndView profileYourCircuits() {

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        ModelAndView mv = new ModelAndView("pages/profile");
        List<Entity> toDisplay = datastore.loadYourCircuits(currUser.getEmail());
        List<String> circuitNames = new ArrayList<>();
        List<String> circuitOwners = new ArrayList<>();

        for (Entity td : toDisplay) {
            circuitNames.add((String) td.getProperty("name"));
            circuitOwners.add((String) td.getProperty("owner"));

        }

        mv.addObject("circuitNames", circuitNames);
        mv.addObject("circuitOwners", circuitOwners);

        return mv;


    }


}
