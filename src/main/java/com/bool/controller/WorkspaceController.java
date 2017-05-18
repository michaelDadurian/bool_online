package com.bool.controller;


import com.bool.data.Circuit;
import com.bool.data.Datastore;
import com.bool.data.Notification;
import com.bool.data.NotificationDatastore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Text;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by Nelson on 4/19/2017.
 */

@Controller
public class WorkspaceController {

    Datastore datastore = new Datastore();
    NotificationDatastore notificationData = new NotificationDatastore();

    @RequestMapping("/workspace")
    public ModelAndView workspace(){
        ModelAndView mv = new ModelAndView("pages/workspace");

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        if(userService.isUserLoggedIn()){
            mv.addObject("currEmail", currUser.getEmail());
            System.out.println(currUser.getEmail());
        }

        return mv;
    }

    @RequestMapping(value = "/workspace/submit", method = RequestMethod.POST)
    @ResponseBody
    public String getSubmission(

            /*
            @RequestParam(required = false, value ="owner") String owner,
            @RequestParam(required = false, value ="shared") String shared,
            @RequestParam(required = false, value ="name") String name,
            @RequestParam(required = false, value ="circuitContent") String circuitContent,
            @RequestParam(required = false, value ="quizletConstraints") String quizletConstraints,
            @RequestParam(required = false, value ="tags") String tags
            */


        @RequestBody final String circuitFile
        //HttpServletRequest request
    ){

        Circuit jsonCircuitFile = Circuit.jsonToObject(circuitFile);

        if(jsonCircuitFile != null) {

            UserService userService = UserServiceFactory.getUserService();
            User currUser = userService.getCurrentUser();

            if (currUser != null) {
                jsonCircuitFile.setOwner(currUser.getEmail());
                datastore.pushData(jsonCircuitFile);

                Notification notification = new Notification(currUser.getEmail(), jsonCircuitFile.getShared(), jsonCircuitFile.getName());
                notificationData.pushData(notification);
                return "Successfully submitted circuitFile to datastore!";
            } else {
                return "User not signed in!";
            }

        }



        return "Failed!";

    }

    @RequestMapping("/workspace/toggleLoginLogout")
    public ModelAndView workspaceLogin(){

        UserService userService = UserServiceFactory.getUserService();
        User currUser = userService.getCurrentUser();

        ModelAndView mv;

        if (userService.isUserLoggedIn()) { //signed in
            mv = new ModelAndView("redirect:" + userService.createLogoutURL("/workspace"));
        } else { //not signed in
            mv = new ModelAndView("redirect:" + userService.createLoginURL("/workspace"));
        }

        return mv;
    }

    @RequestMapping(value = "workspace/{circuitOwner}/{circuitName}")
    public ModelAndView workspaceDirectShareableLinkStep2(
            @PathVariable("circuitOwner") String circuitOwner,
            @PathVariable("circuitName") String circuitName
    ){
        ModelAndView mv = new ModelAndView("pages/workspace");

        System.out.println(circuitOwner);
        System.out.println(circuitName);
        return mv;
    }

    @RequestMapping(value = "workspace/loadData", method = RequestMethod.POST)
    @ResponseBody
    public String workspaceLoad(
            //@RequestParam("circuitOwner") String circuitOwner,
            //@RequestParam("circuitName") String circuitName
            @RequestBody String circuitOwnerAndName
    ) {



        Circuit cOwnerName = Circuit.jsonToObject(circuitOwnerAndName);

        if (cOwnerName != null) {

            UserService userService = UserServiceFactory.getUserService();
            User currUser = userService.getCurrentUser();

            String actualName = cOwnerName.getName().replaceAll("%20", " ");

            Entity circuitFile = datastore.queryCircuitName(actualName, cOwnerName.getOwner());

            System.out.println("loadData, Owner: "+cOwnerName.getOwner());
            System.out.println("loadData, Name: "+actualName);

            if(circuitFile != null) {

                String datastoreOwner = (String) circuitFile.getProperty("owner");
                String datastoreShared = (String) circuitFile.getProperty("shared");
                String datastoreName = (String) circuitFile.getProperty("name");
                String databaseCircuitContent = ((Text) circuitFile.getProperty("circuitContent")).getValue();
                String datastoreQuizletConstraints = (String) circuitFile.getProperty("quizletConstraints");
                String datastoreTags = (String) circuitFile.getProperty("tags");

                String toSend = "{";

                toSend += "\"owner\": \"" + datastoreOwner + "\",";
                toSend += "\"shared\": \"" + datastoreShared + "\",";
                toSend += "\"name\": \"" + datastoreName + "\",";
                toSend += "\"circuitContent\": " + databaseCircuitContent + ",";
                toSend += "\"quizletConstraints\": " + datastoreQuizletConstraints + ",";
                toSend += "\"tags\": \"" + datastoreTags + "\"";
                toSend += "}";

                if (datastoreTags.contains("#public") || currUser != null && datastoreOwner.equals(currUser.getEmail())) {
                    return toSend;
                }
                else{
                    return "This circuit is private";
                }
            }
            else{
                return "No circuit file with this name";
            }

        }
        else{
            return "Invalid Circuit Name or Circuit Owner";
        }

        //return "Circuit Unavailable";
    }

}
