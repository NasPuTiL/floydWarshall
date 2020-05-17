package com.lysy.floydWarshall.controller;

import com.lysy.floydWarshall.algorithm.AlgorithmFloydWarshall;
import com.lysy.floydWarshall.utilis.ParseStructure;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StructureController {
    @RequestMapping(method = RequestMethod.POST, value = "/floydWarshall")
    @ResponseBody
    public int getStructure(@RequestBody String structJSON) {
        System.out.println("@getStructure\n" + structJSON);
        JSONObject jsonObject = new JSONObject(structJSON);
        ParseStructure.upnarse(jsonObject.optJSONObject("structJSON"));

        AlgorithmFloydWarshall fw = new AlgorithmFloydWarshall();
        fw.printArray();
        fw.algFloydWarshall();
        fw.printArray();

        int shortestPath = fw.findShortestPath(ParseStructure.getSourceNode(), ParseStructure.getDestinationNode());
        System.out.println("shortestPath = " + shortestPath);
        return shortestPath;
    }


}
