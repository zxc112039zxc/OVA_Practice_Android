package com.example.project_final;

import java.util.ArrayList;

public class Problem {
    private final String problemNumber;
    private final String problemName;
    private boolean favorite;
//    private final String url;
    static ArrayList<String> history = new ArrayList<>();

    public Problem (String inputNumber, String inputName, Integer inputFavorite){
        problemNumber = inputNumber;
        problemName = inputName;
        favorite = (inputFavorite == 1);
//        url = "";
    }

//    public Problem (String inputNumber, String inputName, Integer inputFavorite, String inputUrl){
//        problemNumber = inputNumber;
//        problemName = inputName;
//        favorite = (inputFavorite == 1);
//        url = inputUrl;
//    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getProblemNumber() {
        return problemNumber;
    }

    public String getProblemName() {
        return problemName;
    }

    public boolean isFavorite() {
        return favorite;
    }

//    public String getUrl() { return url; }
}