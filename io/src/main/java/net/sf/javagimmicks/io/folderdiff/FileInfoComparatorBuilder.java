package net.sf.javagimmicks.io.folderdiff;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.sf.javagimmicks.collections.ListComparator;
import net.sf.javagimmicks.math.NumberCompareUtils;
import net.sf.javagimmicks.math.comparator.LongComparator;
import net.sf.javagimmicks.util.CompositeComparator;

@SuppressWarnings("unchecked")
class FileInfoComparatorBuilder
{
   private final FolderDiffBuilder _builder;

   private boolean _compareSize = false;
   private boolean _compareLastModified = false;
   private boolean _compareChecksum = false;

   FileInfoComparatorBuilder(final FolderDiffBuilder builder)
   {
      _builder = builder;
   }

   public Comparator<FileInfo> buildComparator()
   {
      final List<Comparator<FileInfo>> comparators = new ArrayList<Comparator<FileInfo>>();

      comparators.add(PATH_COMPARATOR);

      if (_compareSize)
      {
         comparators.add(SIZE_COMPARATOR);
      }

      if (_compareLastModified)
      {
         comparators.add(LAST_MOD_COMPARATOR);
      }

      if (_compareChecksum)
      {
         comparators.add(CHECKSUM_COMPARATOR);
      }

      return new EventFileInfoComparator(new CompositeComparator<FileInfo>(comparators), _builder);
   }

   private static final class EventFileInfoComparator implements Comparator<FileInfo>
   {
      private final Comparator<FileInfo> _baseComparator;
      private final FolderDiffBuilder _builder;

      public EventFileInfoComparator(final Comparator<FileInfo> comparator, final FolderDiffBuilder builder)
      {
         _baseComparator = comparator;
         _builder = builder;
      }

      @Override
      public int compare(final FileInfo o1, final FileInfo o2)
      {
         // Fire an Event only in the Non-Trivial case (i.e. two files with the
         // same path are compared)
         if (!o1.isDirectory() && !o2.isDirectory() && o1.getPathFragments().equals(o2.getPathFragments())
               && o1.getOrigin() != o2.getOrigin())
         {
            _builder.fireEvent(new FolderDiffEvent(_builder, o1, o2));
         }

         return _baseComparator.compare(o1, o2);
      }
   }

   public boolean isCompareSize()
   {
      return _compareSize;
   }

   public boolean isCompareLastModified()
   {
      return _compareLastModified;
   }

   public boolean isCompareChecksum()
   {
      return _compareChecksum;
   }

   public FileInfoComparatorBuilder setCompareSize(final boolean compareSize)
   {
      _compareSize = compareSize;

      return this;
   }

   public FileInfoComparatorBuilder setCompareLastModified(final boolean compareLastModified)
   {
      _compareLastModified = compareLastModified;

      return this;
   }

   public void setCompareChecksum(final boolean compareChecksum)
   {
      _compareChecksum = compareChecksum;
   }

   private static final LongComparator LONG_COMPARATOR = NumberCompareUtils.getLongComparator();
   private static final ListComparator<String> STRING_LIST_COMPARATOR;

   static
   {
      final ListComparator<String> c = (ListComparator<String>) ListComparator.COMPARABLE_INSTANCE;
      STRING_LIST_COMPARATOR = c;
   }

   public static final Comparator<FileInfo> CHECKSUM_COMPARATOR = new Comparator<FileInfo>()
   {
      @Override
      public int compare(final FileInfo o1, final FileInfo o2)
      {
         return LONG_COMPARATOR.compare(o1.getChecksum(), o2.getChecksum());
      }
   };

   public static final Comparator<FileInfo> LAST_MOD_COMPARATOR = new Comparator<FileInfo>()
   {
      @Override
      public int compare(final FileInfo o1, final FileInfo o2)
      {
         if (o1.isDirectory() && o2.isDirectory())
         {
            return 0;
         }
         else if (o1.isDirectory())
         {
            return -1;
         }
         else if (o2.isDirectory())
         {
            return 1;
         }

         return LONG_COMPARATOR.compare(o1.getLastModified(), o2.getLastModified());
      }
   };

   public static final Comparator<FileInfo> SIZE_COMPARATOR = new Comparator<FileInfo>()
   {
      @Override
      public int compare(final FileInfo o1, final FileInfo o2)
      {
         return LONG_COMPARATOR.compare(o1.getSize(), o2.getSize());
      }
   };

   public static final Comparator<PathInfo> PATH_INFO_COMPARATOR = new Comparator<PathInfo>()
   {
      @Override
      public int compare(final PathInfo o1, final PathInfo o2)
      {
         final List<String> pathFragments1 = o1.getPathFragments();
         final List<String> pathFragments2 = o2.getPathFragments();

         final int size1 = pathFragments1.size();
         final int size2 = pathFragments2.size();

         if (size1 == 0 && size2 == 0)
         {
            return 0;
         }
         else if (size1 == 0)
         {
            return -1;
         }
         else if (size2 == 0)
         {
            return 1;
         }

         final List<String> parentPath1 = pathFragments1.subList(0, size1 - 1);
         final List<String> parentPath2 = pathFragments2.subList(0, size2 - 1);

         final int parentCompare = STRING_LIST_COMPARATOR.compare(parentPath1, parentPath2);

         if (parentCompare != 0)
         {
            return parentCompare;
         }

         final boolean isDirectory1 = o1.isDirectory();
         if (isDirectory1 != o2.isDirectory())
         {
            return isDirectory1 ? -1 : 1;
         }

         return pathFragments1.get(size1 - 1).compareTo(pathFragments2.get(size2 - 1));
      }
   };

   public static final Comparator<FileInfo> PATH_COMPARATOR = new Comparator<FileInfo>()
   {
      @Override
      public int compare(final FileInfo o1, final FileInfo o2)
      {
         return PATH_INFO_COMPARATOR.compare(o1.getPathInfo(), o2.getPathInfo());
      }
   };
}
