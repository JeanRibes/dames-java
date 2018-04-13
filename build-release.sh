#!/bin/bash
jar cfm MyJar.jar Manifest.txt *.class
yes N|unzip MyJar.jar
yes N|unzip jline.jar
yes N|unzip Java-WebSocket-1.3.8.jar
yes N|unzip gson-2.8.2.jar
zip -r jeu-dames.jar *