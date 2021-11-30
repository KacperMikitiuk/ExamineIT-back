package com.examineit.springjwt.models;

import java.util.Random;

public class DeviceTokenGenerator {

    public String setToken(){
        Random random = new Random();
        int[] table = {48,49,50,51,52,53,54,55,56,57,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,
                80,81,82,83,84,85,86,87,88,89,90,97,98,99,100,101,102,103,104,105,106,107,108,109,
                110,111,112,113,114,115,116,117,118,119,120,121,122};
        StringBuilder stringBuilder = new StringBuilder();
        int number = table.length;
        for (int i = 0; i < 20; i++) {
            int value = table[random.nextInt(number)];
            stringBuilder.append((char)value);
        }
        String token = stringBuilder.toString();
        return token;
    }
}
