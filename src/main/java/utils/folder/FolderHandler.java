package utils.folder;

public class FolderHandler {
    private static final String root = System.getProperty("user.home");
    private static final String currentFolder = System.getProperty("user.dir");

    public static Folder getResourceFolder(){
        return new Folder(currentFolder, "src", "main", "resources");
    }
    public static Folder getJavaFolder(){
        return new Folder(currentFolder, "src", "main", "java");
    }
    public static Folder getProjectFolder(){
        return new Folder(currentFolder);
    }

    public static Folder getFolder(String... folders){
        return new Folder(folders);
    }
}
