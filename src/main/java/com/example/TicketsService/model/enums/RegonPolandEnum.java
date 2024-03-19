package com.example.TicketsService.model.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RegonPolandEnum {
    DOLNOSASKIE("Dolnośkąskie"),
    KUJAWSKO_POMORSKIE("Kujawsko-Pomorskie"),
    LUBELSKIE("Lubelskie"),
    LUBUSKIE("Lubuskie"),
    LODZKIE("Łódzkie"),
    MALOPOLSKIE("Małopolskie"),
    MAZOWIECKIE("Mazowieckie"),
    OPOLSKIE("Opolskie"),
    PODKARPACKIE("Podkarpackie"),
    PODLASKIE("Podlaskie"),
    POMORSKIE("Pomorskie"),
    SLASKIE("Śląskie"),
    SWIETOKRZYSKIE("Świętkorzyskie"),
    WARMINSKO_MAZURSKIE("Warmińsko-Mazurskie"),
    WIELKOPOLSKIE("Wielkopolskie"),
    ZACHODNIOPOMORSKIE("Zachodniopomorskie");

    private final String displayRegon;

    RegonPolandEnum(String displayRegon){
        this.displayRegon = displayRegon;
    }

    public static RegonPolandEnum formDisplayRegon(String regon){
        for(RegonPolandEnum regonPolandEnum : RegonPolandEnum.values()){
            if(regonPolandEnum.displayRegon.equalsIgnoreCase(regon)){
                return regonPolandEnum;
            }
        }
        return null;
    }

//    public static List<String> getAvailableRegon(){
//        return Stream.of(RegonPolandEnum.values()).map(RegonPolandEnum::getDisplayRegon).collect(Collectors.toList());
//    }

    public String getDisplayRegon(){
        return displayRegon;
    }

    public String toString(){
        return this.displayRegon;
    }
}
