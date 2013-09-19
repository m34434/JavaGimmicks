package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.javagimmicks.collections.CollectionDifference;
import net.sf.javagimmicks.collections.diff.Difference;
import net.sf.javagimmicks.collections.diff.DifferenceList;
import net.sf.javagimmicks.collections.diff.DifferenceUtils;
import net.sf.javagimmicks.collections.transformer.Transformer;
import net.sf.javagimmicks.collections.transformer.TransformerUtils;

public class FolderDiffBuilder
{
   private final Comparator<PathInfo> PATH_COMPARATOR = FileInfoComparatorBuilder.PATH_INFO_COMPARATOR;

   private final File _sourceFolder;
   private final Collection<String> _sourceIncludes = new TreeSet<String>();
   private final Collection<String> _sourceExcludes = new TreeSet<String>();

   private final File _targetFolder;
   private final Collection<String> _targetIncludes = new TreeSet<String>();
   private final Collection<String> _targetExcludes = new TreeSet<String>();

   private final FileInfoComparatorBuilder _comparatorBuilder = new FileInfoComparatorBuilder();

   private boolean _recursive;
   private FolderDiffListener _listener;

   public FolderDiffBuilder(final File sourceFolder, final File targetFolder, final boolean recursive)
   {
      _sourceFolder = sourceFolder;
      _targetFolder = targetFolder;

      _recursive = recursive;
   }

   public FolderDiffBuilder(final File sourceFolder, final File targetFolder)
   {
      this(sourceFolder, targetFolder, true);
   }

   public FolderDiff buildFolderDiff()
   {
      final IncludeExcludeFilenameFilter sourceFilter = new IncludeExcludeFilenameFilter(_sourceIncludes,
            _sourceExcludes);
      final FileScanner sourceScanner = new FileScanner(_sourceFolder, sourceFilter, _recursive);
      if (_listener != null)
      {
         sourceScanner.setFolderDiffListener(_listener);
      }
      final List<FileInfo> sourceFiles = sourceScanner.scan();

      final IncludeExcludeFilenameFilter targetFilter = new IncludeExcludeFilenameFilter(_targetIncludes,
            _targetExcludes);
      final FileScanner targetScanner = new FileScanner(_targetFolder, targetFilter, _recursive);
      if (_listener != null)
      {
         targetScanner.setFolderDiffListener(_listener);
      }
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

         @SuppressWarnings("deprecation")
         final CollectionDifference<FileInfo> collectionDifference = new CollectionDifference<FileInfo>(listDelete,
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

   public File getSourceFolder()
   {
      return _sourceFolder;
   }

   public Collection<String> getSourceIncludes()
   {
      return _sourceIncludes;
   }

   public Collection<String> getSourceExcludes()
   {
      return _sourceExcludes;
   }

   public File getTargetFolder()
   {
      return _targetFolder;
   }

   public Collection<String> getTargetIncludes()
   {
      return _targetIncludes;
   }

   public Collection<String> getTargetExcludes()
   {
      return _targetExcludes;
   }

   public boolean isRecursive()
   {
      return _recursive;
   }

   public boolean isCompareChecksum()
   {
      return _comparatorBuilder.isCompareChecksum();
   }

   public boolean isCompareLastModified()
   {
      return _comparatorBuilder.isCompareLastModified();
   }

   public boolean isCompareSize()
   {
      return _comparatorBuilder.isCompareSize();
   }

   public FolderDiffBuilder addSourceInclude(final String pattern)
   {
      _sourceIncludes.add(pattern);
      return this;
   }

   public FolderDiffBuilder addSourceExclude(final String pattern)
   {
      _sourceExcludes.add(pattern);
      return this;
   }

   public FolderDiffBuilder addTargetInclude(final String pattern)
   {
      _targetIncludes.add(pattern);
      return this;
   }

   public FolderDiffBuilder addTargetExclude(final String pattern)
   {
      _targetExcludes.add(pattern);
      return this;
   }

   public FolderDiffBuilder addSourceIncludes(final Collection<String> patterns)
   {
      _sourceIncludes.addAll(patterns);
      return this;
   }

   public FolderDiffBuilder addSourceExcludes(final Collection<String> patterns)
   {
      _sourceExcludes.addAll(patterns);
      return this;
   }

   public FolderDiffBuilder addTargetIncludes(final Collection<String> patterns)
   {
      _targetIncludes.addAll(patterns);
      return this;
   }

   public FolderDiffBuilder addTargetExcludes(final Collection<String> patterns)
   {
      _targetExcludes.addAll(patterns);
      return this;
   }

   public FolderDiffBuilder setRecursive(final boolean recursive)
   {
      _recursive = recursive;
      return this;
   }

   public FolderDiffBuilder setCompareChecksum(final boolean compareChecksum)
   {
      _comparatorBuilder.setCompareChecksum(compareChecksum);
      return this;
   }

   public FolderDiffBuilder setCompareLastModified(final boolean compareLastModified)
   {
      _comparatorBuilder.setCompareLastModified(compareLastModified);
      return this;
   }

   public FolderDiffBuilder setCompareSize(final boolean compareSize)
   {
      _comparatorBuilder.setCompareSize(compareSize);
      return this;
   }

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

   public FolderDiffBuilder setFolderDiffListener(final FolderDiffListener listener)
   {
      _listener = listener;
      _comparatorBuilder.setFolderDiffListener(listener);

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
