package com.fretboardtrainer.logic;

import com.fretboardtrainer.model.Note;
import com.fretboardtrainer.util.Constants;
import java.util.List;
import java.util.Random;

public class NoteGenerator {
    private final Random random = new Random();

    public Note getRandomNote(List<Integer> strings) {
        int stringNumber = strings.get(random.nextInt(strings.size()));
        String[] notes = Constants.STANDARD_TUNING[stringNumber - 1];
        int fret = random.nextInt(12);
        String noteName = notes[fret];
        return new Note(noteName, stringNumber, fret);
    }
}
