package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.javagimmicks.collections.CollectionDifference;
import net.sf.javagimmicks.collections.diff.Difference;
import net.sf.javagimmicks.collections.diff.DifferenceList;
import net.sf.javagimmicks.collections.diff.DifferenceUtils;
import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.event.EventListener;
import net.sf.javagimmicks.event.ObservableBase;
import net.sf.javagimmicks.io.folderdiff.FileInfo.Origin;
import net.sf.javagimmicks.transform.Transformer;

/**
 * This class is the starting point for comparing two folders - it allows to
 * configure and finally execute the folder comparison returning a
 * {@link FolderDiff} object which wraps the results.
 * <p>
 * Folder comparison always needs a source and target folder specification and
 * can be performed {@link #setRecursive(boolean) recursively} or not.
 * <p>
 * It is also configurable how {@link File} contents are to be compared. Clients
 * can activate any of the following modes (all are off by default):
 * <ul>
 * <li>{@link #setCompareSize(boolean) File size comparison}</li>
 * <li>{@link #setCompareLastModified(boolean) Change date comparison}</li>
 * <li>{@link #setCompareChecksum(boolean) File checksum comparison}</li>
 * </ul>
 * <b>Note that comparison strategies are applied in the shown order using a
 * first-hit algorithm (e.g. checksum is not compared if size already
 * differs).</b>
 * <p>
 * Finally it is possible to filter compared files/folders per side via
 * includes/excludes (following Ant style).
 */
public class FolderDiffBuilder extends ObservableBase<FolderDiffEvent>
{
   private final Comparator<PathInfo> PATH_COMPARATOR = FileInfoComparatorBuilder.PATH_INFO_COMPARATOR;

   private final File _sourceFolder;
   private final Collection<String> _sourceIncludes = new TreeSet<String>();
   private final Collection<String> _sourceExcludes = new TreeSet<String>();

   private final File _targetFolder;
   private final Collection<String> _targetIncludes = new TreeSet<String>();
   private final Collection<String> _targetExcludes = new TreeSet<String>();

   private final FileInfoComparatorBuilder _comparatorBuilder = new FileInfoComparatorBuilder(this);

   private boolean _recursive;

   /**
    * Creates a new instance for the given {@link File source folder} and
    * {@link File target folder} using recursion depending on the given flag.
    * 
    * @param sourceFolder
    *           the {@link File source folder} of the comparison
    * @param targetFolder
    *           the {@link File target folder} of the comparison
    * @param recursive
    *           if comparison should be done recursively
    */
   public FolderDiffBuilder(final File sourceFolder, final File targetFolder, final boolean recursive)
   {
      _sourceFolder = sourceFolder;
      _targetFolder = targetFolder;

      _recursive = recursive;
   }

   /**
    * Creates a new instance for the given {@link File source folder} and
    * {@link File target folder} using recursive scanning.
    * 
    * @param sourceFolder
    *           the {@link File source folder} of the comparison
    * @param targetFolder
    *           the {@link File target folder} of the comparison
    */
   public FolderDiffBuilder(final File sourceFolder, final File targetFolder)
   {
      this(sourceFolder, targetFolder, true);
   }

   /**
    * Starts a new comparison run and wraps the results into a
    * {@link FolderDiff} object.
    * <p>
    * <b>Attention:</b> this operation is not Thread-safe! Client should take
    * care about proper synchronization!
    * 
    * @return the {@link FolderDiff} object containing the results of the
    *         comparison
    */
   public FolderDiff buildFolderDiff()
   {
      final IncludeExcludeFilenameFilter sourceFilter = new IncludeExcludeFilenameFilter(_sourceIncludes,
            _sourceExcludes);
      final FileScanner sourceScanner = new FileScanner(Origin.Source, this, _sourceFolder, sourceFilter, _recursive);
      final List<FileInfo> sourceFiles = sourceScanner.scan();

      final IncludeExcludeFilenameFilter targetFilter = new IncludeExcludeFilenameFilter(_targetIncludes,
            _targetExcludes);
      final FileScanner targetScanner = new FileScanner(Origin.Target, this, _targetFolder, targetFilter, _recursive);
      final List<FileInfo> targetFiles = targetScanner.scan();

      final SortedSet<PathInfo> filesAll = new TreeSet<PathInfo>(PATH_COMPARATOR);
      filesAll.addAll(getPathInfoCollection(sourceFiles));
      filesAll.addAll(getPathInfoCollection(targetFiles));

      final DifferenceList<FileInfo> differences = DifferenceUtils.findDifferences(sourceFiles, targetFiles,
            _comparatorBuilder.buildComparator());

      final SortedSet<PathInfo> filesDifferent = new TreeSet<PathInfo>(PATH_COMPARATOR);
      final SortedSet<PathInfo> filesSourceOnly = new TreeSet<PathInfo>(PATH_COMPARATOR);
      final SortedSet<PathInfo> filesTargetOnly = new TreeSet<PathInfo>(PATH_COMPARATOR);

      for (final Difference<FileInfo> difference : differences)
      {
         final List<FileInfo> listDelete = difference.deleteRange();
         final List<FileInfo> listAdd = difference.addRange();

         final CollectionDifference<FileInfo> collectionDifference = CollectionDifference.create(listDelete,
               listAdd);

         filesDifferent.addAll(getPathInfoCollection(collectionDifference.getBoth()));
         filesSourceOnly.addAll(getPathInfoCollection(collectionDifference.getOnlyA()));
         filesTargetOnly.addAll(getPathInfoCollection(collectionDifference.getOnlyB()));
      }

      final SortedSet<PathInfo> filesEqual = new TreeSet<PathInfo>(filesAll);
      filesEqual.removeAll(filesDifferent);
      filesEqual.removeAll(filesSourceOnly);
      filesEqual.removeAll(filesTargetOnly);

      return new FolderDiff(
            _sourceFolder, _targetFolder,
            filesAll, filesEqual, filesDifferent, filesSourceOnly, filesTargetOnly);
   }

   /**
    * Returns the {@link File source folder} of the comparison
    * 
    * @return the {@link File source folder} of the comparison
    */
   public File getSourceFolder()
   {
      return _sourceFolder;
   }

   /**
    * Returns the included names of files/folders to scan on the source side
    * 
    * @return the included names of files/folders to scan on the source side
    */
   public Collection<String> getSourceIncludes()
   {
      return _sourceIncludes;
   }

   /**
    * Returns the excluded names of files/folders not to scan on the source side
    * 
    * @return the excluded names of files/folders not to scan on the source side
    */
   public Collection<String> getSourceExcludes()
   {
      return _sourceExcludes;
   }

   /**
    * Returns the {@link File target folder} of the comparison
    * 
    * @return the {@link File target folder} of the comparison
    */
   public File getTargetFolder()
   {
      return _targetFolder;
   }

   /**
    * Returns the included names of files/folders to scan on the target side
    * 
    * @return the included names of files/folders to scan on the target side
    */
   public Collection<String> getTargetIncludes()
   {
      return _targetIncludes;
   }

   /**
    * Returns the excluded names of files/folders not to scan on the target side
    * 
    * @return the excluded names of files/folders not to scan on the target side
    */
   public Collection<String> getTargetExcludes()
   {
      return _targetExcludes;
   }

   /**
    * Returns if comparisons are run in recursive mode.
    * 
    * @return if comparisons are run in recursive mode
    */
   public boolean isRecursive()
   {
      return _recursive;
   }

   /**
    * Returns if {@link File}s should be compared via checksum.
    * 
    * @return if {@link File}s should be compared via checksum
    */
   public boolean isCompareChecksum()
   {
      return _comparatorBuilder.isCompareChecksum();
   }

   /**
    * Returns if {@link File}s should be compared via last modified date.
    * 
    * @return if {@link File}s should be compared via last modified date
    */
   public boolean isCompareLastModified()
   {
      return _comparatorBuilder.isCompareLastModified();
   }

   /**
    * Returns if {@link File}s should be compared via size.
    * 
    * @return if {@link File}s should be compared via size
    */
   public boolean isCompareSize()
   {
      return _comparatorBuilder.isCompareSize();
   }

   /**
    * Adds new inclusion pattern(s) for the source folder
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder addSourceIncludes(final String... patterns)
   {
      return addSourceIncludes(Arrays.asList(patterns));
   }

   /**
    * Adds new exclusion patterns for the source folder
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder addSourceExcludes(final String... patterns)
   {
      return addSourceExcludes(Arrays.asList(patterns));
   }

   /**
    * Adds new inclusion pattern(s) for the target folder
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder addTargetIncludes(final String... patterns)
   {
      return addTargetIncludes(Arrays.asList(patterns));
   }

   /**
    * Adds new exclusion pattern(s) for the target folder
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder addTargetExcludes(final String... patterns)
   {
      return addTargetExcludes(Arrays.asList(patterns));
   }

   /**
    * Adds new inclusion pattern(s) for the source folder
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder addSourceIncludes(final Collection<String> patterns)
   {
      _sourceIncludes.addAll(patterns);
      return this;
   }

   /**
    * Adds new exclusion pattern(s) for the source folder
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder addSourceExcludes(final Collection<String> patterns)
   {
      _sourceExcludes.addAll(patterns);
      return this;
   }

   /**
    * Adds new inclusion pattern(s) for the target folder
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder addTargetIncludes(final Collection<String> patterns)
   {
      _targetIncludes.addAll(patterns);
      return this;
   }

   /**
    * Adds new exclusion pattern(s) for the target folder
    * 
    * @param patterns
    *           the pattern {@link String}s to add
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder addTargetExcludes(final Collection<String> patterns)
   {
      _targetExcludes.addAll(patterns);
      return this;
   }

   /**
    * Enables or disables recursive scanning.
    * 
    * @param recursive
    *           if recursive scanning should be enabled or disabled
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder setRecursive(final boolean recursive)
   {
      _recursive = recursive;
      return this;
   }

   /**
    * Enables or disables {@link File} checksum comparison.
    * 
    * @param compareChecksum
    *           if {@link File} checksum comparison should be enabled or
    *           disabled
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder setCompareChecksum(final boolean compareChecksum)
   {
      _comparatorBuilder.setCompareChecksum(compareChecksum);
      return this;
   }

   /**
    * Enables or disables {@link File} change date comparison.
    * 
    * @param compareLastModified
    *           if {@link File} change date comparison should be enabled or
    *           disabled
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder setCompareLastModified(final boolean compareLastModified)
   {
      _comparatorBuilder.setCompareLastModified(compareLastModified);
      return this;
   }

   /**
    * Enables or disables {@link File} size comparison.
    * 
    * @param compareSize
    *           if {@link File} change size should be enabled or disabled
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder setCompareSize(final boolean compareSize)
   {
      _comparatorBuilder.setCompareSize(compareSize);
      return this;
   }

   /**
    * Convenience method for {@link #addEventListener(EventListener)} that
    * additionally return the current {@link FolderDiffBuilder} so that the
    * method is usable for fluent building.
    * 
    * @param listener
    *           the {@link EventListener} to add
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder addListener(final EventListener<FolderDiffEvent> listener)
   {
      addEventListener(listener);

      return this;
   }

   /**
    * Reset this instance to default values:
    * <ul>
    * <li>Clears all include/exclude filters on source and target side</li>
    * <li>Disables all three file comparison options (file will only be compared
    * by existence)</li>
    * <li>Enables recursion</li>
    * </ul>
    * 
    * @return the {@link FolderDiffBuilder} itself
    */
   public FolderDiffBuilder reset()
   {
      _sourceIncludes.clear();
      _sourceExcludes.clear();
      _targetIncludes.clear();
      _targetExcludes.clear();

      setCompareChecksum(false);
      setCompareLastModified(false);
      setCompareSize(false);
      setRecursive(true);

      return this;
   }

   private static Collection<PathInfo> getPathInfoCollection(final Collection<FileInfo> collection)
   {
      return TransformerUtils.decorate(collection, FILE_TO_PATH_INFO);
   }

   private static final Transformer<FileInfo, PathInfo> FILE_TO_PATH_INFO = new Transformer<FileInfo, PathInfo>()
   {
      @Override
      public PathInfo transform(final FileInfo source)
      {
         return source.getPathInfo();
      }
   };
}
