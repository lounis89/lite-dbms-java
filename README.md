# DBMS in Java

This project was created in 2018/2019 at the University of Paris Descartes for the Advanced Database course. The goal was to create a command-line DBMS (database management system) to understand its functioning from file/storage management on disk to the creation of queries using SQL syntax and their processing.

This project is based on the concepts presented in the book [Database Management Systems 3ed, R. Ramakrishnan et J. Gehrke](https://pages.cs.wisc.edu/~dbbook/).  

## Architecture  
The application has been decomposed into layers, as shown in this diagram created by R. Ramakrishnan et J. Gehrke  
  
<img src="https://i.imgur.com/bhPOt6j.png"  width="20%" height="20%">  

# Utilisation
### Creating a table:
```
create <rel_name> <nb_columns> <type_nth_col>
```

### Inserting tuples:

``` 
insert <rel_name> <val_of_nth_col>
fill <rel_name> <file_name>
``` 
### Displaying tuples:
``` 
selectall <rel_name>
select <rel_name> <filtered_col_num> <filtered_value>
join <rel_name_1> <rel_name_2> <rel_col_1> <rel_col_2>
``` 

### Cleaning/vacuuming the database:
``` 
clean
``` 

### Exiting the console and terminating the program:
``` 
exit
``` 
