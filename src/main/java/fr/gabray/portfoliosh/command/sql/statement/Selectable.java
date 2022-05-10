package fr.gabray.portfoliosh.command.sql.statement;

import java.util.ArrayList;
import java.util.List;

public interface Selectable extends StatementBuilder {

    default SelectBuilder select(List<String> columns)
    {
        return new SelectBuilder(this, columns, false);
    }

    default SelectBuilder select()
    {
        return new SelectBuilder(this, new ArrayList<>(), false);
    }

    default SelectBuilder selectAll()
    {
        return new SelectBuilder(this).all();
    }
}
