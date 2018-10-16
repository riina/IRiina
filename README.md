# IRiina

#### *Intralism* map editor

You can download the latest release for Windows and macOS [here](https://github.com/cyriaca-technologies/IRiina/releases/latest)

(You do not need Java installed on your system to use this)

[All releases](https://github.com/cyriaca-technologies/IRiina/releases)

## Features

* Timing events

    * Synchronize arcs and combo events in two modes

        * Tick
        
            * Lock events to ticks based on your specified starting time and ticks per second

        * Measure / beat / subbeat
        
            * Lock events to the song based on your starting time, beats per minute, beats per measure, and subbeats per beat
            
            * Click the BPM button to the beat while the song is playing to help find the right BPM to use

* Remapping mode

    * Sequentially choose what arc type to use for each existing arc event

    * Automatically scrolls to the next event once you choose, or you can move forward and back with [ and ]

* Event selection sequencing

    * Make the events you selected align to ticks or subbeats based on your selected starting point and spacing between events
    
* Arc event sequence generation

    * Make a sequence of new arc events that align to ticks or subbeats based on your selected starting point and spacing between events
    
* Background flash-preview

    * See a preview of arc events being activated behind the event sequencer
    
    * See a preview of beats simulated by timing events (only in measure/beat/subbeat mode) to help find the right BPM
    
* Map import / export

    * Import normal Intralism maps to use in the editor (will be stored in this editor's modified project storage structure)

    * Export normal Intralism maps from projects opened in the editor
    
        * Knows the default location of Intralism's Editor folder in Windows (on the C drive) and on macOS (in the current user's Library folder)

* Combo events

    * Set up to one of every event type (two for foreground / background sprites) to be output at the same time

    * Events set up in the combo event will be exported as separate events

## Project Info

This project is Java 10-based. It utilizes javapackager and modules to create self-contained application bundles.

Some dependencies are only available as legacy JARs without a module-info.class, so we have provided compiled module-info.class files  for these dependencies under `build/injections_info` that are injected into the JARs you copy into `build/modules` as part of the build  process.

### Building native images

Compile `src/main/java` to `build/modules/net.cyriaca.riina.misc.iriina`

Copy dependency jars to `build/modules`

Run `build-win.bat` or `build-mac.sh`

### Dependencies

[Jaudiotagger](https://mvnrepository.com/artifact/org/jaudiotagger) 2.0.3 (LEGACY JAR)

[JSR 374 (JSON Processing) Default Provider](https://mvnrepository.com/artifact/org.glassfish/javax.json) 1.1.3

* [JSR 374 (JSON Processing) API](https://mvnrepository.com/artifact/javax.json/javax.json-api) 1.1.3

[VorbisSPI](https://mvnrepository.com/artifact/com.googlecode.soundlibs/vorbisspi) 1.0.3.3 (LEGACY JAR)

* [Jorbis](https://mvnrepository.com/artifact/com.googlecode.soundlibs/jorbis) 0.0.17.4 (LEGACY JAR)

* [Tritonus Share](https://mvnrepository.com/artifact/com.googlecode.soundlibs/tritonus-share) 0.3.7.4 (LEGACY JAR)

##

**[Intralism on Steam](https://store.steampowered.com/app/513510/Intralism/)**
