package com.example.newpuzzlegame;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeaderBoardTest extends LeaderBoard {
    /**
     * Testing player leaderBoard
     *
     * @param player  Name
     * @param score   Score
     * @param ranking Rankings
     */
    public LeaderBoardTest(String player, double score, long ranking) {
        super(player, score, ranking);
    }

    @Test
    public void testLeaderBoardMethod() {
        LeaderBoard leader = new LeaderBoard("player", 1, 2);
        assertEquals("player", leader.getPlayer());
        assertEquals(2, leader.getRanking());
        assertEquals("new_player", leader.getPlayer());
    }
}
