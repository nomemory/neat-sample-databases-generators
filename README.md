A list of scripts that are genering DDL and DML scripts to fill-up sample schemas with data.

Scripts are written in Java, and can be run using [jbang](https://github.com/jbangdev/jbang).

To install jbang - [check the official documentation](https://github.com/jbangdev/jbang) - or simply:

```sh
curl -s "https://get.sdkman.io" | bash 
source ~/.bash_profile
sdk install java 
sdk install jbang
```

Data is generated using [mockneat](https://www.mockneat.com).

Contributions are welcomed!

# List of scripts

| Script | Schema | Targets |
| ------ | ------ | ------- |
| [scripts/classicmodels_mysql.java](https://github.com/nomemory/neat-sample-databases-generators/blob/main/scripts/classicmodels_mysql.java) | [classicmodels](#classicmodels-sql) | mysql, mariadb |
| [scripts/classicmodels_postgresql.java](https://github.com/nomemory/neat-sample-databases-generators/blob/main/scripts/classicmodels_postgresql.java) | [classicmodels](#classicmodels-sql) | postgresql |
| [scripts/hr_schema_mysql.java](https://github.com/nomemory/neat-sample-databases-generators/blob/main/scripts/hr_schema_mysql.java) | [HR](#hr-schema-sql) | mysql, mariadb |
| [scripts/hr_schema_postgresql.java](https://github.com/nomemory/neat-sample-databases-generators/blob/main/scripts/hr_schema_postgresql.java) | [HR](#hr-schema-sql) | postgresql |

# Running the scripts

After cloning the project:

```sh
jbang scripts/hr_schema_mysql.java > inserts.sql
```

Most scripts support various options (e.g.: generating more data, or target various databases). The best way to see the options a script is offering:

```sh
jbang scripts/hr_schema_mysql.java --help
```

Each subsequent run will generate arbitrary data, different than the previous run.

# Supported Schemas

ER diagrams were generated using [DBeaver](https://dbeaver.io/). 

## HR Schema (SQL)

If you ever had the chance to work with [Oracle DB](https://www.oracle.com/ro/database/technologies/) this is probably one of the most known sample schemas to play with. 

It models an HR application containing information about the Company's Locations, Departements, Employees and their Managers.

![HR Schema](https://github.com/nomemory/neat-sample-databases-generators/blob/main/assets/hr-schema.png)

## classicmodels (SQL)

This is a classic schema describing a online-shop. 

It's one of the official sample schemas for MySQL.

![classicmodels](https://github.com/nomemory/neat-sample-databases-generators/blob/main/assets/classicmodels_schema.png)
