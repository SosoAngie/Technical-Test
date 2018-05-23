package com.example.angel.technicaltest;

class Repo {

    private String name;
    private String description;
    private int watcherCount;

    public Repo(String name, String description, int watcherCount){
        this.name=name;
        this.description=description;
        this.watcherCount=watcherCount;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWatcherCount() {
        return watcherCount;
    }

    public void setWatcherCount(int watcherCount) {
        this.watcherCount = watcherCount;
    }
}
