# dames-java
Jeu de dames en Java, interface terminal
# comment compiler ?
il faut la bibliothèque Jline
(sur linux) : télécharger [jline3.jar](http://central.maven.org/maven2/org/jline/jline/3.6.2/jline-3.6.2.jar)

pour compiler, faire ``javac -cp .:/dossier/jline.jar LesDames.java``

pour exécuter, ``java -cp .:/dossier/jline.jar LesDames``
# Cahier des charges
* Dans un premier temps, il faut créer un jeu fonctionnel dont les règles reposent sur l'utilisateur (pas de vérifications)
* On veut un plateau de jeu, des pions, des dames, des cases de différentes couleurs (voir PDF moodle)
