package utils.file;

import utils.folder.Folder;
import utils.folder.FolderException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class FileHandler {
    public static File getFileFrom(Folder folder, String filename){
        return new File(folder.getPath()+filename);
    }
    public static List<File> getAllFilesHavingPrefixFrom(Folder folder, String prefix){
        Path folderPath = Paths.get(folder.getPath());
        if(!Files.isDirectory(folderPath)){
            throw new FolderException("No such folder: "+folder);
        }
        File[] files = new File(folder.getPath()).listFiles();
        List<File> prefixFiles = new LinkedList<>();
        for(File file: files){
            if(file.getName().endsWith(prefix))
                prefixFiles.add(file);
        }
        return prefixFiles;
    }
}
