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
     * @param r1 Most recent score
     * @param r2
     * @param r3
     * @param r4
     * @param r5 Earliest score
     * @return Calculated rating
     */
    public static double calculateRating(int r1, int r2, int r3, int r4, int r5) {
        double rating;

        rating = Math.floor((5 * r1 + 4 * r4 + 3 * r3 + 2 * r2 + r1) / 15);

        return rating;
    }
}
