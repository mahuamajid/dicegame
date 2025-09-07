package com.example.dicegame.client.support;

import com.example.dicegame.client.RollDiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RollDiceSupport {
    private final RollDiceClient rollDiceClient;

    public int roll() {
       return Objects.requireNonNull(rollDiceClient.roll().getBody()).getValue();
    }
}
