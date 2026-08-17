package com.fretboardtrainer.model;

public class Note {
    private final String name;
    private final int stringNumber;
    private final int fret;

    public Note(String name, int stringNumber, int fret) {
        this.name = name;
        this.stringNumber = stringNumber;
        this.fret = fret;
    }

    public String getName() {
        return name;
    }

    public int getStringNumber() {
        return stringNumber;
    }

    public int getFret() {
        return fret;
    }

    @Override
    public String toString() {
        return "String " + stringNumber + " – Note: " + name;
    }
}

