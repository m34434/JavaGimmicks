package net.sf.javagimmicks.io.folderdiff.event;

import java.io.File;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.io.folderdiff.FileInfo;
import net.sf.javagimmicks.io.folderdiff.FolderDiffBuilder;

public class FolderDiffEvent implements Event<FolderDiffEvent>
{
   public static enum Type
   {
      FolderScanned, FilesCompared
   }

   private final FolderDiffBuilder _source;
   private final Type _type;

   private final File _scannedFolder;

   private final FileInfo _sourceFileInfo;
   private final FileInfo _targetFileInfo;

   public FolderDiffEvent(final FolderDiffBuilder source, final File scannedFolder)
   {
      _source = source;
      _type = Type.FolderScanned;

      _scannedFolder = scannedFolder;

      _sourceFileInfo = null;
      _targetFileInfo = null;
   }

   public FolderDiffEvent(final FolderDiffBuilder source, final FileInfo sourceFileInfo, final FileInfo targetFileInfo)
   {
      _source = source;
      _type = Type.FilesCompared;

      _scannedFolder = null;

      _sourceFileInfo = sourceFileInfo;
      _targetFileInfo = targetFileInfo;
   }

   public Type getType()
   {
      return _type;
   }

   public boolean isFolderScanned()
   {
      return getType() == Type.FolderScanned;
   }

   public boolean isFilesCompared()
   {
      return getType() == Type.FilesCompared;
   }

   public File getScannedFolder()
   {
      return _scannedFolder;
   }

   public FileInfo getSourceFileInfo()
   {
      return _sourceFileInfo;
   }

   public FileInfo getTargetFileInfo()
   {
      return _targetFileInfo;
   }

   @Override
   public FolderDiffBuilder getSource()
   {
      return _source;
   }
}
