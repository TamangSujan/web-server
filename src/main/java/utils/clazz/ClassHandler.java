package utils.clazz;

import utils.file.FileHandler;
import utils.folder.Folder;
import utils.folder.FolderHandler;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ClassHandler {
    private ClassHandler(){}

    public static List<Class<?>> getAllClassFromProject(String fullPackagePath) throws ClassNotFoundException {
        List<Class<?>> classes = new LinkedList<>();
        Folder folder = FolderHandler.getFolder(
                FolderHandler.getProjectFolder().getPath(),
                "out",
                "production",
                "JWebServer",
                fullPackagePath.replace(".", File.pathSeparator)
                );
        List<File> classFiles = FileHandler.getAllFilesHavingPrefixFrom(folder, ".class");
        for(File classFile: classFiles){
            classes.add(Class.forName(fullPackagePath+"."+getClassName(classFile)));
        }
        return classes;
    }

    private static String getClassName(File clazz){
        return clazz.getName().substring(0, clazz.getName().length()-6);
    }
}
