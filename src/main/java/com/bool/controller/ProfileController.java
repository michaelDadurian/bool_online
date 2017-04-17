package com.bool.controller;

/**
 * Created by Nelson on 4/16/2017.
 */

import com.bool.data.Circuit;
import com.bool.data.Datastore;

import com.google.appengine.api.datastore.*;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String pcloadTestData(){
        datastore.loadTestData();
        return "pages/profile";
    }

    @RequestMapping("/profile")
    public ModelAndView profileLogin(){

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();


        System.out.println("currUser "+currUser);

        if(userService.isUserLoggedIn()){ //signed in
            ModelAndView mv = new ModelAndView("pages/profile");
            List<Entity> toDisplay = datastore.loadYourCircuits(currUser.getEmail());
            List<String> circuitNames = new ArrayList<>();

            for (Entity td:toDisplay) {
                circuitNames.add((String)td.getProperty("name"));
            }


            mv.addObject("circuitNames", circuitNames);
            mv.addObject("currUser", currUser);



            return mv;
        }
        else{ //not signed in
            return new ModelAndView("redirect:" + userService.createLoginURL("/profile"));
        }
    }

    @RequestMapping("profile/public")
    public ModelAndView profilePublicCircuits(){

        ModelAndView mv = new ModelAndView("pages/profile");
        List<Entity> toDisplay = datastore.loadPublicCircuits();
        List<String> circuitNames = new ArrayList<>();

        for (Entity td:toDisplay){
            circuitNames.add((String)td.getProperty("name"));
        }

       mv.addObject("circuitNames", circuitNames);

        return mv;
    }



}
