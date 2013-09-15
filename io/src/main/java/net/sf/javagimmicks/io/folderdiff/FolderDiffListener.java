package net.sf.javagimmicks.io.folderdiff;

import java.io.File;

public interface FolderDiffListener
{
   public void folderScanned(File folder);
   public void fileInfosCompared(FileInfo fileInfo1, FileInfo fileInfo2);
}
