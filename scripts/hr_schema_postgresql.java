///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.1
//DEPS net.andreinc:mockneat:0.4.6

import net.andreinc.mockneat.abstraction.MockUnitString;
import net.andreinc.mockneat.unit.text.sql.SQLTable;
import net.andreinc.mockneat.unit.text.sql.escapers.MySQL;
import net.andreinc.mockneat.unit.text.sql.escapers.PostgreSQL;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static java.lang.Integer.parseInt;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static net.andreinc.mockneat.types.enums.DictType.*;
import static net.andreinc.mockneat.types.enums.StringFormatType.CAPITALIZED;
import static net.andreinc.mockneat.types.enums.StringFormatType.UPPER_CASE;
import static net.andreinc.mockneat.unit.address.Cities.cities;
import static net.andreinc.mockneat.unit.objects.Constant.constant;
import static net.andreinc.mockneat.unit.objects.From.from;
import static net.andreinc.mockneat.unit.regex.Regex.regex;
import static net.andreinc.mockneat.unit.seq.IntSeq.intSeq;
import static net.andreinc.mockneat.unit.seq.Seq.seq;
import static net.andreinc.mockneat.unit.text.Formatter.fmt;
import static net.andreinc.mockneat.unit.text.SQLInserts.sqlInserts;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.text.Words.words;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.user.Names.names;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

@Command(
        name = "hr_schema",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Script to generate SQL Inserts for HR Schema (PostgreSQL)"
)
class hr_schema_postgresql implements Callable<Integer> {

    // const
    final int NUM_COUNTRIES = 241;

    @CommandLine.Option(
            names = { "-l", "--locations" },
            description = "The number of locations to be generated",
            defaultValue = "100",
            showDefaultValue = ALWAYS

    )
    int numLocations = 100;

    @CommandLine.Option(
            names = { "-e", "--employees" },
            description = "The number of employees to be generated",
            defaultValue = "1000",
            showDefaultValue = ALWAYS
    )
    int numEmployees = 1000;

    @CommandLine.Option(
            names = { "-m", "--managers" },
            description = "The maximum number of managers.",
            defaultValue = "50",
            showDefaultValue = ALWAYS
    )
    int numManagers = 50;

    @CommandLine.Option(
        names = { "-ms", "--min-salary" },
        description = "The minimum salary an employee can get",
        defaultValue = "1000",
        showDefaultValue = ALWAYS
    )
    int minSalary = 1000;

    @CommandLine.Option(
            names = {"-Ms", "--max-salary" },
            description = "The maximum salary an employee can get",
            defaultValue = "15000",
            showDefaultValue = ALWAYS
    )
    int maxSalary = 15000;

    String[] regionNames = new String[] {
            "Europe",
            "Americas",
            "Asia",
            "Middle East and Africa"
    };

    String[] streetPrefixes = new String[]{
            "Ave",
            "Avenue",
            "St",
            "Street",
            "Blvd",
            "Rd",
            "Road",
            "Hill"
    };

    String[] jobNames = new String[] {
            "Analyst",
            "Consultant",
            "Senior Consultant",
            "Manager",
            "Software Architect",
            "Senior Manager",
            "Director"
    };

    String[] jobIds = new String[] {
            "A",
            "C",
            "SC",
            "M",
            "SA",
            "SM",
            "D"
    };

    String schemaCreatePostgreSQL =
            "drop schema if exists hr cascade ;\n" +
            "create schema hr;\n" +
            "SET search_path TO hr;";

    String ddlPostgreSQL =
            "create table regions (\n" +
                "\tregion_id INT primary key,\n" +
                "\tregion_name VARCHAR(25)\n" +
                ");\n" +
                "\n" +
            "create table countries (\n" +
                "\tcountry_id CHAR(2) primary key,\n" +
                "\tcountry_name VARCHAR(128),\n" +
                "\tregion_id INT\n" +
                ");\n" +
                "\n" +
            "create table locations (\n" +
                "\tlocation_id INT primary key,\n" +
                "\tstreet_address VARCHAR(40),\n" +
                "\tpostal_code VARCHAR(12),\n" +
                "\tcity VARCHAR(30) not null,\n" +
                "\tstate_province VARCHAR(25),\n" +
                "\tcountry_id CHAR(2) not null\n" +
                ");\n" +
                "\n" +
            "create table departments (\n" +
                "\tdepartment_id INT primary key,\n" +
                "\tdepartment_name VARCHAR(30) not null,\n" +
                "\tmanager_id INT,\n" +
                "\tlocation_id INT\n" +
                ");\n" +
                "\n" +
            "create table jobs (\n" +
                "\tjob_id VARCHAR(10) primary key,\n" +
                "\tjob_title VARCHAR(35) not null,\n" +
                "\tmin_salary DECIMAL(8,0),\n" +
                "\tmax_salary DECIMAL(8,0)\n" +
                "\t);\n" +
                "\t\n" +
            "create table employees (\n" +
                "\temployee_id INT primary key,\n" +
                "\tfirst_name VARCHAR(20),\n" +
                "\tlast_name VARCHAR(25) not null,\n" +
                "\temail VARCHAR(128) not null,\n" +
                "\tphone_number VARCHAR(20),\n" +
                "\thire_date DATE not null,\n" +
                "\tjob_id VARCHAR(10) not null,\n" +
                "\tsalary DECIMAL(8, 2) not null,\n" +
                "\tcommission_pct DECIMAL(2, 2),\n" +
                "\tmanager_id INT,\n" +
                "\tdepartment_id INT\n" +
                "\t);\n" +
                "\t\n" +
            "create table job_history (\n" +
                "\temployee_id INT primary key,\n" +
                "\tstart_date DATE not null,\n" +
                "\tend_date DATE not null,\n" +
                "\tjob_id VARCHAR(10) not null,\n" +
                "\tdepartment_id INT \n" +
                "\t);\n" +
                "\t\n" +
                "create unique index emp_sdate_ui on job_history (\n" +
                "\temployee_id,\n" +
                "\tstart_date\n" +
                "\t);";

    String foreignKeysMySQL =
            "ALTER TABLE countries ADD FOREIGN KEY (region_id) REFERENCES regions(region_id);    \n" +
            "ALTER TABLE locations ADD FOREIGN KEY (country_id) REFERENCES countries(country_id);\n" +
            "ALTER TABLE departments ADD FOREIGN KEY (location_id) REFERENCES locations(location_id);\n" +
            "ALTER TABLE employees ADD FOREIGN KEY (job_id) REFERENCES jobs(job_id);\n" +
            "ALTER TABLE employees ADD FOREIGN KEY (department_id) REFERENCES departments(department_id);\n" +
            "ALTER TABLE employees ADD FOREIGN KEY (manager_id) REFERENCES employees(employee_id);\n" +
            "ALTER TABLE departments ADD FOREIGN KEY (manager_id) REFERENCES employees (employee_id);\n" +
            "ALTER TABLE job_history ADD FOREIGN KEY (employee_id) REFERENCES employees(employee_id);\n" +
            "ALTER TABLE job_history ADD FOREIGN KEY (job_id) REFERENCES jobs(job_id);\n" +
            "ALTER TABLE job_history ADD FOREIGN KEY (department_id) REFERENCES departments(department_id);";

    public final MockUnitString streets =
            fmt("#{num} #{noun} #{end}")
            .param("num", ints().range(10, 2000))
            .param("noun", words().nouns().format(CAPITALIZED))
            .param("end", from(streetPrefixes));

    public final MockUnitString postalCodes =
            fmt("#{word1} #{word2}")
                    .param("word1", strings().size(3).format(UPPER_CASE))
                    .param("word2", strings().size(3).format(UPPER_CASE));

    public static String sanitizeName(String text) {
        return text == null ? null :
                Normalizer
                        .normalize(text, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("[^A-Za-z ]", "");
    }

    public void validateCall() {
        if (numManagers >= numEmployees) {
            throw new IllegalArgumentException("The number of managers cannot be bigger than the number of employees");
        }
    }

    @Override
    public Integer call() {

        validateCall();

        Function<String, String> escape = PostgreSQL.TEXT_BACKSLASH;

        SQLTable regions =
                sqlInserts()
                .tableName("regions")
                .column("region_id", intSeq().start(1))
                .column("region_name", seq(regionNames), escape)
                .table(regionNames.length)
                .get();

        SQLTable countries =
                sqlInserts()
                .tableName("countries")
                .column("country_id",  seq(COUNTRY_ISO_CODE_2), escape)
                .column("country_name", seq(COUNTRY_NAME), escape)
                .column("region_id", regions.fromColumn("region_id"))
                .table(NUM_COUNTRIES)
                .get();

        SQLTable locations =
                sqlInserts()
                .tableName("locations")
                .column("location_id", intSeq().start(1000).increment(100))
                .column("street_address", streets, escape)
                .column("postal_code", postalCodes, escape)
                .column("city", cities().us(), escape)
                .column("state_province", cities().capitals(), escape)
                .column("country_id", countries.fromColumn("country_id"), escape)
                .table(numLocations)
                .get();

        SQLTable jobs = sqlInserts()
                .tableName("jobs")
                .column("job_id", seq(jobIds), escape)
                .column("job_title", seq(jobNames), escape)
                .column("min_salary", minSalary + "")
                .column("max_salary", maxSalary + "")
                .table(jobNames.length)
                .val();

        SQLTable departments = sqlInserts()
                .tableName("departments")
                .column("department_id", intSeq().start(0).increment(10))
                .column("department_name",seq(DEPARTMENTS), escape)
                .column("manager_id", constant(""))
                .column("location_id", locations.fromColumn("location_id"))
                .table(DEPARTMENTS.size())
                .val();

        SQLTable employees =
                sqlInserts()
                .tableName("employees")
                .column("employee_id", intSeq())
                .column("first_name", names().first(), escape)
                .column("last_name", names().last(), escape)
                .column("email", "NULL", escape)
                .column("phone_number", regex("\\+30 [0-9]{9}"), escape)
                .column("hire_date",
                        localDates()
                                .past(LocalDate.of(2000, 1, 1))
                                .display(BASIC_ISO_DATE), escape)
                .column("job_id", jobs.fromColumn("job_id"), escape)
                .column("salary", ints().range(minSalary, maxSalary))
                .column("commission_pct", "NULL")
                .column("manager_id", constant(""))
                .column("department_id", departments.fromColumn("department_id"))
                .table(numEmployees)
                .val();

        List<Integer> managersIds =
                employees
                .fromColumn("employee_id")
                .map(Integer::parseInt)
                .list(numManagers)
                .get();

        departments.updateAll((i, insert) -> insert.setValue("manager_id", from(managersIds).get() + ""));

        employees.updateAll((i, insert) -> {
            // Manager id
            Integer employeeId = parseInt(insert.getValue("employee_id"));
            Integer managerId;
            while(employeeId == (managerId = from(managersIds).val()));
            insert.setValue("manager_id", managerId + "");
            // Emails
            String firstName = sanitizeName(insert.getValue("first_name")).toLowerCase();
            String lastName = sanitizeName(insert.getValue("last_name")).toLowerCase();
            String email = firstName + "." + lastName + "@corporation.com";
            insert.setValue("email", email);
        });

        employees.selectFirstWhere(sqlInsert -> sqlInsert.getValue("job_id").equals("D"))
                .get()
                .setValue("manager_id", "NULL");

        System.out.println(schemaCreatePostgreSQL);
        System.out.println(ddlPostgreSQL);
        System.out.println(regions);
        System.out.println(countries);
        System.out.println(jobs);
        System.out.println(locations);
        System.out.println(departments);
        System.out.println(employees);
        System.out.println(foreignKeysMySQL);

        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new hr_schema_postgresql()).execute(args);
        System.exit(exitCode);
    }
}
