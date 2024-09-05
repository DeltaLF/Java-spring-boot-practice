package com.exampleGroup.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.exampleGroup.demo.service.JokeService;

@RestController
@RequestMapping("/api")
public class ConsumingRestController {

    @Autowired
    private JokeService jokeService;

    @GetMapping("/joke")
    public ResponseEntity<String>getJoke() {

        try{
            return new ResponseEntity<String>(jokeService.fetchJoke(), HttpStatus.OK);
        }catch(HttpClientErrorException e ){
            System.err.println(e);
            return new ResponseEntity<String>(jokeService.fetchJoke(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
