package com.example.newpuzzlegame;

public class User implements Comparable<User>{
    public String userId;
    public int score;
    public String userName;
    public User(String userId,String userName,int score){
        this.userId = userId;
        this.userName = userName;
        this.score = score;
    }
    public User(String userName,int score){
        this.userName = userName;
        this.score = score;
    }
    public void setUserId(String userId){
        this.userId=userId;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setScore(int score){
        this.score = score;
    }

    public String getUserId(){
        return userId;
    }
    public Integer getScore(){
        return score;
    }
    public String getUserName(){
        return userName;
    }

    @Override
    public int compareTo(User user) {
        return this.getScore().compareTo(user.getScore());
    }
}
