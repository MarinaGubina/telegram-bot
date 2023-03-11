package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


@Entity
@Table(name = "notification_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "description")
    private String description;
    @Column(name = "time")
    private LocalDateTime time;

    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return chatId == task.chatId && Objects.equals(id, task.id) && Objects.equals(description, task.description) && Objects.equals(time, task.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, description, time);
    }

    @Override
    public String toString() {
        return  "' " + description + " " +
                time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + " '";
    }
}
