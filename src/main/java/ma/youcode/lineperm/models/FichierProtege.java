package ma.youcode.lineperm.models;

public class FichierProtege {
    private String permissions;
    private String owner;
    private String fileName;

    public FichierProtege(String permissions, String owner, String fileName) {
        this.permissions = permissions;
        this.owner = owner;
        this.fileName = fileName;
    }

    public String getPermissions() {
        return permissions;
    }
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
