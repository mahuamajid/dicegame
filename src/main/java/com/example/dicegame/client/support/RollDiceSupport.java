package com.example.dicegame.client.support;

import com.example.dicegame.client.RollDiceClient;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RollDiceSupport {
    private RollDiceClient rollDiceClient;

    public int getRoll() {
       return Objects.requireNonNull(rollDiceClient.roll().getBody()).getValue();
    }
}
