package utils.folder;

import java.io.File;

public class Folder {
    private final String path;

    /**
     * @param folders folder names in hierarchical manner where first becomes parent/root folder,
     *                second becomes child of first where last folder name becomes final path
     */
    public Folder(String... folders){
        StringBuilder builder = new StringBuilder();
        for(String folder: folders){
            builder.append(folder).append(File.separator);
        }
        path = builder.toString();
    }

    public String getPath(){
        return path;
    }
}
