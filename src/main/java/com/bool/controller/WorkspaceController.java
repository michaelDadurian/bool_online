package com.bool.controller;


import com.bool.data.Circuit;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;

/**
 * Created by Nelson on 4/19/2017.
 */

@Controller
public class WorkspaceController {

    @RequestMapping("/workspace")
    public String workspace(){
        return "pages/workspace";
    }

    @RequestMapping(value = "/workspace/submit")
    public String getSubmission(
            @RequestParam(required = false, value = "owner") String owner,
            @RequestParam(required = false, value ="shared") String shared,
            @RequestParam(required = false, value ="name") String name,
            @RequestParam(required = false, value ="circuitContent") String circuitContent,
            @RequestParam(required = false, value ="quizletConstraints") String quizletConstraints,
            @RequestParam(required = false, value ="tags") String tags

    ){
        System.out.println("\n\n\n\n\n\naaaaaaaaaaaaa\n\n\n\n\n\n\n\n\n\n");
        //System.out.println(circuitFile.toString());
        System.out.println(name);

        return "Successfully submitted circuitFile to datastore!";
    }

}
