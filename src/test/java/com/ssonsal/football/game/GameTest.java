package com.ssonsal.football.game;

import com.ssonsal.football.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

@SpringBootTest
public class GameTest {

    @Autowired
    GameService gameService;

    @TestConfiguration
    static class GameConfig {

    }
}
