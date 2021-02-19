///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.1
//DEPS net.andreinc:mockneat:0.4.6

import net.andreinc.mockneat.abstraction.MockUnit;
import net.andreinc.mockneat.abstraction.MockUnitString;
import net.andreinc.mockneat.types.enums.StringType;
import net.andreinc.mockneat.unit.text.sql.SQLTable;
import net.andreinc.mockneat.unit.text.sql.escapers.MySQL;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.temporal.ChronoUnit.DAYS;
import static net.andreinc.mockneat.types.enums.DictType.*;
import static net.andreinc.mockneat.types.enums.StringFormatType.CAPITALIZED;
import static net.andreinc.mockneat.types.enums.StringFormatType.UPPER_CASE;
import static net.andreinc.mockneat.types.enums.StringType.ALPHA_NUMERIC;
import static net.andreinc.mockneat.unit.address.Cities.cities;
import static net.andreinc.mockneat.unit.address.Countries.countries;
import static net.andreinc.mockneat.unit.address.USStates.usStates;
import static net.andreinc.mockneat.unit.misc.Cars.cars;
import static net.andreinc.mockneat.unit.objects.Constant.constant;
import static net.andreinc.mockneat.unit.objects.From.from;
import static net.andreinc.mockneat.unit.objects.From.fromInts;
import static net.andreinc.mockneat.unit.objects.Probabilities.probabilities;
import static net.andreinc.mockneat.unit.regex.Regex.regex;
import static net.andreinc.mockneat.unit.seq.IntSeq.intSeq;
import static net.andreinc.mockneat.unit.seq.Seq.seq;
import static net.andreinc.mockneat.unit.text.Formatter.fmt;
import static net.andreinc.mockneat.unit.text.Markovs.markovs;
import static net.andreinc.mockneat.unit.text.SQLInserts.sqlInserts;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.text.Words.words;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.types.Bools.bools;
import static net.andreinc.mockneat.unit.types.Chars.chars;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.types.Longs.longs;
import static net.andreinc.mockneat.unit.user.Names.names;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

@Command(
        name = "classicmodels_mysql",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Script to generate SQL Inserts for classicmodels schema (MySQL|mariadb)"
)
class classicmodels_mysql implements Callable<Integer> {

    @CommandLine.Option(
            names = { "-c", "--customers" },
            description = "The number of customers to be generated",
            defaultValue = "1000",
            showDefaultValue = ALWAYS

    )
    int numCustomers = 1000;

    @CommandLine.Option(
            names = { "-e", "--employees" },
            description = "The number of sales employees to be generated",
            defaultValue = "1000",
            showDefaultValue = ALWAYS

    )
    int numEmployees = 1000;

    @CommandLine.Option(
            names = { "-o", "--offices" },
            description = "The number of offices to be generated",
            defaultValue = "100",
            showDefaultValue = ALWAYS

    )
    int numOffices = 100;

    @CommandLine.Option(
            names = { "-s", "--vps-sales" },
            description = "The number of VP Sales to be generated",
            defaultValue = "5",
            showDefaultValue = ALWAYS

    )
    int numVpsSales = 5;

    @CommandLine.Option(
            names = { "-m", "--vps-marketing" },
            description = "The number of VP Marketing to be generated",
            defaultValue = "5",
            showDefaultValue = ALWAYS

    )
    int numVpsMarketing = 5;

    @CommandLine.Option(
            names = { "-g", "--sales-managers" },
            description = "The number of Sales Managers to be generated",
            defaultValue = "10",
            showDefaultValue = ALWAYS

    )
    int numSalesManagers = 10;

    @CommandLine.Option(
            names = { "-p", "--products" },
            description = "The number of Products to be generated",
            defaultValue = "1000",
            showDefaultValue = ALWAYS

    )
    int numProducts = 1000;

    @CommandLine.Option(
            names = { "-d", "--orders" },
            description = "The number of orders to be generated",
            defaultValue = "1500",
            showDefaultValue = ALWAYS

    )
    int numOrders = 1500;

    @CommandLine.Option(
            names = { "-y", "--payments]" },
            description = "The number of payments to be generated",
            defaultValue = "100",
            showDefaultValue = ALWAYS

    )
    int numPayments = 100;

    String schemaCreateMySQL =
            "DROP SCHEMA IF EXISTS classicmodels;\n" +
            "CREATE SCHEMA classicmodels COLLATE = utf8_general_ci;\n" +
            "USE classicmodels;";

    String ddlMySQL =
            "CREATE TABLE `customers` (\n" +
            "  `customerNumber` int(11) NOT NULL,\n" +
            "  `customerName` varchar(256) NOT NULL,\n" +
            "  `contactLastName` varchar(50) NOT NULL,\n" +
            "  `contactFirstName` varchar(50) NOT NULL,\n" +
            "  `phone` varchar(50) NOT NULL,\n" +
            "  `addressLine1` varchar(50) NOT NULL,\n" +
            "  `addressLine2` varchar(50) DEFAULT NULL,\n" +
            "  `city` varchar(50) NOT NULL,\n" +
            "  `state` varchar(50) DEFAULT NULL,\n" +
            "  `postalCode` varchar(15) DEFAULT NULL,\n" +
            "  `country` varchar(128) NOT NULL,\n" +
            "  `salesRepEmployeeNumber` int(11) DEFAULT NULL,\n" +
            "  `creditLimit` decimal(10,2) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`customerNumber`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
            "\n" +
            "CREATE TABLE `employees` (\n" +
            "  `employeeNumber` int(11) NOT NULL,\n" +
            "  `lastName` varchar(50) NOT NULL,\n" +
            "  `firstName` varchar(50) NOT NULL,\n" +
            "  `extension` varchar(10) NOT NULL,\n" +
            "  `email` varchar(100) NOT NULL,\n" +
            "  `officeCode` varchar(10) NOT NULL,\n" +
            "  `reportsTo` int(11) DEFAULT NULL,\n" +
            "  `jobTitle` varchar(50) NOT NULL,\n" +
            "  PRIMARY KEY (`employeeNumber`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
            "\n" +
            "CREATE TABLE `offices` (\n" +
            "  `officeCode` varchar(10) NOT NULL,\n" +
            "  `city` varchar(50) NOT NULL,\n" +
            "  `phone` varchar(50) NOT NULL,\n" +
            "  `addressLine1` varchar(50) NOT NULL,\n" +
            "  `addressLine2` varchar(50) DEFAULT NULL,\n" +
            "  `state` varchar(50) DEFAULT NULL,\n" +
            "  `country` varchar(50) NOT NULL,\n" +
            "  `postalCode` varchar(15) NOT NULL,\n" +
            "  `territory` varchar(10) NOT NULL,\n" +
            "  PRIMARY KEY (`officeCode`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
            "\n" +
            "CREATE TABLE `orders` (\n" +
            "  `orderNumber` int(11) NOT NULL,\n" +
            "  `orderDate` date NOT NULL,\n" +
            "  `requiredDate` date NOT NULL,\n" +
            "  `shippedDate` date DEFAULT NULL,\n" +
            "  `status` varchar(15) NOT NULL,\n" +
            "  `comments` text,\n" +
            "  `customerNumber` int(11) NOT NULL,\n" +
            "  PRIMARY KEY (`orderNumber`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
            "\n" +
            "CREATE TABLE `payments` (\n" +
            "  `customerNumber` int(11) NOT NULL,\n" +
            "  `checkNumber` varchar(50) NOT NULL,\n" +
            "  `paymentDate` date NOT NULL,\n" +
            "  `amount` decimal(10,2) NOT NULL,\n" +
            "  PRIMARY KEY (`customerNumber`,`checkNumber`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
            "\n" +
            "CREATE TABLE `productlines` (\n" +
            "  `productLine` varchar(50) NOT NULL,\n" +
            "  `textDescription` varchar(4000) DEFAULT NULL,\n" +
            "  `htmlDescription` mediumtext,\n" +
            "  `image` mediumblob,\n" +
            "  PRIMARY KEY (`productLine`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
            "\n" +
            "CREATE TABLE `products` (\n" +
            "  `productCode` varchar(15) NOT NULL,\n" +
            "  `productName` varchar(70) NOT NULL,\n" +
            "  `productLine` varchar(50) NOT NULL,\n" +
            "  `productScale` varchar(10) NOT NULL,\n" +
            "  `productVendor` varchar(50) NOT NULL,\n" +
            "  `productDescription` text NOT NULL,\n" +
            "  `quantityInStock` smallint(6) NOT NULL,\n" +
            "  `buyPrice` decimal(10,2) NOT NULL,\n" +
            "  `MSRP` decimal(10,2) NOT NULL,\n" +
            "  PRIMARY KEY (`productCode`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n" +
            "CREATE TABLE `orderdetails` (\n" +
            "  `orderNumber` int(11) NOT NULL,\n" +
            "  `productCode` varchar(15) NOT NULL,\n" +
            "  `quantityOrdered` int(11) NOT NULL,\n" +
            "  `priceEach` decimal(10,2) NOT NULL,\n" +
            "  `orderLineNumber` smallint(6) NOT NULL,\n" +
            "  PRIMARY KEY (`orderNumber`,`productCode`),\n" +
            "  KEY `productCode` (`productCode`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

    String foreignKeysMySQL =
            "ALTER TABLE customers ADD FOREIGN KEY (salesRepEmployeeNumber) REFERENCES employees(employeeNumber);\n" +
            "ALTER TABLE employees ADD FOREIGN KEY (reportsTo) REFERENCES employees(employeeNumber);\n" +
            "ALTER TABLE employees ADD FOREIGN KEY (officeCode) REFERENCES offices(officeCode);\n" +
            "ALTER TABLE orders ADD FOREIGN KEY (customerNumber) REFERENCES customers(customerNumber);\n" +
            "ALTER TABLE payments ADD FOREIGN KEY (customerNumber) REFERENCES customers(customerNumber);\n" +
            "ALTER TABLE products ADD FOREIGN KEY (productLine) REFERENCES productlines(productLine);\n" +
            "ALTER TABLE orderdetails ADD FOREIGN KEY(orderNumber) REFERENCES orders(orderNumber);\n" +
            "ALTER TABLE orderdetails ADD FOREIGN KEY(productCode) REFERENCES products(productCode);\n";

    String[] customerNameSuff = {
            ", Co.",
            ", Ltd.",
            "&Co",
            "Inc.",
            "Co.",
            "Imports",
            "Network",
            "Garage"
    };

    String[] dealerships = {
            "Auto",
            "Auto-Moto",
            "Auto-moto",
            "Mini Wheels",
            "Fast Track",
            "AutoHaven",
            "EncoreAutos",
            "DriVinci",
            "Eminent Cars",
            "Stark Auto",
            "Voila-Car",
            "Thrifty Wheels",
            "Motor Mint",
            "Warp",
            "Engines"
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

    String[] addressLine2Prefix = new String[]{
            "Line",
            "Suite",
            "Apartment"
    };

    String[] territories = new String[]{
            "NA",
            "EMEA",
            "Japan",
            "APAC",
            "SA"
    };

    String[] productLine = new String[]{
            "Classic Cars",
            "Motorcycles",
            "Planes",
            "Ships",
            "Trains",
            "Trucks and Buses",
            "Vintage Cars"
    };

    String[] productLineDesc = new String[] {
            "Attention car enthusiasts: Make your wildest car ownership dreams come true. Whether you are looking for classic muscle cars, dream sports cars or movie-inspired miniatures, you will find great choices in this category. These replicas feature superb attention to detail and craftsmanship and offer features such as working steering system, opening forward compartment, opening rear trunk with removable spare wheel, 4-wheel independent spring suspension, and so on. The models range in size from 1:10 to 1:24 scale and include numerous limited edition and several out-of-production vehicles. All models include a certificate of authenticity from their manufacturers and come fully assembled and ready for display in the home or office.",
            "Our motorcycles are state of the art replicas of classic as well as contemporary motorcycle legends such as Harley Davidson, Ducati and Vespa. Models contain stunning details such as official logos, rotating wheels, working kickstand, front suspension, gear-shift lever, footbrake lever, and drive chain. Materials used include diecast and plastic. The models range in size from 1:10 to 1:50 scale and include numerous limited edition and several out-of-production vehicles. All models come fully assembled and ready for display in the home or office. Most include a certificate of authenticity.",
            "Unique, diecast airplane and helicopter replicas suitable for collections, as well as home, office or classroom decorations. Models contain stunning details such as official logos and insignias, rotating jet engines and propellers, retractable wheels, and so on. Most come fully assembled and with a certificate of authenticity from their manufacturers.",
            "The perfect holiday or anniversary gift for executives, clients, friends, and family. These handcrafted model ships are unique, stunning works of art that will be treasured for generations! They come fully assembled and ready for display in the home or office. We guarantee the highest quality, and best value.",
            "Model trains are a rewarding hobby for enthusiasts of all ages. Whether you're looking for collectible wooden trains, electric streetcars or locomotives, you'll find a number of great choices for any budget within this category. The interactive aspect of trains makes toy trains perfect for young children. The wooden train sets are ideal for children under the age of 5.",
            "The Truck and Bus models are realistic replicas of buses and specialized trucks produced from the early 1920s to present. The models range in size from 1:12 to 1:50 scale and include numerous limited edition and several out-of-production vehicles. Materials used include tin, diecast and plastic. All models include a certificate of authenticity from their manufacturers and are a perfect ornament for the home and office.",
            "Our Vintage Car models realistically portray automobiles produced from the early 1900s through the 1940s. Materials used include Bakelite, diecast, plastic and wood. Most of the replicas are in the 1:18 and 1:24 scale sizes, which provide the optimum in detail and accuracy. Prices range from $30.00 up to $180.00 for some special limited edition replicas. All models include a certificate of authenticity from their manufacturers and come fully assembled and ready for display in the home or office."
    };

    String[] productVendorSuffix = new String[] {
            "Collectibles",
            "Classics",
            "Galleries",
            "Design",
            "Diecast",
            "Mini Classics",
            "Legends",
            "Mini Legends"
    };

    String[] productVendorPrefix = new String[] {
            "Gearbox",
            "Red",
            "Studio",
            "Creative",
            "Highway 66",
            "Autoart",
            "Motor",
            "Carousel",
            "Iron",
            "Silver",
            "Toy Toy",
            "V8",
            "Engines"
    };

    String[] orderComments = new String[] {
            "Check on availability.",
            "Difficult to negotiate with customer. We need more marketing materials",
            "Customer requested that FedEx Ground is used for this shipping",
            "Customer requested that ad materials (such as posters, pamphlets) be included in the shippment",
            "Customer has worked with some of our vendors in the past and is aware of their MSRP",
            "Customer very concerned about the exact color of the models. There is high risk that he may dispute the order because there is a slight color mismatch",
            "Customer requested special shippment. The instructions were passed along to the warehouse",
            "Customer is interested in buying more Ferrari models",
            "Can we deliver the new Ford Mustang models by end-of-quarter?",
            "They want to reevaluate their terms agreement with Finance.",
            "This order was disputed, but resolved on 11/1/2003; Customer doesn't like the colors and precision of the models.",
            "This order was on hold because customers's credit limit had been exceeded. Order will ship when payment is received",
            "Customer called to cancel. The warehouse was notified in time and the order didn't ship. They have a new VP of Sales and are shifting their sales model. Our VP of Sales should contact them.",
            "Cautious optimism. We have happy customers here, if we can keep them well stocked.  I need all the information I can get on the planned shippments of Porches",
            "Custom shipping instructions sent to warehouse",
            "Customer cancelled due to urgent budgeting issues. Must be cautious when dealing with them in the future. Since order shipped already we must discuss who would cover the shipping charges.",
            "We need to keep in close contact with their Marketing VP. He is the decision maker for all their purchases.",
            "They want to reevaluate their terms agreement with the VP of Sales",
            "They want to reevaluate their terms agreement with Finance.",
            "We must be cautions with this customer. Their VP of Sales resigned. Company may be heading down.",
            "Customer inquired about remote controlled models and gold models.",
            "I need all the information I can get on our competitors.",
            "Can we renegotiate this one?"
    };

    public static String sanitizeName(String text) {
        return text == null ? null :
                Normalizer
                        .normalize(text, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("[^A-Za-z ]", "");
    }

    public void validateCall() {
    }

    @Override
    public Integer call() {

        validateCall();

        Function<String, String> escAsStr = MySQL.TEXT_BACKSLASH;

        Function<String, String> escAsStrOrNull = str -> {
            if (str.equals("NULL"))
                return str;
            return MySQL.TEXT_BACKSLASH.apply(str);
        };

        List<Integer> employeeIds = intSeq().increment(10).list(numEmployees).get();
        List<Integer> officesIds = intSeq().increment(100).list(numOffices).get();
        Set<Integer> vpSalesIds = from(employeeIds).set(numVpsSales).get();
        Set<Integer> vpMarketingIds = from(employeeIds).set(numVpsMarketing).get();
        Set<Integer> salesManager = from(employeeIds).set(numSalesManagers).get();
        List<Integer> allManagersIds = new ArrayList<>();

        Integer presidentId = from(employeeIds).get();
        allManagersIds.addAll(vpSalesIds);
        allManagersIds.addAll(vpMarketingIds);
        allManagersIds.addAll(salesManager);

        MockUnitString customerName =
                probabilities(String.class)
                    .add(0.2,
                            fmt("#{name1}, #{name2} and #{name3} #{suff}")
                                    .param("name1", names().last())
                                    .param("name2", names().last())
                                    .param("name3", names().last())
                                    .param("suff", from(customerNameSuff))
                    )
                    .add(0.3,
                            fmt("#{pref} #{brand} #{geo}")
                                .param("pref", from(dealerships))
                                .param("brand", cars().brands())
                                .param("geo",
                                        probabilities(String.class)
                                            .add(0.5, countries().iso2())
                                            .add(0.2, countries())
                                            .add(0.15, cities().us())
                                            .add(0.15, cities().capitalsEurope())
                                )
                    )
                    .add(0.2,
                            fmt("#{name1} & #{name2} #{suff}")
                                .param("name1", names().full(0.2))
                                .param("name2", names().full())
                                .param("suff", from(customerNameSuff))
                    )
                    .add(0.3,
                            fmt("#{l1}.#{l2}.#{l3} #{dl} #{suff}")
                                .param("l1", chars().upperLetters())
                                .param("l2", chars().upperLetters())
                                .param("l3", chars().upperLetters())
                                .param("dl", from(dealerships))
                                .param("suff", from(customerNameSuff))
                    )
                    .mapToString();

        MockUnitString phone =
                probabilities(String.class)
                    .add(0.5,
                        fmt("(#{pref}) #{num1}-#{num2}-#{num3}")
                            .param("pref", ints().range(10, 99).mapToString().prepend("+"))
                            .param("num1", ints().range(100, 999))
                            .param("num2", ints().range(100, 999))
                            .param("num3", ints().range(1000, 9999))
                    )
                    .add(0.5,
                        fmt("#{pref} #{num1}")
                            .param("pref", ints().range(10, 99).mapToString().prepend("0"))
                            .param("num1", ints().range(100000, 99999999))
                    )
                    .mapToString();

        MockUnitString addressLine1 =
                fmt("#{num} #{noun} #{end}")
                        .param("num", ints().range(10, 20000))
                        .param("noun", words().nouns().format(CAPITALIZED))
                        .param("end", from(streetPrefixes));

        MockUnitString addressLine2 =
                probabilities(String.class)
                    .add(0.8, constant("NULL"))
                    .add(0.2,
                            fmt("#{prefix} #{number}")
                                    .param("prefix", from(addressLine2Prefix))
                                    .param("number", ints().range(1, 1000))
                    )
                    .mapToString();

        MockUnitString state =
                probabilities(String.class)
                    .add(0.8, constant("NULL"))
                    .add(0.1, usStates())
                    .add(0.1, cities().us())
                    .mapToString();

        MockUnitString postalCode =
                probabilities(String.class)
                    .add(0.5, ints().range(10000, 99999).mapToString())
                    .add(0.5, strings().size(5).type(ALPHA_NUMERIC))
                    .mapToString();

        SQLTable customers =
                sqlInserts()
                        .tableName("customers")
                        .column("customerNumber", intSeq().start(10).increment(10))
                        .column("customerName", customerName, escAsStr)
                        .column("contactLastName", names().last(), escAsStr)
                        .column("contactFirstName", names().first(), escAsStr)
                        .column("phone", phone, escAsStr)
                        .column("addressLine1", addressLine1, escAsStr)
                        .column("addressLine2", addressLine2, escAsStrOrNull)
                        .column("city", cities().capitals(), escAsStr)
                        .column("state", state, escAsStrOrNull)
                        .column("country", countries(), escAsStr)
                        .column("salesRepEmployeeNumber", from(employeeIds))
                        .column("creditLimit", ints().range(0, 1_000_000))
                        .table(numCustomers)
                        .get();

        customers.updateAll((i, insert) -> {
            if (probabilities(Boolean.class)
                    .add(0.1, true)
                    .add(0.9, false)
                    .get()) {
                insert.setValue("salesRepEmployeeNumber", "NULL");
            }
        });

        SQLTable employees =
                sqlInserts()
                    .tableName("employees")
                    .column("employeeNumber", seq(employeeIds))
                    .column("lastName", names().last(), escAsStr)
                    .column("firstName", names().first(), escAsStr)
                    .column("extension",
                            ints()
                            .range(100, 99999999)
                            .mapToString()
                            .prepend("x")
                            ,
                            escAsStr
                    )
                    .column("email", constant("NULL"))
                    .column("officeCode", from(officesIds))
                    .column("reportsTo", from(allManagersIds))
                    .column("jobTitle", constant("Sales Rep"), escAsStr)
                    .table(employeeIds.size())
                    .get();

        // Generate nice fname.lame@classicmodels.com emails
        employees.updateAll( (i, insert) -> {
            String lastName = sanitizeName(insert.getValue("lastName"));
            String firstName = sanitizeName(insert.getValue("firstName"));
            String email = firstName + "." + lastName + "@classicmodels.com";
            insert.setValue("email", escAsStr.apply(email));
        });

        employees.updateAll( (i, insert) -> {
            Integer id = parseInt(insert.getValue("employeeNumber"));
            if (vpSalesIds.contains(id)) {
                insert.setValue("jobTitle", escAsStr.apply("VP Sales"));
                insert.setValue("reportsTo", presidentId+"");
            }
            if (vpMarketingIds.contains(id)) {
                insert.setValue("jobTitle", escAsStr.apply("VP Marketing"));
                insert.setValue("reportsTo", presidentId+"");
            }
            if (salesManager.contains(id)) {
                insert.setValue("jobTitle", escAsStr.apply("Sales Manager"));
                insert.setValue("reportsTo", presidentId+"");
            }
            if (id.equals(presidentId)) {
                insert.setValue("jobTitle", escAsStr.apply("President"));
                insert.setValue("reportsTo", "NULL");
            }
        });

        SQLTable offices =
                sqlInserts()
                        .tableName("offices")
                        .column("officeCode", seq(officesIds))
                        .column("city", cities().capitals(), escAsStr)
                        .column("phone", phone, escAsStr)
                        .column("addressLine1", addressLine1, escAsStr)
                        .column("addressLine2", addressLine2, escAsStrOrNull)
                        .column("state", state, escAsStrOrNull)
                        .column("country", countries(), escAsStr)
                        .column("postalCode", postalCode, escAsStr)
                        .column("territory", from(territories), escAsStr)
                        .table(officesIds.size())
                        .get();

        SQLTable productLines =
                sqlInserts()
                    .tableName("productlines")
                    .column("productLine", seq(productLine), escAsStr)
                    .column("textDescription", seq(productLineDesc), escAsStr)
                    .column("htmlDescription", constant("NULL"))
                    .column("image", constant("NULL"))
                    .table(productLine.length)
                    .get();

        List<String> prodCodePreffixes =
                fmt("#{L}#{NUM}")
                .param("L", chars().upperLetters())
                .param("NUM", ints().range(1, 99))
                .set(100)
                .get()
                .stream()
                .collect(Collectors.toList());

        SQLTable products =
                sqlInserts()
                    .tableName("products")
                    .column("productCode",
                            fmt("#{pref}_#{num}")
                            .param("pref", from(prodCodePreffixes))
                            .param("num", intSeq().increment(10))
                            , escAsStr
                    )
                    .column("productName",
                            fmt("#{year} #{car}")
                            .param("year", ints().range(1920, 2050))
                            .param("car", cars())
                            , escAsStr
                    )
                    .column("productLine", from(productLine), escAsStr)
                    .column("productScale",
                            fromInts(new int[]{10, 12, 18, 24, 32, 50, 72, 700})
                            .mapToString().prepend("1:")
                            , escAsStr
                    )
                    .column("productVendor",
                            fmt("#{pref} #{suf}")
                            .param("pref", from(productVendorPrefix))
                            .param("suf", from(productVendorSuffix))
                            , escAsStr
                    )
                    .column("productDescription", markovs().loremIpsum().sub(32), escAsStr)
                    .column("quantityInStock", ints().range(0, 10000))
                    .column("buyPrice", ints().range(5, 10000))
                    .column("MSRP", ints().range(0, 250))
                    .table(numProducts)
                    .get();

        MockUnitString shipmentStatus =
                probabilities(String.class)
                .add(0.05, "Cancelled")
                .add(0.05, "Disputed")
                .add(0.1, "In Process")
                .add(0.05, "On Hold")
                .add(0.05, "Resolved")
                .add(0.7, "Shipped")
                .mapToString();

        List<Integer> ordersIds = intSeq().start(1000).increment(10).list(numOrders).get();

        SQLTable orders =
                sqlInserts()
                    .tableName("orders")
                    .column("orderNumber", seq(ordersIds))
                    .column("orderDate", constant("NULL"))
                    .column("shippedDate", constant("NULL"))
                    .column("requiredDate", constant("NULL"))
                    .column("status", shipmentStatus, escAsStr)
                    .column("comments",
                            probabilities(String.class)
                            .add(0.2, from(orderComments))
                            .add(0.8, "NULL")
                            , escAsStrOrNull
                    )
                    .column("customerNumber", customers.fromColumn("customerNumber"))
                    .table(numOrders)
                    .get();

        orders.updateAll((i, row) -> {
            String status = row.getValue("status");

            LocalDate orderDate = localDates().thisYear().get();
            LocalDate requiredDate = orderDate.plus(longs().range(10l, 28l).get(), DAYS);
            LocalDate shippedDate = orderDate.plus(longs().range(2l, 8l).get(), DAYS);

            row.setValue("orderDate", escAsStr.apply(orderDate.format(ISO_DATE)));
            row.setValue("shippedDate", escAsStr.apply(shippedDate.format(ISO_DATE)));
            row.setValue("requiredDate", escAsStr.apply(requiredDate.format(ISO_DATE)));

            if ("Cancelled".equals(status) && bools().get()) {
                row.setValue("shippedDate", "NULL");
            }
            else if ("In Process".equals(status) || "On Hold".equals("status")) {
                row.setValue("shippedDate", "NULL");
            }
        });

        SQLTable orderdetails =
                sqlInserts()
                .tableName("orderdetails")
                .column("orderNumber", seq(ordersIds).cycle(true))
                .column("productCode", products.fromColumn("productCode"), escAsStr)
                .column("quantityOrdered", ints().range(1, 128))
                .column("priceEach", ints().range(5, 10000))
                .column("orderLineNumber", ints().range(1,24))
                .table(numOrders)
                .get();


        SQLTable payments =
                sqlInserts()
                .tableName("payments")
                .column("customerNumber", customers.fromColumn("customerNumber"))
                .column("checkNumber", strings().size(8).type(ALPHA_NUMERIC).format(UPPER_CASE), escAsStr)
                .column("paymentDate", localDates().thisYear().display(ISO_DATE), escAsStr)
                .column("amount", ints().range(100, 99999))
                .table(numPayments)
                .get();

        System.out.println(schemaCreateMySQL);
        System.out.println(ddlMySQL);
        System.out.println(customers);
        System.out.println(employees);
        System.out.println(offices);
        System.out.println(productLines);
        System.out.println(products);
        System.out.println(orders);
        System.out.println(orderdetails);
        System.out.println(payments);
        System.out.println(foreignKeysMySQL);

        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new classicmodels_mysql()).execute(args);
        System.exit(exitCode);
    }
}
