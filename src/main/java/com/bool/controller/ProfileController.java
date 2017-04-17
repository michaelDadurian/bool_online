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

    @RequestMapping("/profile/login")
    public ModelAndView profile(){

        //datastore.loadTestData();

        /*
        List<String> toDisplay = new ArrayList<>();

        for(int i=0;i<10;i++){
            toDisplay.add("entry "+i);
        }

        toDisplay.add("hello world");




        ModelAndView mv = new ModelAndView("pages/profile");
        //mv.addObject("Strings", toDisplay);

        return mv;

        */

        ModelAndView mv = new ModelAndView("pages/profile");
        List<Entity> toDisplay = datastore.loadYourCircuits("Mike");
        List<String> circuitNames = new ArrayList<>();

        for (Entity td:toDisplay) {
            circuitNames.add((String)td.getProperty("name"));
        }


        mv.addObject("circuitNames", circuitNames);
        //model.addAttribute("circuitNames", circuitNames);


        return mv;
    }

    @RequestMapping("/profile")
    public ModelAndView profileLogin(ModelMap model){

        UserService userService = UserServiceFactory.getUserService();
        //User currUser = userService.getCurrentUser();

        return new ModelAndView("redirect:" + userService.createLoginURL("/profile/login"));




    }



}
