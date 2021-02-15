///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.1
//DEPS net.andreinc:mockneat:0.4.5

import net.andreinc.mockneat.abstraction.MockUnitString;
import net.andreinc.mockneat.unit.text.sql.SQLTable;
import net.andreinc.mockneat.unit.text.sql.escapers.MySQL;
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
        description = "Script to generate SQL Inserts for HR Schema (MySQL|MariaDB)"
)
class hr_schema_mysql implements Callable<Integer> {

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
            "St",
            "Blvd",
            "Rd"
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

    String schemaCreateMySQL =
            "DROP SCHEMA IF EXISTS hr;\n" +
            "CREATE SCHEMA hr COLLATE = utf8_general_ci;\n" +
            "USE hr;";

    String ddlMySQL =
            "CREATE TABLE regions (\n" +
                    "\tregion_id INT (11) UNSIGNED NOT NULL,\n" +
                    "\tregion_name VARCHAR(25),\n" +
                    "\tPRIMARY KEY (region_id)\n" +
                    "\t);\n" +
                    "\n" +
            "CREATE TABLE countries (\n" +
                    "\tcountry_id CHAR(2) NOT NULL,\n" +
                    "\tcountry_name VARCHAR(128),\n" +
                    "\tregion_id INT (11) UNSIGNED NOT NULL,\n" +
                    "\tPRIMARY KEY (country_id)\n" +
                    ");\n" +
                    "\n" +
                    "\n" +
            "CREATE TABLE locations (\n" +
                    "\tlocation_id INT (11) UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                    "\tstreet_address VARCHAR(40),\n" +
                    "\tpostal_code VARCHAR(12),\n" +
                    "\tcity VARCHAR(30) NOT NULL,\n" +
                    "\tstate_province VARCHAR(25),\n" +
                    "\tcountry_id CHAR(2) NOT NULL,\n" +
                    "\tPRIMARY KEY (location_id)\n" +
                    "\t);\n" +
                    "\n" +
            "CREATE TABLE departments (\n" +
                    "\tdepartment_id INT (11) UNSIGNED NOT NULL,\n" +
                    "\tdepartment_name VARCHAR(30) NOT NULL,\n" +
                    "\tmanager_id INT (11) UNSIGNED,\n" +
                    "\tlocation_id INT (11) UNSIGNED,\n" +
                    "\tPRIMARY KEY (department_id)\n" +
                    "\t);\n" +
                    "\n" +
            "CREATE TABLE jobs (\n" +
                    "\tjob_id VARCHAR(10) NOT NULL,\n" +
                    "\tjob_title VARCHAR(35) NOT NULL,\n" +
                    "\tmin_salary DECIMAL(8, 0) UNSIGNED,\n" +
                    "\tmax_salary DECIMAL(8, 0) UNSIGNED,\n" +
                    "\tPRIMARY KEY (job_id)\n" +
                    "\t);\n" +
                    "\n" +
            "CREATE TABLE employees (\n" +
                    "\temployee_id INT (11) UNSIGNED NOT NULL,\n" +
                    "\tfirst_name VARCHAR(20),\n" +
                    "\tlast_name VARCHAR(25) NOT NULL,\n" +
                    "\temail VARCHAR(128) NOT NULL,\n" +
                    "\tphone_number VARCHAR(20),\n" +
                    "\thire_date DATE NOT NULL,\n" +
                    "\tjob_id VARCHAR(10) NOT NULL,\n" +
                    "\tsalary DECIMAL(8, 2) NOT NULL,\n" +
                    "\tcommission_pct DECIMAL(2, 2),\n" +
                    "\tmanager_id INT (11) UNSIGNED,\n" +
                    "\tdepartment_id INT (11) UNSIGNED,\n" +
                    "\tPRIMARY KEY (employee_id)\n" +
                    "\t);\n" +
                    "\n" +
            "CREATE TABLE job_history (\n" +
                    "\temployee_id INT (11) UNSIGNED NOT NULL,\n" +
                    "\tstart_date DATE NOT NULL,\n" +
                    "\tend_date DATE NOT NULL,\n" +
                    "\tjob_id VARCHAR(10) NOT NULL,\n" +
                    "\tdepartment_id INT (11) UNSIGNED NOT NULL\n" +
                    "\t);\n" +
                    "\n" +
            "ALTER TABLE job_history ADD UNIQUE INDEX (\n" +
                    "\temployee_id,\n" +
                    "\tstart_date\n" +
                    "\t);\n" +
                    "\n" +
                    "\n" +
            "CREATE VIEW emp_details_view\n" +
                    "AS\n" +
                    "SELECT e.employee_id,\n" +
                    "\te.job_id,\n" +
                    "\te.manager_id,\n" +
                    "\te.department_id,\n" +
                    "\td.location_id,\n" +
                    "\tl.country_id,\n" +
                    "\te.first_name,\n" +
                    "\te.last_name,\n" +
                    "\te.salary,\n" +
                    "\te.commission_pct,\n" +
                    "\td.department_name,\n" +
                    "\tj.job_title,\n" +
                    "\tl.city,\n" +
                    "\tl.state_province,\n" +
                    "\tc.country_name,\n" +
                    "\tr.region_name\n" +
                    "FROM employees e,\n" +
                    "\tdepartments d,\n" +
                    "\tjobs j,\n" +
                    "\tlocations l,\n" +
                    "\tcountries c,\n" +
                    "\tregions r\n" +
                    "WHERE e.department_id = d.department_id\n" +
                    "\tAND d.location_id = l.location_id\n" +
                    "\tAND l.country_id = c.country_id\n" +
                    "\tAND c.region_id = r.region_id\n" +
                    "\tAND j.job_id = e.job_id;";

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

        if (!Set.of("mysql", "mariadb", "postgresql").contains(target)) {
            throw new IllegalArgumentException("Invalid target: " + target + ". Accepted values: 'postgresql' or 'mysql'");
        }
    }

    @Override
    public Integer call() {

        validateCall();

        Function<String, String> escape = MySQL.TEXT_BACKSLASH;

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

        System.out.println(schemaCreateMySQL);
        System.out.println(ddlMySQL);
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
        int exitCode = new CommandLine(new hr_schema_mysql()).execute(args);
        System.exit(exitCode);
    }
}
