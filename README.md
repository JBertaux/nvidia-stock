# Nvidia Stock poller

This application polls the Nvidia API to check if there is stock for a RTX 4090. It uses IFTTT to send a trigger an applet via a WebHook.

## How to use ?

The Spring Boot script is enabled so the jar can be build and run with the following commands:

Build:
```
./gradlew build
```

Run:
```
./nvidia-stock-0.0.1-SNAPSHOT.jar --ifttt.key=YOUR_IFTTT_KEY --ifttt.event=IFTTT_EVENT_NAME --store.locale=fr-fr
```
