A collection of scripts that are generating data for various SQL Sample schemas.

Scripts should be run using [jbang](https://github.com/jbangdev/jbang).

To install jbang - check the official documentation - or simply:

```sh
curl -s "https://get.sdkman.io" | bash 
source ~/.bash_profile
sdk install java 
sdk install jbang
```

Scripts can be run using:

```
jbang script_name.java >> inserts.sql
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

Output:

```
Usage: hr_schema [-hV] [-e=<numEmployees>] [-l=<numLocations>]
                 [-m=<numManagers>] [-ms=<minSalary>] [-Ms=<maxSalary>]
                 [-t=<target>]
                 
Script to generate SQL Inserts for HR Schema

  -e, --employees=<numEmployees>
                          The number of employees to be generated
                            Default: 1000
  -h, --help              Show this help message and exit.
  -l, --locations=<numLocations>
                          The number of locations to be generated
                            Default: 100
  -m, --managers=<numManagers>
                          The maximum number of managers.
                            Default: 50
  -ms, --min-salary=<minSalary>
                          The minimum salary an employee can get
                            Default: 1000
  -Ms, --max-salary=<maxSalary>
                          The maximum salary an employee can get
                            Default: 15000
  -t, --target=<target>   The target database. Supported options: mysql |
                            postgresql
                            Default: mysql
  -V, --version           Print version information and exit.
```

# Supported Schemas

## HR Schema (SQL)

If you ever had the chance to work with [Oracle DB](https://www.oracle.com/ro/database/technologies/) this is probably one of the most known sample schemas to play with. 

It models an HR application containing information about the Company's Locations, Departements, Employees and their Managers.

![HR Schema](https://github.com/nomemory/neat-sample-databases-generators/blob/main/assets/hr-schema.png)
