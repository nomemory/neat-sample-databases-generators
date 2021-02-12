///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.0
//DEPS net.andreinc:mockneat:0.4.5

import net.andreinc.mockneat.abstraction.MockUnitString;
import net.andreinc.mockneat.unit.text.sql.SQLTable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Callable;

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
import static net.andreinc.mockneat.unit.text.sql.escapers.MySQL.TEXT_BACKSLASH;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.user.Emails.emails;
import static net.andreinc.mockneat.unit.user.Names.names;

@Command(
        name = "hr_schema",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Script to generate SQL Inserts for HR Schema"
)
class hr_schema implements Callable<Integer> {

    @Parameters(
            index = "0",
            description = "The greeting to print",
            defaultValue = "World!"
    )
    private String greeting;

    public static void main(String... args) {
        int exitCode = new CommandLine(new hr_schema()).execute(args);
        System.exit(exitCode);
    }


    // Script

    int NUM_COUNTRIES = 241;
    int NUM_LOCATIONS = 100;
    int NUM_EMPLOYEES = 1000;
    int NUM_MANAGERS = 50;
    int MIN_SALARY = 1000;
    int MAX_SALARY = 15000;

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

    public final MockUnitString streets =
            fmt("#{num} #{noun} #{end}")
            .param("num", ints().range(10, 2000))
            .param("noun", words().nouns().format(CAPITALIZED))
            .param("end", from(streetPrefixes));

    public final MockUnitString postalCodes =
            fmt("#{word1} #{word2}")
                    .param("word1", strings().size(3).format(UPPER_CASE))
                    .param("word2", strings().size(3).format(UPPER_CASE));

    @Override
    public Integer call() {

        SQLTable regions =
                sqlInserts()
                .tableName("regions")
                .column("region_id", intSeq().start(1))
                .column("region_name", seq(regionNames), TEXT_BACKSLASH)
                .table(regionNames.length)
                .get();

        SQLTable countries =
                sqlInserts()
                .tableName("countries")
                .column("country_id",  seq(COUNTRY_ISO_CODE_2), TEXT_BACKSLASH)
                .column("country_name", seq(COUNTRY_NAME), TEXT_BACKSLASH)
                .column("region_id", regions.fromColumn("region_id"))
                .table(NUM_COUNTRIES)
                .get();

        SQLTable locations =
                sqlInserts()
                .tableName("locations")
                .column("location_id", intSeq().start(1000).increment(100))
                .column("street_address", streets, TEXT_BACKSLASH)
                .column("postal_code", postalCodes, TEXT_BACKSLASH)
                .column("city", cities().us(), TEXT_BACKSLASH)
                .column("state_province", cities().capitals(), TEXT_BACKSLASH)
                .column("country_id", countries.fromColumn("country_id"), TEXT_BACKSLASH)
                .table(NUM_LOCATIONS)
                .get();

        SQLTable jobs = sqlInserts()
                .tableName("jobs")
                .column("job_id", seq(jobIds), TEXT_BACKSLASH)
                .column("job_title", seq(jobNames), TEXT_BACKSLASH)
                .column("min_salary", MIN_SALARY + "")
                .column("max_salary", MAX_SALARY + "")
                .table(jobNames.length)
                .val();

        SQLTable departments = sqlInserts()
                .tableName("departments")
                .column("department_id", intSeq().start(0).increment(10))
                .column("department_name",seq(DEPARTMENTS), TEXT_BACKSLASH)
                .column("manager_id", constant(""))
                .column("location_id", locations.fromColumn("location_id"))
                .table(DEPARTMENTS.size())
                .val();

        SQLTable employees =
                sqlInserts()
                .tableName("employees")
                .column("employee_id", intSeq())
                .column("first_name", names().first(), TEXT_BACKSLASH)
                .column("last_name", names().last(), TEXT_BACKSLASH)
                .column("email", emails().domain("corp.com"), TEXT_BACKSLASH)
                .column("phone_number", regex("\\+30 [0-9]{9}"), TEXT_BACKSLASH)
                .column("hire_date",
                        localDates()
                                .past(LocalDate.of(2000, 1, 1))
                                .display(BASIC_ISO_DATE), TEXT_BACKSLASH)
                .column("job_id", jobs.fromColumn("job_id"), TEXT_BACKSLASH)
                .column("salary", ints().range(MIN_SALARY, MAX_SALARY))
                .column("commission_pct", "NULL")
                .column("manager_id", constant(""))
                .column("department_id", departments.fromColumn("department_id"))
                .table(NUM_EMPLOYEES)
                .val();

        List<Integer> managersIds =
                employees
                .fromColumn("employee_id")
                .map(Integer::parseInt)
                .list(NUM_MANAGERS)
                .get();

        departments.updateAll((i, insert) -> insert.setValue("manager_id", from(managersIds).get() + ""));

        employees.updateAll((i, insert) -> {
            Integer employeeId = parseInt(insert.getValue("employee_id"));
            Integer managerId;
            while(employeeId == (managerId = from(managersIds).val()));
            insert.setValue("manager_id", managerId + "");
        });

        employees.selectFirstWhere(sqlInsert -> sqlInsert.getValue("job_id").equals("D"))
                .get()
                .setValue("manager_id", "NULL");

        System.out.println(regions);
        System.out.println(countries);
        System.out.println(jobs);
        System.out.println(locations);
        System.out.println(departments);
        System.out.println(employees);

        return 0;
    }
}
