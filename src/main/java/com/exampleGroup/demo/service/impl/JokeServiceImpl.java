package com.exampleGroup.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.exampleGroup.demo.model.Joke;
import com.exampleGroup.demo.service.JokeService;


@Service
@Component
public class JokeServiceImpl implements JokeService {

    @Value("${joke.api.url}")
    private String jokeApiUrl;
    private RestTemplate restTemplate;

    @Autowired
    public JokeServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    @Override
    public String fetchJoke() throws RestClientException{
        ResponseEntity<Joke> response = restTemplate.getForEntity(jokeApiUrl, Joke.class);
        if (response == null || response.getBody() == null) {
           throw new RestClientException("Response structure is not as expected!");
        }
        Joke joke = response.getBody();

        if(joke.getJoke() == null){
            return "setup: " + joke.getSetup() + " delivery: " + joke.getDelivery();
        }
        return joke.getJoke();
    }

}
