package todo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner sc      = new Scanner(System.in);
    private static final TodoManager mgr = new TodoManager();
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        seedData();

        System.out.println("\n  ╔══════════════════════╗");
        System.out.println("  ║    📋  To-Do App     ║");
        System.out.println("  ╚══════════════════════╝\n");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> listTasks();
                case "2" -> addTask();
                case "3" -> completeTask();
                case "4" -> removeTask();
                case "5" -> editTask();
                case "6" -> searchTasks();
                case "7" -> showStats();
                case "0" -> running = false;
                default  -> System.out.println("  Geçersiz seçim.\n");
            }
        }

        System.out.println("\n  Görüşürüz! 👋\n");
    }

    private static void printMenu() {
        System.out.println("  ┌─────────────────────────┐");
        System.out.println("  │  1. Görevleri listele   │");
        System.out.println("  │  2. Görev ekle          │");
        System.out.println("  │  3. Görevi tamamla      │");
        System.out.println("  │  4. Görevi sil          │");
        System.out.println("  │  5. Görevi düzenle      │");
        System.out.println("  │  6. Ara                 │");
        System.out.println("  │  7. İstatistikler       │");
        System.out.println("  │  0. Çıkış               │");
        System.out.println("  └─────────────────────────┘");
        System.out.print("  Seçim: ");
    }

    private static void listTasks() {
        System.out.println("\n  Filtre: [1] Tümü  [2] Bekleyen  [3] Tamamlanan  [4] Gecikmiş");
        System.out.print("  > ");
        String f = sc.nextLine().trim();

        List<Task> list = switch (f) {
            case "2" -> mgr.getPending();
            case "3" -> mgr.getDone();
            case "4" -> mgr.getOverdue();
            default  -> mgr.getSorted();
        };

        printList(list);
    }

    private static void printList(List<Task> list) {
        System.out.println();
        if (list.isEmpty()) {
            System.out.println("  Hiç görev yok.\n");
            return;
        }
        System.out.println("  [✓] #ID  Başlık                         Öncelik  Durum    Tarih");
        System.out.println("  " + "─".repeat(72));
        for (Task t : list)
            System.out.println("  " + t);
        System.out.println();
    }

    private static void addTask() {
        System.out.println();
        System.out.print("  Başlık: ");
        String title = sc.nextLine().trim();
        if (title.isEmpty()) { System.out.println("  Başlık boş olamaz.\n"); return; }

        System.out.print("  Açıklama (opsiyonel): ");
        String desc = sc.nextLine().trim();

        Task.Priority prio = askPriority();

        LocalDate due = askDate();

        Task task = new Task(title, desc, prio, due);
        mgr.add(task);
        System.out.println("  ✓ Görev eklendi: #" + task.getId() + "\n");
    }

    private static void completeTask() {
        System.out.print("\n  Tamamlanacak görevin ID'si: ");
        int id = readInt();
        if (id < 0) return;

        Optional<Task> opt = mgr.findById(id);
        if (opt.isEmpty()) { System.out.println("  Bulunamadı.\n"); return; }

        Task t = opt.get();
        if (t.isDone()) {
            System.out.print("  Zaten tamamlanmış. Tekrar bekleyene al? (e/h): ");
            if (sc.nextLine().trim().equalsIgnoreCase("e"))
                t.markPending();
        } else {
            t.markDone();
            System.out.println("  ✓ Tamamlandı: " + t.getTitle() + "\n");
        }
    }

    private static void removeTask() {
        System.out.print("\n  Silinecek görevin ID'si: ");
        int id = readInt();
        if (id < 0) return;

        if (mgr.remove(id))
            System.out.println("  Silindi.\n");
        else
            System.out.println("  Bulunamadı.\n");
    }

    private static void editTask() {
        System.out.print("\n  Düzenlenecek görevin ID'si: ");
        int id = readInt();
        if (id < 0) return;

        Optional<Task> opt = mgr.findById(id);
        if (opt.isEmpty()) { System.out.println("  Bulunamadı.\n"); return; }

        Task t = opt.get();
        System.out.println("  Mevcut başlık: " + t.getTitle());
        System.out.print("  Yeni başlık (boş bırakırsan değişmez): ");
        String newTitle = sc.nextLine().trim();
        if (!newTitle.isEmpty()) t.setTitle(newTitle);

        System.out.println("  Mevcut açıklama: " + t.getDescription());
        System.out.print("  Yeni açıklama: ");
        String newDesc = sc.nextLine().trim();
        if (!newDesc.isEmpty()) t.setDescription(newDesc);

        System.out.println("  Yeni öncelik değiştirmek ister misin? (e/h): ");
        if (sc.nextLine().trim().equalsIgnoreCase("e"))
            t.setPriority(askPriority());

        System.out.println("  Bitiş tarihini değiştirmek ister misin? (e/h): ");
        if (sc.nextLine().trim().equalsIgnoreCase("e"))
            t.setDueDate(askDate());

        System.out.println("  ✓ Güncellendi.\n");
    }

    private static void searchTasks() {
        System.out.print("\n  Aranacak kelime: ");
        String kw = sc.nextLine().trim();
        printList(mgr.search(kw));
    }

    private static void showStats() {
        System.out.println();
        System.out.println("  Toplam   : " + mgr.totalCount());
        System.out.println("  Bekleyen : " + mgr.pendingCount());
        System.out.println("  Tamam    : " + mgr.doneCount());
        System.out.println("  Gecikmiş : " + mgr.getOverdue().size());
        System.out.println();
    }

    private static Task.Priority askPriority() {
        while (true) {
            System.out.print("  Öncelik [1=LOW, 2=MEDIUM, 3=HIGH]: ");
            String s = sc.nextLine().trim();
            switch (s) {
                case "1" -> { return Task.Priority.LOW;    }
                case "2" -> { return Task.Priority.MEDIUM; }
                case "3" -> { return Task.Priority.HIGH;   }
                default  -> System.out.println("  1, 2 veya 3 gir.");
            }
        }
    }

    private static LocalDate askDate() {
        System.out.print("  Bitiş tarihi (dd/MM/yyyy, boş bırakabilirsin): ");
        String raw = sc.nextLine().trim();
        if (raw.isEmpty()) return null;
        try {
            return LocalDate.parse(raw, DF);
        } catch (DateTimeParseException e) {
            System.out.println("  Tarih formatı yanlış, tarih eklenmedi.");
            return null;
        }
    }

    private static int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  Sayı girilmeli.\n");
            return -1;
        }
    }

    private static void seedData() {
        mgr.add(new Task("Market alışverişi", "ekmek, süt, yoğurt", Task.Priority.LOW,
                LocalDate.now().plusDays(1)));
        mgr.add(new Task("Proje raporu yaz", "haftalık ilerleme raporu", Task.Priority.HIGH,
                LocalDate.now().minusDays(1)));
        mgr.add(new Task("Spor yap", "", Task.Priority.MEDIUM,
                LocalDate.now()));
        Task t = new Task("Kitap oku", "Sapiens'i bitir", Task.Priority.LOW, null);
        t.markDone();
        mgr.add(t);
    }
}
