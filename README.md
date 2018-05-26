# dames-java
Jeu de dames en Java, interface terminal
## comment compiler ?
il faut les biliothèques [JNA](http://central.maven.org/maven2/com/sun/jna/jna/3.0.9/jna-3.0.9.jar)
et [JNA-platform](http://central.maven.org/maven2/net/java/dev/jna/jna-platform/4.5.1/jna-platform-4.5.1.jar)
ainsi que [GSON](http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.2/gson-2.8.2.jar)
et [JavaWebsockets](https://github.com/TooTallNate/Java-WebSocket/releases/download/v1.3.8/Java-WebSocket-1.3.8.jar)
pour compiler, faire ``javac -cp .:jline.jar:gson-2.8.2.jar LesDames.java``

pour compiler, faire ``javac java -classpath .\jna-3.0.9.jar;.\jna-platform-4.5.1.jar;.\ LesDames.java``

## comment packager dans un jar ?
pour packager `jar cfm jeu-dames.jar Manifest.txt *.class`
pour exécuter : `java -jar jeu-dames.jar`

## Comment exécuter
 Linux / OSX(?) ``java -cp .:/dossier/jline.jar LesDames``

Windows: `java -classpath .\jna-3.0.9.jar;.\jna-platform-4.5.1.jar;.\ LesDames
`
# Cahier des charges
* Dans un premier temps, il faut créer un jeu fonctionnel dont les règles reposent sur l'utilisateur (pas de vérifications)
* On veut un plateau de jeu, des pions, des dames, des cases de différentes couleurs (voir PDF moodle)

# Règles
* le périmètre est une zone d'invincibilité (on peut pas manger et atterir en dehors du plateau)
* les pions peuvent manger les dames
* les dames peuvent se déplacer et manger comme elles le veulent, même en arrière (il faut avoir une case libre par contre) 
# Comment ça marche
Partie input/affichage :
pour prendre une position depuis le plateau, appeler input.getPos(), ça affichera le plateau et permettra à l'utilisateur de déplacer le curseur
le curseur se souvient de la dernière position séléctionnée, si on veut le remettre à 0, il faut faire input.reset()
on peut afficher instantanément le plateau sans rien demander au joueur, il faut faire plateau.afficher()

Les coordonnées : j'utilise un système de coordonnées : un tableau d'entiers [x,y]. Attention : quand on parcourt un tableau 2D,
Y représente le numéro des lignes (un tableau 1D) et X celui des colonnes (objets contenus dans le tableau 1D Y)

Pour la partie synchronisation, le côté serveur est ici : [central/dames](https://github.com/JeanRibes/central/tree/master/dames)

Les duets de joueurs utilisent le même token d'authentification, cependant le serveur implémente JWT, il est possible de créer un compte par joueur

demander à @JeanRibes pour les informations d'authentification

## Fun facts
Dans le mode "dégradé" où il faut utiliser ZSQD+espace puis enter, le joueur peut taper plusieurs lettres à la fois, et ça exécutera tout

ça fait un peu comme des macros au clavier