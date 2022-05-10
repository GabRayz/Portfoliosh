package fr.gabray.portfoliosh.command.sql.parser;

import fr.gabray.portfoliosh.command.sql.Column;
import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.Table;
import fr.gabray.portfoliosh.command.sql.statement.*;
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
        employees.addColumn(new Column("company_id"), false);
        Table companies = new Table("companies");
        database.addTable(companies);
        companies.addColumn(new Column("id"), true);
        companies.addColumn(new Column("name"), false);

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

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(SelectBuilder.class, builder);
        assertEquals("employees", ((FromBuilder) builder.getSelectable()).getTableName());
        assertEquals(1, builder.getColumns().size());
        assertEquals("id", builder.getColumns().get(0));
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

        SelectBuilder builder = parser.parse(database);

        assertEquals("employees", ((FromBuilder) builder.getSelectable()).getTableName());
        assertEquals(2, builder.getColumns().size());
        assertEquals("id", builder.getColumns().get(0));
        assertEquals("firstname", builder.getColumns().get(1));
    }

    @Test
    void selectAllColumnsTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(FromBuilder.class, builder.getSelectable());
        assertEquals("employees", ((FromBuilder) builder.getSelectable()).getTableName());
        assertTrue(builder.isAll());
    }

    @Test
    void fromOrderByTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees ORDER BY id");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(OrderByBuilder.class, builder.getSelectable());
        assertEquals(1, ((OrderByBuilder) builder.getSelectable()).getColumns().size());
        assertEquals("id", ((OrderByBuilder) builder.getSelectable()).getColumns().get(0));
    }

    @Test
    void fromOrderByDescTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees ORDER BY id DESC");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(OrderByBuilder.class, builder.getSelectable());
        assertEquals(1, ((OrderByBuilder) builder.getSelectable()).getColumns().size());
        assertEquals("id", ((OrderByBuilder) builder.getSelectable()).getColumns().get(0));
        assertTrue(((OrderByBuilder) builder.getSelectable()).isDesc());
    }

    @Test
    void fromMultipleOrderByTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees ORDER BY id, firstname");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(OrderByBuilder.class, builder.getSelectable());
        assertEquals(2, ((OrderByBuilder) builder.getSelectable()).getColumns().size());
        assertEquals("id", ((OrderByBuilder) builder.getSelectable()).getColumns().get(0));
        assertEquals("firstname", ((OrderByBuilder) builder.getSelectable()).getColumns().get(1));
    }

    @Test
    void fromLimitTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees LIMIT 42");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(LimitBuilder.class, builder.getSelectable());
        assertEquals(42, ((LimitBuilder) builder.getSelectable()).count());
    }

    @Test
    void fromOrderByLimitTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees ORDER BY id LIMIT 42");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(LimitBuilder.class, builder.getSelectable());
        assertInstanceOf(OrderByBuilder.class, ((LimitBuilder) builder.getSelectable()).parent());
    }

    @Test
    void whereTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees WHERE id = 1");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(WhereBuilder.class, builder.getSelectable());
        WhereBuilder builder1 = (WhereBuilder) builder.getSelectable();
        assertInstanceOf(WhereLeafCondition.class, builder1.getCondition());
        assertEquals("id", ((WhereLeafCondition) builder1.getCondition()).left());
        assertEquals("1", ((WhereLeafCondition) builder1.getCondition()).right());
    }

    @Test
    void whereIsNullTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees WHERE id is null");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(WhereBuilder.class, builder.getSelectable());
        WhereBuilder builder1 = (WhereBuilder) builder.getSelectable();
        assertInstanceOf(WhereLeafCondition.class, builder1.getCondition());
        assertEquals("id", ((WhereLeafCondition) builder1.getCondition()).left());
        assertEquals(SqlOperator.IS_NULL, ((WhereLeafCondition) builder1.getCondition()).operator());
        assertNull(((WhereLeafCondition) builder1.getCondition()).right());
    }

    @Test
    void whereIsNotNullTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees WHERE id is not null");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(WhereBuilder.class, builder.getSelectable());
        WhereBuilder builder1 = (WhereBuilder) builder.getSelectable();
        assertInstanceOf(WhereLeafCondition.class, builder1.getCondition());
        assertEquals("id", ((WhereLeafCondition) builder1.getCondition()).left());
        assertEquals(SqlOperator.IS_NOT_NULL, ((WhereLeafCondition) builder1.getCondition()).operator());
        assertNull(((WhereLeafCondition) builder1.getCondition()).right());
    }

    @Test
    void whereMultipleTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees WHERE id = 1 AND firstname != John");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(WhereBuilder.class, builder.getSelectable());
        WhereBuilder builder1 = (WhereBuilder) builder.getSelectable();
        assertInstanceOf(WhereNodeCondition.class, builder1.getCondition());
        assertEquals(SqlReservedWord.AND, ((WhereNodeCondition) builder1.getCondition()).operator());
    }

    @Test
    void joinTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees JOIN companies ON employees.company_id = companies.id");

        SelectBuilder builder = parser.parse(database);

        assertInstanceOf(JoinBuilder.class, builder.getSelectable());
        JoinBuilder builder1 = (JoinBuilder) builder.getSelectable();
        assertEquals("companies", builder1.getTableName());
        assertEquals("employees.company_id", builder1.getLeft());
        assertEquals("companies.id", builder1.getRight());
    }
}