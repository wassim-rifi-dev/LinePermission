package ma.youcode.lineperm.models;

import java.time.LocalDate;
import java.time.LocalTime;

import ma.youcode.lineperm.models.enums.LogResult;
import ma.youcode.lineperm.models.enums.LogType;

public class Log {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private User user;
    private LogType type;
    private Fichier file;
    private LogResult result;

    public Log(LocalDate date, LocalTime time, User user, LogType type, Fichier file, LogResult result) {
        this.date = date;
        this.time = time;
        this.user = user;
        this.type = type;
        this.file = file;
        this.result = result;
    }

    public Log(Long id, LocalDate date, LocalTime time, User user, LogType type, Fichier file, LogResult result) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.user = user;
        this.type = type;
        this.file = file;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public Fichier getFile() {
        return file;
    }

    public void setFile(Fichier file) {
        this.file = file;
    }

    public LogResult getResult() {
        return result;
    }

    public void setResult(LogResult result) {
        this.result = result;
    }
}