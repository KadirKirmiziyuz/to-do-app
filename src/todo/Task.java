package todo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {

    public enum Priority { LOW, MEDIUM, HIGH }
    public enum Status   { PENDING, DONE }

    private static int idCounter = 1;

    private final int id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDate dueDate;

    public Task(String title, String description, Priority priority, LocalDate dueDate) {
        this.id          = idCounter++;
        this.title       = title;
        this.description = description;
        this.priority    = priority;
        this.status      = Status.PENDING;
        this.dueDate     = dueDate;
    }

    public Task(String title) {
        this(title, "", Priority.MEDIUM, null);
    }

    public void markDone() {
        this.status = Status.DONE;
    }

    public void markPending() {
        this.status = Status.PENDING;
    }

    public boolean isDone() {
        return status == Status.DONE;
    }

    public boolean isOverdue() {
        if (dueDate == null || isDone()) return false;
        return dueDate.isBefore(LocalDate.now());
    }

    public int getId()                  { return id; }
    public String getTitle()            { return title; }
    public String getDescription()      { return description; }
    public Priority getPriority()       { return priority; }
    public Status getStatus()           { return status; }
    public LocalDate getDueDate()       { return dueDate; }

    public void setTitle(String t)         { this.title = t; }
    public void setDescription(String d)   { this.description = d; }
    public void setPriority(Priority p)    { this.priority = p; }
    public void setDueDate(LocalDate d)    { this.dueDate = d; }

    @Override
    public String toString() {
        String fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(
                dueDate != null ? dueDate : LocalDate.MIN
        );
        String due     = dueDate != null ? fmt : "  -     ";
        String check   = isDone() ? "✓" : " ";
        String overdue = isOverdue() ? " (!)" : "";

        return String.format("[%s] #%-3d %-30s %-8s %-7s  Due: %s%s",
                check, id, title, priority, status, due, overdue);
    }
}
