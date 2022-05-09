package fr.gabray.portfoliosh.command.sql.parser;

import fr.gabray.portfoliosh.command.sql.Column;
import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.Table;
import fr.gabray.portfoliosh.command.sql.statement.FromBuilder;
import fr.gabray.portfoliosh.command.sql.statement.StatementBuilder;
import fr.gabray.portfoliosh.exception.ParsingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SqlParserTest {

    private static Database database;

    @BeforeAll
    static void beforeAll()
    {
        database = new Database();
        Table employees = new Table("employees");
        database.addTable(employees);
        employees.addColumn(new Column("id"), true);
        employees.addColumn(new Column("firstname"), false);
        employees.addColumn(new Column("lastname"), false);
        employees.addColumn(new Column("age"), false);

        employees.insertObj(Map.of(
                "firstname", "John",
                "lastname", "Smith",
                "age", 22
        ));
        employees.insertObj(Map.of(
                "firstname", "Jack",
                "lastname", "Sparrow",
                "age", 28
        ));
        employees.insertObj(Map.of(
                "firstname", "Obiwan",
                "lastname", "Kennobi",
                "age", 30
        ));
        employees.insertObj(Map.of(
                "firstname", "Darth",
                "lastname", "Vador",
                "age", 25
        ));
    }

    @Test
    void simpleSelectParseTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT id FROM employees");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(FromBuilder.class, builder);
        assertEquals("employees", ((FromBuilder) builder).getTableName());
        assertEquals(1, ((FromBuilder) builder).getSelect().getColumns().size());
        assertEquals("id", ((FromBuilder) builder).getSelect().getColumns().get(0));
    }

    @Test
    void missingSelectColumnShouldThrowTest()
    {
        SqlParser parser = new SqlParser("SELECT FROM employees");

        assertThrows(ParsingException.class, () -> parser.parse(database));
    }

    @Test
    void missingFromColumnTest()
    {
        SqlParser parser = new SqlParser("SELECT * employees");

        assertThrows(ParsingException.class, () -> parser.parse(database));
    }

    @Test
    void selectTwoColumnsTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT id, firstname FROM employees");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(FromBuilder.class, builder);
        assertEquals("employees", ((FromBuilder) builder).getTableName());
        assertEquals(2, ((FromBuilder) builder).getSelect().getColumns().size());
        assertEquals("id", ((FromBuilder) builder).getSelect().getColumns().get(0));
        assertEquals("firstname", ((FromBuilder) builder).getSelect().getColumns().get(1));
    }

    @Test
    void selectAllColumnsTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(FromBuilder.class, builder);
        assertEquals("employees", ((FromBuilder) builder).getTableName());
        assertTrue(((FromBuilder) builder).getSelect().isAll());
    }
}