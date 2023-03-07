# DBMS in Java

Ce projet a été réalisé avec 3 collègues @ Université Paris Descartes en 2018/2019 pour l'UE base de données avancées. Le but était de créer un SGBD (système de gestion de bases de données) en ligne de commande afin de comprendre son fonctionnement en partant de la gestion des fichiers/stockages sur disque jusqu'à la création de requêtes avec une syntaxe SQL et le traitement de celles-ci.

Ce projet se base sur les concepts se trouvant dans le livre [Database Management Systems 3ed, R. Ramakrishnan et J. Gehrke](https://pages.cs.wisc.edu/~dbbook/).  

## Architecture  
L'application a été décomposée en couche comme sur ce schéma réalisé par R. Ramakrishnan et J. Gehrke  
  
<img src="https://i.imgur.com/bhPOt6j.png"  width="20%" height="20%">  

# Utilisation
### Créer une table:
```
create <nom_rel> <nb_colonnes> <type_nième_col>
```

### Insertion de tuples :

``` 
insert <nom_rel> <val_de_la_nième_col>
fill <nom_rel> <nom_fichier>
``` 
### Affichage de tuples :
``` 
selectall <nom_rel>
select <nom_rel> <num_col_filtrée> <valeur_filtre>
join <nom_rel_1> <nom_rel_2> <col_rel_1> <col_rel_2>
``` 

### Clean/vacuum de la base :
``` 
clean
``` 

### Quitter la console et mettre fin au programme :
``` 
exit
``` 
