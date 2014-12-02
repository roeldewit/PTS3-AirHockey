package Airhockey.Utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roel
 */
public class ScoreCalculator {

    /**
     * Calculate the rating of a user bases on his five most recent games
     * @param score1 Most recent score
     * @param score2
     * @param score3
     * @param score4
     * @param score5 Earliest score
     * @return Calculated rating
     */
    public static double calculateRating(int score1, int score2, int score3, int score4, int score5) {
        double rating;

        rating = Math.floor((5 * score1 + 4 * score4 + 3 * score3 + 2 * score2 + score1) / 15);

        return rating;
    }
}
