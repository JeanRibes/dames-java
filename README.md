# dames-java
Jeu de dames en Java, interface terminal
# comment compiler ?
il faut la bibliothèque Jline
et [GSON](http://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.2/gson-2.8.2.jar)
pour compiler, faire ``javac -cp .:jline.jar:gson-2.8.2.jar LesDames.java``

# comment packager dans un jar ?
pour packager `jar cfm jeu-dames.jar Manifest.txt *.class`
pour exécuter : `java -jar jeu-dames.jar`

pour exécuter, ``java -cp .:jline.jar:gson-2.8.2.jar LesDames``
# Cahier des charges
* Dans un premier temps, il faut créer un jeu fonctionnel dont les règles reposent sur l'utilisateur (pas de vérifications)
* On veut un plateau de jeu, des pions, des dames, des cases de différentes couleurs (voir PDF moodle)

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