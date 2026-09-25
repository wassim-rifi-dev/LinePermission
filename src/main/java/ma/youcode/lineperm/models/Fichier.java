package ma.youcode.lineperm.models;

public class Fichier {
    private Long id;
    private String permissions;
    private User owner;
    private String fileName;

    public Fichier(String permissions, User owner, String fileName) {
        this.permissions = permissions;
        this.owner = owner;
        this.fileName = fileName;
    }

    public Fichier(Long id, String permissions, User owner, String fileName) {
        this.id = id;
        this.permissions = permissions;
        this.owner = owner;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}