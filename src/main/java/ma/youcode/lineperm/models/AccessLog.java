package ma.youcode.lineperm.models;

import java.time.LocalDate;
import java.time.LocalTime;

import ma.youcode.lineperm.models.enums.LogResult;
import ma.youcode.lineperm.models.enums.LogType;

public class AccessLog {
    private LocalDate date;
    private LocalTime time;
    private String user;
    private LogType type;
    private String file;
    private LogResult result;
    
    public AccessLog(LocalDate date, LocalTime time, String user, LogType type, String file, LogResult result) {
        this.date = date;
        this.time = time;
        this.user = user;
        this.type = type;
        this.file = file;
        this.result = result;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public LogResult getResult() {
        return result;
    }

    public void setResult(LogResult result) {
        this.result = result;
    }
}
