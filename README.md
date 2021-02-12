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
| scripts/hr_schema.java | HR | mysql, postgresql |
