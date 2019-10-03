package com.example.newpuzzlegame;

import org.jetbrains.annotations.Contract;

public class LeaderBoard {
    private String player;
    private Double score;
    private Long ranking;
    /**
     * Storing player leaderBoard
     *
     * @param player Name
     * @param score Score
     * @param ranking Rankings
     */
    public LeaderBoard(String player, double score, long ranking){
        player = player;
        score = score;
        ranking = ranking;
    }
    /**
     * Set the player name
     *
     * @param player Player name
     */
    public void setPlayer(String player){
        player = player;
    }
    /**
     * Get the player name
     *
     * @return Player name
     */
    public String getPlayer() {
        final String player = this.player;
        return player;
    }
    /**
     * Set the score
     *
     * @param score Score
     */
    public void setScore(double score) {
        score = score;
    }
    /**
     * Get the score
     *
     * @return Score
     */
    public double getScore() {
        final Double score = this.score;
        return score;
    }
    /**
     * Set the player ranking
     *
     * @param ranking Ranking
     */
    public void setRanking(long ranking) {
        ranking = ranking;
    }
    /**
     * Get the ranking
     *
     * @return Ranking
     */
    public long getRanking() {
        final Long ranking = this.ranking;
        return ranking;
    }
}
