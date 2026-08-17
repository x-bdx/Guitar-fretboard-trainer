package com.fretboardtrainer.logic;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

import javax.sound.sampled.LineUnavailableException;
import java.util.function.Consumer;

public class AudioDetector {
    private final Consumer<Double> pitchCallback;

    public AudioDetector(Consumer<Double> pitchCallback) {
        this.pitchCallback = pitchCallback;
    }

    public void start() throws LineUnavailableException {
        int sampleRate = 44100;
        int bufferSize = 4096; //2048
        int overlap = 2048; //1024

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, overlap);
                // Debounce timestamp
        final long[] lastTime = {0};

        PitchDetectionHandler handler = (PitchDetectionResult result, AudioEvent e) -> {
            float pitch = result.getPitch();       // Hz
            double rms = e.getRMS() * 100;         // volume estimate

            // 1. Ignore very quiet noise
            if (rms < 20) return;

            // 2. Ignore unreal low-frequency junk
            if (pitch < 70) return;

            // 3. Debounce to avoid spam (120 ms)
            long now = System.currentTimeMillis();
            if (now - lastTime[0] < 120) return;
            lastTime[0] = now;

            // 4. Accept valid note
            pitchCallback.accept((double) pitch);
        };


        AudioProcessor processor = new PitchProcessor(
                PitchProcessor.PitchEstimationAlgorithm.YIN, //PitchProcessor.PitchEstimationAlgorithm.FFT_YIN

                sampleRate,
                bufferSize,
                handler
        );

        dispatcher.addAudioProcessor(processor);
        new Thread(dispatcher, "Audio Detector Thread").start();
    }
}

