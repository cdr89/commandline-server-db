# commandline-server-db
Simple command line tool written using Apache CLI that implements CRUD operations on H2 DB

## Quick start
To use this tool, you have to launch it using the following command:
```
java -jar commandline-server-db-1.0.jar
```

giving no arguments, the tool will print the help message that explains the usage:

```
usage:
 -a,--add <id> <name> <description>    Add a new server
 -c,--count                            Return the number of servers
 -d,--delete <id>                      Delete a server given the id
 -e,--edit <id> <name> <description>   Edit a server given the id
 -h,--help                             Print this help message
 -l,--list                             List all servers
 -x,--clear                            Removes all records
 ```
 
## Usage examples
Some usage examples that explain how the tool works

### Add a new server
Add a new server and also print the list of the servers with -l option
```
java -jar commandline-server-db-1.0.jar -a 1 Name1 Description1 -l
```
Output:
```
Server added
Id              Name            Description
1               Name1           Description1
```

### Edit an existing server by id
In this case we add also the -c option that prints how many servers are into the DB
```
java -jar commandline-server-db-1.0.jar -e 1 Name-1 Description-1 -l -c
```
Output:
```
Server edited
Number of servers: 1
Id              Name            Description
1               Name-1          Description-1
```

### Delete an existing server by id
```
java -jar commandline-server-db-1.0.jar -d 1 -l -c
```
Output:
```
Server deleted
Number of servers: 0
Id              Name            Description
```

### Clear all records
```
java -jar commandline-server-db-1.0.jar -x
```


