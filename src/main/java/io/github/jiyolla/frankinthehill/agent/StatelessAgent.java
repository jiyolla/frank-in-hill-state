package io.github.jiyolla.frankinthehill.agent;

import io.github.jiyolla.frankinthehill.command.Command;
import io.github.jiyolla.frankinthehill.query.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class StatelessAgent {
    private final Collection<Query> availableQueries;
    private final Collection<Command> availableCommands;


    public StatelessAgent(Collection<Query> availableQueries, Collection<Command> availableCommands) {
        this.availableQueries = availableQueries;
        this.availableCommands = availableCommands;
    }

    public List<Command> signal(LocalDateTime now) {
        return null;
    }
}