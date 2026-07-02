package com.oyku.blog.enums;

import lombok.Getter;

@Getter
public enum Category {

    TECHNOLOGY("Technology", "Technology related posts"),
    SOFTWARE("Software", "Software development posts"),
    EDUCATION("Education", "Educational posts"),
    DESIGN("Design", "Design related posts"),
    GENERAL("General", "General posts");

    private final String displayName;
    private final String description;

    Category(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}