# dames-java
Jeu de dames en Java, interface terminal
## comment compiler ?
il faut les biliothèques [JNA](http://central.maven.org/maven2/com/sun/jna/jna/3.0.9/jna-3.0.9.jar)
et [JNA-platform](http://central.maven.org/maven2/net/java/dev/jna/jna-platform/4.5.1/jna-platform-4.5.1.jar)

pour compiler, faire ``javac java -classpath .\jna-3.0.9.jar;.\jna-platform-4.5.1.jar;.\ LesDames.java``

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
Y représente le numéro des lignes (un tableu 1D) et X celui des colonnes (objets contenus dans le tableau 1D Y) 