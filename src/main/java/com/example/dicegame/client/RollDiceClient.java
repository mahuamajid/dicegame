package com.example.dicegame.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
    name = "roll-dice-service",
    url = "${feign.client.roll-dice.url}"
)
public interface RollDiceClient {

    @GetMapping
    ResponseEntity<Integer> roll();
}
