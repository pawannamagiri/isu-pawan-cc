package com.example.p2papp.controllers;


import com.example.p2papp.services.ReceiverService;
import com.example.p2papp.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Author @ Pawan Namagiri
 **/

@RestController
public class MainController {

    @Autowired
    RegisterService mainService;

    @Autowired
    ReceiverService receiverService;

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;

    }


    @GetMapping("/start")
    public ModelAndView registerWithIndexServer() {
        mainService.registerWithIndexServer();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("filesPage");

        return modelAndView;
    }

    @GetMapping("/findFile")
    public ModelAndView findFileOwner(@RequestParam String fileName) {
        String ownerIps[] = receiverService.findFileOwner(fileName);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("filesPage");
        modelAndView.addObject("owners", ownerIps);
        return modelAndView;
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String fileName) throws IOException {
        File file = new File(System.getProperty("user.home")+"/Desktop/p2p-files/"+fileName);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }


}
