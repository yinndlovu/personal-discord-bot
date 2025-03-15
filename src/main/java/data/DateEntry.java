package data;

import java.time.LocalDate;

public class DateEntry {

    private final String description;
    private final LocalDate date;
    private final String emoji;

    public DateEntry(String description, LocalDate date, String emoji) {
        this.description = description;
        this.date = date;
        this.emoji = emoji;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public String getEmoji() {
        return emoji;
    }
}
