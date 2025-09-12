package com.example.dicegame.client.support;

import com.example.dicegame.client.RollDiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class RollDiceSupport {
    private final RollDiceClient rollDiceClient;
    private final Random random = new Random();

    public int roll() {
        try {
            return rollDiceClient.roll().getBody();
        } catch (Exception e) {
            return random.nextInt(6) + 1;
        }
    }
}
