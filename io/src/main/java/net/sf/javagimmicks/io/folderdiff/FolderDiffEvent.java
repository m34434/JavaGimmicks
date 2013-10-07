package net.sf.javagimmicks.io.folderdiff;

import java.io.File;

import net.sf.javagimmicks.event.Event;

/**
 * An {@link Event} implementation representing simple status information about
 * a folder comparison scan run.
 * 
 * @see FolderDiffBuilder#buildFolderDiff()
 */
public class FolderDiffEvent implements Event<FolderDiffEvent>
{
   /**
    * The possible types of {@link FolderDiffEvent}s.
    */
   public static enum Type
   {
      /**
       * A source or target folder was scanned
       */
      FolderScanned,

      /**
       * A source and target {@link FileInfo file} were compared
       */
      FilesCompared
   }

   private final FolderDiffBuilder _source;
   private final Type _type;

   private final File _scannedFolder;

   private final FileInfo _sourceFileInfo;
   private final FileInfo _targetFileInfo;

   FolderDiffEvent(final FolderDiffBuilder source, final File scannedFolder)
   {
      _source = source;
      _type = Type.FolderScanned;

      _scannedFolder = scannedFolder;

      _sourceFileInfo = null;
      _targetFileInfo = null;
   }

   FolderDiffEvent(final FolderDiffBuilder source, final FileInfo fileInfo1, final FileInfo fileInfo2)
   {
      _source = source;
      _type = Type.FilesCompared;

      _scannedFolder = null;

      if (fileInfo1.isSource() && fileInfo2.isTarget())
      {
         _sourceFileInfo = fileInfo1;
         _targetFileInfo = fileInfo2;
      }
      else if (fileInfo1.isTarget() && fileInfo2.isSource())
      {
         _sourceFileInfo = fileInfo2;
         _targetFileInfo = fileInfo1;
      }
      else
      {
         throw new IllegalStateException(String.format("Compared FileInfos have same or illegal origin: %1$s / %2$s",
               fileInfo1, fileInfo2));
      }
   }

   /**
    * Returns the {@link Type} of this event.
    * 
    * @return the {@link Type} of this event
    */
   public Type getType()
   {
      return _type;
   }

   /**
    * Returns if the {@link #getType() Type} of this event is
    * {@link Type#FolderScanned}.
    * 
    * @return if the {@link #getType() Type} of this event is
    *         {@link Type#FolderScanned}
    */
   public boolean isFolderScanned()
   {
      return getType() == Type.FolderScanned;
   }

   /**
    * Returns if the {@link #getType() Type} of this event is
    * {@link Type#FilesCompared}.
    * 
    * @return if the {@link #getType() Type} of this event is
    *         {@link Type#FilesCompared}
    */
   public boolean isFilesCompared()
   {
      return getType() == Type.FilesCompared;
   }

   /**
    * Returns the scanned {@link File folder} if the event {@link #getType()
    * Type} is {@link Type#FolderScanned}, otherwise {@code null}.
    * 
    * @return the scanned {@link File folder} if the event {@link #getType()
    *         Type} is {@link Type#FolderScanned}, otherwise {@code null}
    */
   public File getScannedFolder()
   {
      return _scannedFolder;
   }

   /**
    * Returns the source {@link FileInfo} if the event {@link #getType() Type}
    * is {@link Type#FilesCompared}, otherwise {@code null}.
    * 
    * @return the source {@link FileInfo} if the event {@link #getType() Type}
    *         is {@link Type#FilesCompared}, otherwise {@code null}
    */
   public FileInfo getSourceFileInfo()
   {
      return _sourceFileInfo;
   }

   /**
    * Returns the target {@link FileInfo} if the event {@link #getType() Type}
    * is {@link Type#FilesCompared}, otherwise {@code null}.
    * 
    * @return the target {@link FileInfo} if the event {@link #getType() Type}
    *         is {@link Type#FilesCompared}, otherwise {@code null}
    */
   public FileInfo getTargetFileInfo()
   {
      return _targetFileInfo;
   }

   /**
    * Returns the {@link FolderDiffBuilder} that created this instance.
    * 
    * @return the {@link FolderDiffBuilder} that created this instance
    */
   @Override
   public FolderDiffBuilder getSource()
   {
      return _source;
   }
}
