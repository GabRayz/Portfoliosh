package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.Column;
import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.ResultSet;
import fr.gabray.portfoliosh.command.sql.Table;
import fr.gabray.portfoliosh.exception.SqlException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatementBuilderTest {

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
    void simpleSelectWithoutFromShouldThrowTest()
    {
        SelectBuilder statement = new SelectBuilder().column("id").column("firstname");

        assertThrows(UnsupportedOperationException.class, () -> statement.execute(database));
    }

    @Test
    void simpleSelectFromTest()
    {
        StatementBuilder statement = new SelectBuilder()
                .column("id").column("firstname")
                .from("employees");

        ResultSet result = statement.execute(database);

        assertEquals(2, result.columns().size());
        assertEquals(4, result.data().size());
    }

    @Test
    void selectInvalidFrom()
    {
        StatementBuilder statement = new SelectBuilder()
                .column("id").column("firstname")
                .from("invalid");

        assertThrows(SqlException.class, () -> statement.execute(database));
    }

    @Test
    void selectInvalidColumnsFrom()
    {
        StatementBuilder statement = new SelectBuilder()
                .column("id").column("invalid")
                .from("employees");

        assertThrows(SqlException.class, () -> statement.execute(database));
    }

    @Test
    void orderByIdTest()
    {
        StatementBuilder statement = new SelectBuilder()
                .column("id").column("firstname")
                .from("employees")
                .orderBy("id");

        ResultSet result = statement.execute(database);

        assertEquals(2, result.columns().size());
        assertEquals(4, result.data().size());
        assertEquals("John", result.data().get(0).get("firstname").toString());
        assertEquals("Jack", result.data().get(1).get("firstname").toString());
        assertEquals("Obiwan", result.data().get(2).get("firstname").toString());
        assertEquals("Darth", result.data().get(3).get("firstname").toString());
    }

    @Test
    void orderByAgeTest()
    {
        StatementBuilder statement = new SelectBuilder()
                .column("age").column("firstname")
                .from("employees")
                .orderBy("age");

        ResultSet result = statement.execute(database);

        assertEquals(2, result.columns().size());
        assertEquals(4, result.data().size());
        assertEquals("John", result.data().get(0).get("firstname").toString());
        assertEquals("Darth", result.data().get(1).get("firstname").toString());
        assertEquals("Jack", result.data().get(2).get("firstname").toString());
        assertEquals("Obiwan", result.data().get(3).get("firstname").toString());
    }

    @Test
    void orderByAgeDescTest()
    {
        StatementBuilder statement = new SelectBuilder()
                .column("age").column("firstname")
                .from("employees")
                .orderBy("age")
                .desc();

        ResultSet result = statement.execute(database);

        assertEquals(2, result.columns().size());
        assertEquals(4, result.data().size());
        assertEquals("John", result.data().get(3).get("firstname").toString());
        assertEquals("Darth", result.data().get(2).get("firstname").toString());
        assertEquals("Jack", result.data().get(1).get("firstname").toString());
        assertEquals("Obiwan", result.data().get(0).get("firstname").toString());
    }

    @Test
    void orderByInvalidColumnTest()
    {
        StatementBuilder statement = new SelectBuilder()
                .column("id").column("firstname")
                .from("employees")
                .orderBy("age");

        assertThrows(SqlException.class, () -> statement.execute(database));
    }
}
