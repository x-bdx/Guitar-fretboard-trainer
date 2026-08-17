package com.fretboardtrainer.logic;

import com.fretboardtrainer.util.FrequencyUtils;
import com.fretboardtrainer.model.Note;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Trainer {
    private final NoteGenerator generator = new NoteGenerator();

    // Tolerance in cents. 50 cents = 1/4 tone (halfway to the wrong note).
    // 35-40 is a good balance for a beginner/intermediate player.
    private static final double CENT_TOLERANCE = 40.0;

    public void startSession(List<Integer> strings, double intervalSeconds, int noteCount, boolean enableDetection) {

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        AtomicReference<Note> currentTarget = new AtomicReference<>(null);

        // Start audio detection only if enabled
        if (enableDetection) {
            try {
                AudioDetector detector = new AudioDetector(pitch -> {
                    Note target = currentTarget.get();
                    if (target == null || pitch <= 0)
                        return;

                    // 1. Calculate the EXACT frequency expected for this specific string/fret
                    double targetFreq = FrequencyUtils.getTargetFrequency(
                            target.getStringNumber(),
                            target.getFret());

                    // 2. Calculate the difference in "cents" between heard pitch and target
                    // 1200 cents = 1 octave. 100 cents = 1 semitone.
                    double diffCents = 1200 * Math.log(pitch / targetFreq) / Math.log(2);

                    // 3. Check if the pitch is within the tolerance range
                    if (Math.abs(diffCents) <= CENT_TOLERANCE) {
                        String playedName = FrequencyUtils.frequencyToNoteName(pitch);

                        System.out.printf("✔ Correct! %s (%.2f Hz, %.1f ¢)%n",
                                playedName, pitch, diffCents);

                        currentTarget.set(null); // Clear target to wait for next note
                    }
                    // Optional: Uncomment to debug pitch issues
                    /*
                     * else {
                     * System.out.printf("...heard %.2f Hz (Target: %.2f Hz)%n", pitch, targetFreq);
                     * }
                     */
                });
                detector.start();
            } catch (Exception e) {
                System.out.println("Microphone not available: " + e.getMessage());
            }
        }

        // Random note generation loop
        Runnable task = new Runnable() {
            int counter = 0;

            public void run() {
                if (counter >= noteCount) {
                    scheduler.shutdown();
                    System.out.println("Session finished.");
                    // In a real app, you might want to stop the AudioDetector thread here too
                    System.exit(0);
                    return;
                }
                Note note = generator.getRandomNote(strings);
                currentTarget.set(note);
                System.out.println("Play this: " + note);
                counter++;
            }
        };

        long delay = (long) (intervalSeconds * 1000);
        scheduler.scheduleAtFixedRate(task, 0, delay, TimeUnit.MILLISECONDS);
    }
}