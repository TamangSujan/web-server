import assertions.Assert;
import utils.folder.Folder;
import utils.folder.FolderHandler;

public class FolderTest {
    public static void main(String[] args) {
        Folder resourcesFolder = FolderHandler.getResourceFolder();
        Assert.print(resourcesFolder.getPath());
    }
}
