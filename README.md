# IRiina

#### *Intralism* map editor written in ***Java (SE 10)***

[Releases](https://github.com/cyriaca-technologies/IRiina/releases)

## Features

* Timing events

    * Synchronize arcs and combo events in two modes

        * Tick
        
            * Lock events to ticks based on your specified starting time and ticks per second

        * Measure / beat / subbeat
        
            * Lock events to the song based on your starting time, beats per minute, beats per measure, and subbeats per beat
            
            * Click the BPM button to the beat while the song is playing to help find the right BPM to use

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

## Building native images

Compile `src/main/java` to `build/modules/net.cyriaca.riina.misc.iriina`

Copy dependency jars to `build/modules`

Run `build-win.bat` or `build-mac.sh`

## Dependencies

[Jaudiotagger](https://mvnrepository.com/artifact/org/jaudiotagger)

[JSR 374 (JSON Processing) Default Provider](https://mvnrepository.com/artifact/org.glassfish/javax.json)

* [JSR 374 (JSON Processing) API](https://mvnrepository.com/artifact/javax.json/javax.json-api)

[VorbisSPI](https://mvnrepository.com/artifact/com.googlecode.soundlibs/vorbisspi)

* [Jorbis](https://mvnrepository.com/artifact/com.googlecode.soundlibs/jorbis)

* [Tritonus Share](https://mvnrepository.com/artifact/com.googlecode.soundlibs/tritonus-share)

##

**[Intralism on Steam](https://store.steampowered.com/app/513510/Intralism/)**
