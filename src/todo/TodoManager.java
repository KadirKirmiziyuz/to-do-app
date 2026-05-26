package todo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TodoManager {

    private final List<Task> tasks = new ArrayList<>();

    public void add(Task t) {
        tasks.add(t);
    }

    public boolean remove(int id) {
        return tasks.removeIf(t -> t.getId() == id);
    }

    public Optional<Task> findById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst();
    }

    public List<Task> getAll() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getPending() {
        return tasks.stream()
                .filter(t -> !t.isDone())
                .collect(Collectors.toList());
    }

    public List<Task> getDone() {
        return tasks.stream()
                .filter(Task::isDone)
                .collect(Collectors.toList());
    }

    public List<Task> getOverdue() {
        return tasks.stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Task> getByPriority(Task.Priority p) {
        return tasks.stream()
                .filter(t -> t.getPriority() == p)
                .collect(Collectors.toList());
    }

    public List<Task> getSorted() {
        return tasks.stream()
                .sorted(Comparator
                        .comparing(Task::isDone)
                        .thenComparing(Comparator.comparing(Task::getPriority).reversed()))
                .collect(Collectors.toList());
    }

    public List<Task> search(String keyword) {
        String kw = keyword.toLowerCase();
        return tasks.stream()
                .filter(t -> t.getTitle().toLowerCase().contains(kw)
                        || t.getDescription().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public int totalCount()   { return tasks.size(); }
    public int pendingCount() { return (int) tasks.stream().filter(t -> !t.isDone()).count(); }
    public int doneCount()    { return (int) tasks.stream().filter(Task::isDone).count(); }
}
