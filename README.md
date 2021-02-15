A collection of scripts that are generating data for various SQL Sample schemas.

Scripts should be run using [jbang](https://github.com/jbangdev/jbang).

To install jbang - check the official documentation - or simply:

```sh
curl -s "https://get.sdkman.io" | bash 
source ~/.bash_profile
sdk install java 
sdk install jbang
```

# List of scripts

| Script | Schema | Targets |
| ------ | ------ | ------- |
| [scripts/hr_schema_mysql.java](https://github.com/nomemory/neat-sample-databases-generators/blob/main/scripts/hr_schema_mysql.java) | [HR](#hr-schema-sql) | mysql, mariadb |
| scripts/hr_schema_postgresql.java | [HR](#hr-schema-sql) | postgresql |

# Running the scripts

After cloning the project:

```sh
jbang scripts/hr_schema_mysql.java > inserts.sql
```

Most scripts support various options (e.g.: generating more data, or target various databases). The best way to see the options a script is offering:

```sh
jbang scripts/hr_schema_mysql.java --help
```

# Supported Schemas

## HR Schema (SQL)

If you ever had the chance to work with [Oracle DB](https://www.oracle.com/ro/database/technologies/) this is probably one of the most known sample schemas to play with. 

It models an HR application containing information about the Company's Locations, Departements, Employees and their Managers.

![HR Schema](https://github.com/nomemory/neat-sample-databases-generators/blob/main/assets/hr-schema.png)
