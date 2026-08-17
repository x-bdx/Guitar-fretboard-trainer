package com.fretboardtrainer.util;

public class FrequencyUtils {

    // Note names for display
    private static final String[] NOTES = {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
    };

    // MIDI starting numbers for Standard Tuning.
    // Index 0 is unused.
    // Index 1 = High E (64)
    // Index 2 = B (59)
    // Index 3 = G (55)
    // Index 4 = D (50)
    // Index 5 = A (45)
    // Index 6 = Low E (40)
    private static final int[] STRING_MIDI_OFFSETS = { 0, 64, 59, 55, 50, 45, 40 };

    /**
     * Calculates the exact target frequency (Hz) for a specific string and fret.
     * Uses Standard Tuning MIDI offsets (1 = High E, 6 = Low E).
     */
    public static double getTargetFrequency(int stringNumber, int fret) {
        // Validation to prevent crashes
        if (stringNumber < 1 || stringNumber > 6)
            return -1;

        // 1. Calculate the MIDI note number (Base string pitch + fret)
        int openStringMidi = STRING_MIDI_OFFSETS[stringNumber];
        int targetMidi = openStringMidi + fret;

        // 2. Convert MIDI to Frequency
        // Formula: f = 440 * 2^((d - 69) / 12)
        return 440.0 * Math.pow(2, (targetMidi - 69) / 12.0);
    }

    /**
     * Converts a frequency to a note name (e.g. 82.4 -> "E")
     * Ignores octave (just for display purposes).
     */
    public static String frequencyToNoteName(double frequency) {
        if (frequency <= 0)
            return "Unknown";

        // Convert Hz to MIDI Note Number
        double noteNumber = 12 * (Math.log(frequency / 440.0) / Math.log(2)) + 69;

        // Round to nearest semitone
        int rounded = (int) Math.round(noteNumber);

        // Modulo 12 to get the note name index (0=C, 1=C#, etc.)
        int index = (rounded % 12 + 12) % 12;

        return NOTES[index];
    }
}