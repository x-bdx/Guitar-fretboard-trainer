# Guitar Fretboard Trainer

A CLI application in Java designed to help guitarists practice fretboard note memorization with real-time audio pitch detection through the microphone.

## Overview

The tool prompts you to find and play random notes on specified guitar strings within a set interval. When pitch detection is enabled, it uses the microphone to listen to your playing, calculates the fundamental frequency of the note, and checks if you hit the correct pitch within an acceptable tuning tolerance (measured in cents).

## Features

* Select specific strings to practice (1 through 6 in standard tuning).
* Configurable pacing and note counts per session.
* Real-time audio analysis and pitch estimation using the YIN algorithm via TarsosDSP.
* Frequency-to-cents offset calculation to handle minor natural pitch variations.

## Prerequisites

* Java Development Kit (JDK 8 or higher)
* A functional microphone connected to your system

## Building and Running

1. Clone this repository:
   git clone https://github.com/x-bdx/Guitar-fretboard-trainer.git
   cd Guitar-fretboard-trainer

2. Compile the source files with the bundled TarsosDSP library:
   Windows (PowerShell):
   javac -cp "lib/TarsosDSP-latest.jar" -d out (Get-ChildItem -Recurse src/*.java | Resolve-Path)

   Linux / macOS:
   javac -cp "lib/TarsosDSP-latest.jar" -d out $(find src -name "*.java")

3. Run the application:
   Windows:
   java -cp "out;lib/TarsosDSP-latest.jar" com.fretboardtrainer.Main

   Linux / macOS:
   java -cp "out:lib/TarsosDSP-latest.jar" com.fretboardtrainer.Main

## License

This project is open source and available under the MIT License.
