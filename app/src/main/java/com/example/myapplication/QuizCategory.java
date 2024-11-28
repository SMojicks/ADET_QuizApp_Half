package com.example.myapplication;

public enum QuizCategory {
    SCIENCE("Science", "9"),
    MATH("Math", "19"),
    ENGLISH("English", "10"),
    HISTORY("History", "23"),
    TECHNOLOGY("Technology", "18"),
    POP("Pop", "11");

    private final String name;
    private final String categoryId;

    QuizCategory(String name, String categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
