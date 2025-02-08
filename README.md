# DBMS in Java

This project is a simplified Database Management System (DBMS) implementation in Java, created at the University of Paris Descartes. It demonstrates the core concepts of database systems, from low-level disk management to SQL-like query processing.

## Project Overview

The system is built using a layered architecture, implementing key DBMS components:

### Storage Layer
- **DiskManager**: Handles low-level file operations and page allocation
- **BufferManager**: Implements buffer management with a LRU (Least Recently Used) replacement policy
- **Frame**: Represents a page in memory with pinCount management

### Record Management
- **Record**: Manages individual database records
- **RecordId**: Unique identifier for records
- **PageId**: Identifies pages within files

### Schema Management
- **DBDef**: Database schema definition and catalog management
- **RelDef**: Relation (table) definition
- **ColInfo**: Column information including types and constraints

### Query Processing
Supports basic SQL-like operations:
- Table creation
- Record insertion (single and batch)
- Selection (with filtering)
- Join operations
- Full table scans

## Technical Details

### Storage Management
- Pages are the basic unit of storage (fixed size)
- Implements buffer management to minimize disk I/O
- Uses a catalog file (`Catalog.def`) to persist database metadata

### Data Types
Supports basic data types:
- INTEGER
- REAL
- VARCHAR

### File Organization
```
project-root/
├── DB/                    # Database files directory
│   └── Catalog.def       # System catalog
├── src/
│   ├── main/java/
│   │   ├── bdda/        # Core DBMS components
│   │   └── exception/   # Custom exceptions
│   └── test/java/
│       └── tests/       # Unit tests
```

## Architecture
The application has been decomposed into layers, as shown in this diagram created by R. Ramakrishnan et J. Gehrke

<img src="https://i.imgur.com/bhPOt6j.png" width="20%" height="20%">

## Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

## Installation and Build
1. Clone the repository:
```bash
git clone https://github.com/lounis89/lite-dbms-java.git
cd lite-dbms-java
```

2. Build the project:
```bash
mvn clean package
```

## Running the Application
1. The application will automatically create a DB directory in the working directory.

2. Launch the application:
```bash
java -jar target/mini-sgbd-1.0-SNAPSHOT.jar
```

Alternatively, you can run it directly with Maven:
```bash
mvn exec:java
```

## Available Commands

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

### Exiting the console:
```
exit
```

## Development

To run tests:
```bash
mvn test
```

To clean and rebuild:
```bash
mvn clean install
```