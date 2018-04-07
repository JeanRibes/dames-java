#!/bin/bash
jar cfm MyJar.jar Manifest.txt *.class
unzip MyJar.jar
unzip jline.jar
unzip gson-2.8.2.jar
zip -r jeu-dames.jar *