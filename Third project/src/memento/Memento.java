package memento;

import file.FileBase;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class Memento {
    private final Queue<FileBase> state;

    public Memento(Queue<FileBase> state) {
        this.state = new LinkedList<>(state);
    }

    public Collection<FileBase> getState() {
        return Collections.unmodifiableCollection(this.state);
    }
}
