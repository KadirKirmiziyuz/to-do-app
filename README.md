# 📋 Todo App

A simple console-based task management application written in plain Java.

## Features

- Add, delete, and edit tasks
- Priority levels (LOW / MEDIUM / HIGH)
- Due date tracking with overdue detection
- Status management (PENDING / DONE)
- Search by title or description
- Filter by: all, pending, completed, overdue

## Requirements

- Java 17 or higher

## Getting Started

```bash
# Compile
javac -d out src/todo/*.java

# Run
java -cp out todo.Main
```

## Project Structure

```
src/
└── todo/
    ├── Task.java          # Task model (fields, enums, business logic)
    ├── TodoManager.java   # List management (add, remove, filter, search)
    └── Main.java          # Console UI
```

## OOP Principles

| Principle | Where it's applied |
|---|---|
| Encapsulation | `Task` fields are private, accessed via getters/setters |
| Single Responsibility | Each class has one clearly defined job |
| Enum usage | `Priority` and `Status` as type-safe constant sets |
| Optional | `findById` returns `Optional<Task>` instead of null |

## Preview

```
  ╔══════════════════════╗
  ║    📋  To-Do App     ║
  ╚══════════════════════╝

  ┌─────────────────────────┐
  │  1. List tasks          │
  │  2. Add task            │
  │  3. Complete task       │
  │  4. Remove task         │
  │  5. Edit task           │
  │  6. Search              │
  │  7. Stats               │
  │  0. Exit                │
  └─────────────────────────┘
```

## License

MIT
