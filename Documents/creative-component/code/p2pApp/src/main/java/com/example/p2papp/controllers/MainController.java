package com.example.p2papp.controllers;


import com.example.p2papp.services.ReceiverService;
import com.example.p2papp.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;

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
    public ResponseEntity<FileSystemResource> downloadFile() {
        File file = new File("Users/pawannamagiri/Desktop/imp-docs/PawanSaiNamagiriResume.pdf");
        FileSystemResource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
