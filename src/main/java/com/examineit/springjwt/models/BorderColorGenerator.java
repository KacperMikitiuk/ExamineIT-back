package com.examineit.springjwt.models;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BorderColorGenerator {

    List<String> colors;

    public BorderColorGenerator() {
        List<String> list = new ArrayList<>();
        list.add("rgb(0,0,0)");
        list.add("rgb(255, 0, 0)");
        list.add("rgb(0, 102, 255)");
        list.add("rgb(153, 102, 51)");
        list.add("rgb(153, 51, 255)");
        this.colors = list;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getColor(){
//        BLACK("rgb(0,0,0)"),
//        RED("rgb(255, 0, 0)"),
//        BLUE("rgb(0, 102, 255)"),
//        BROWN("rgb(153, 102, 51)"),
//        PURPLE("rgb(153, 51, 255)");
        List<String> list = colors;
        String color = list.get(0);
        list.remove(0);
        return color;
    }
}
