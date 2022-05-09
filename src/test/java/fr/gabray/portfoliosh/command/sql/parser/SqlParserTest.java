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

    @Test
    void fromOrderByTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees ORDER BY id");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(OrderByBuilder.class, builder);
        assertEquals(1, ((OrderByBuilder) builder).getColumns().size());
        assertEquals("id", ((OrderByBuilder) builder).getColumns().get(0));
    }

    @Test
    void fromOrderByDescTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees ORDER BY id DESC");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(OrderByBuilder.class, builder);
        assertEquals(1, ((OrderByBuilder) builder).getColumns().size());
        assertEquals("id", ((OrderByBuilder) builder).getColumns().get(0));
        assertTrue(((OrderByBuilder) builder).isDesc());
    }

    @Test
    void fromMultipleOrderByTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees ORDER BY id, firstname");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(OrderByBuilder.class, builder);
        assertEquals(2, ((OrderByBuilder) builder).getColumns().size());
        assertEquals("id", ((OrderByBuilder) builder).getColumns().get(0));
        assertEquals("firstname", ((OrderByBuilder) builder).getColumns().get(1));
    }

    @Test
    void fromLimitTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees LIMIT 42");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(LimitBuilder.class, builder);
        assertEquals(42, ((LimitBuilder) builder).count());
    }

    @Test
    void fromOrderByLimitTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees ORDER BY id LIMIT 42");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(LimitBuilder.class, builder);
        assertInstanceOf(OrderByBuilder.class, ((LimitBuilder) builder).parent());
    }

    @Test
    void whereTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees WHERE id = 1");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(WhereBuilder.class, builder);
        WhereBuilder builder1 = (WhereBuilder) builder;
        assertInstanceOf(WhereLeafCondition.class, builder1.getCondition());
        assertEquals("id", ((WhereLeafCondition) builder1.getCondition()).left());
        assertEquals("1", ((WhereLeafCondition) builder1.getCondition()).right());
    }

    @Test
    void whereIsNullTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees WHERE id is null");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(WhereBuilder.class, builder);
        WhereBuilder builder1 = (WhereBuilder) builder;
        assertInstanceOf(WhereLeafCondition.class, builder1.getCondition());
        assertEquals("id", ((WhereLeafCondition) builder1.getCondition()).left());
        assertEquals(SqlOperator.IS_NULL, ((WhereLeafCondition) builder1.getCondition()).operator());
        assertNull(((WhereLeafCondition) builder1.getCondition()).right());
    }

    @Test
    void whereIsNotNullTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees WHERE id is not null");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(WhereBuilder.class, builder);
        WhereBuilder builder1 = (WhereBuilder) builder;
        assertInstanceOf(WhereLeafCondition.class, builder1.getCondition());
        assertEquals("id", ((WhereLeafCondition) builder1.getCondition()).left());
        assertEquals(SqlOperator.IS_NOT_NULL, ((WhereLeafCondition) builder1.getCondition()).operator());
        assertNull(((WhereLeafCondition) builder1.getCondition()).right());
    }

    @Test
    void whereMultipleTest() throws ParsingException
    {
        SqlParser parser = new SqlParser("SELECT * FROM employees WHERE id = 1 AND firstname != John");

        StatementBuilder builder = parser.parse(database);

        assertInstanceOf(WhereBuilder.class, builder);
        WhereBuilder builder1 = (WhereBuilder) builder;
        assertInstanceOf(WhereNodeCondition.class, builder1.getCondition());
        assertEquals(SqlReservedWord.AND, ((WhereNodeCondition) builder1.getCondition()).operator());
    }
}