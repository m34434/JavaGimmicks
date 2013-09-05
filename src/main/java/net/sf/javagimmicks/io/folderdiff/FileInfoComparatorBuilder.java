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
   private boolean _comparePaths = true;
   private boolean _compareSize = false;
   private boolean _compareLastModified = false;
   private boolean _compareChecksum = false;

   private FolderDiffListener _listener;
   
   public static Comparator<FileInfo> getPathOnlyInstance()
   {
      return new FileInfoComparatorBuilder().buildComparator();
   }
   
   public static Comparator<FileInfo> getPathAndSizeInstance()
   {
      return new FileInfoComparatorBuilder().setCompareSize(true).buildComparator();
   }
   
   public static Comparator<FileInfo> getPathAndLastModifiedInstance()
   {
      return new FileInfoComparatorBuilder().setCompareLastModified(true).buildComparator();
   }
   
   public static Comparator<FileInfo> getPathAndSizeAndLastModifiedInstance()
   {
      return new FileInfoComparatorBuilder().setCompareLastModified(true).setCompareSize(true)
         .buildComparator();
   }
   
   public Comparator<FileInfo> buildComparator()
   {
      List<Comparator<FileInfo>> comparators = new ArrayList<Comparator<FileInfo>>();
      
      if(_compareSize)
      {
         comparators.add(SIZE_COMPARATOR);
      }
      
      if(_compareLastModified)
      {
         comparators.add(LAST_MOD_COMPARATOR);
      }
      
      if(_comparePaths)
      {
         comparators.add(PATH_COMPARATOR);
      }
      
      if(_compareChecksum)
      {
         comparators.add(CHECKSUM_COMPARATOR);
      }
      
      Comparator<FileInfo> actualComparator = new CompositeComparator<FileInfo>(comparators);
      return _listener == null ? actualComparator : new EventFileInfoComparator(actualComparator, _listener);      
   }
   
   private static final class EventFileInfoComparator implements Comparator<FileInfo>
   {
      private final Comparator<FileInfo> _baseComparator;
      private final FolderDiffListener _listener;
      
      public EventFileInfoComparator(Comparator<FileInfo> comparator, FolderDiffListener listener)
      {
         _baseComparator = comparator;
         _listener = listener;
      }

      public int compare(FileInfo o1, FileInfo o2)
      {
         _listener.fileInfosCompared(o1, o2);
         
         return _baseComparator.compare(o1, o2);
      }
   }
   
   public void setFolderDiffListener(FolderDiffListener listener)
   {
      _listener = listener;
   }
   
   public boolean isComparePaths()
   {
      return _comparePaths;
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

   public FileInfoComparatorBuilder setComparePaths(boolean comparePaths)
   {
      _comparePaths = comparePaths;
      
      return this;
   }
   
   public FileInfoComparatorBuilder setCompareSize(boolean compareSize)
   {
      _compareSize = compareSize;
      
      return this;
   }
   
   public FileInfoComparatorBuilder setCompareLastModified(boolean compareLastModified)
   {
      _compareLastModified = compareLastModified;
      
      return this;
   }

   public void setCompareChecksum(boolean compareChecksum)
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
      public int compare(FileInfo o1, FileInfo o2)
      {
         return LONG_COMPARATOR.compare(o1.getChecksum(), o2.getChecksum());
      }
   };

   public static final Comparator<FileInfo> LAST_MOD_COMPARATOR = new Comparator<FileInfo>()
   {
      public int compare(FileInfo o1, FileInfo o2)
      {
         if(o1.isDirectory() && o2.isDirectory())
         {
            return 0;
         }
         else if(o1.isDirectory())
         {
            return -1;
         }
         else if(o2.isDirectory())
         {
            return 1;
         }
         
         return LONG_COMPARATOR.compare(o1.getLastModified(), o2.getLastModified());
      }
   };
   
   public static final Comparator<FileInfo> SIZE_COMPARATOR = new Comparator<FileInfo>()
   {
      public int compare(FileInfo o1, FileInfo o2)
      {
         return LONG_COMPARATOR.compare(o1.getSize(), o2.getSize());
      }
   };
   
   public static final Comparator<PathInfo> PATH_INFO_COMPARATOR = new Comparator<PathInfo>()
   {
      public int compare(PathInfo o1, PathInfo o2)
      {
         List<String> pathFragments1 = o1.getPathFragments();
         List<String> pathFragments2 = o2.getPathFragments();
         
         int size1 = pathFragments1.size();
         int size2 = pathFragments2.size();

         if(size1 == 0 && size2 == 0)
         {
            return 0;
         }
         else if(size1 == 0)
         {
            return -1;
         }
         else if(size2 == 0)
         {
            return 1;
         }
         
         List<String> parentPath1 = pathFragments1.subList(0, size1 - 1);
         List<String> parentPath2 = pathFragments2.subList(0, size2 - 1);
         
         int parentCompare = STRING_LIST_COMPARATOR.compare(parentPath1, parentPath2);
         
         if(parentCompare != 0)
         {
            return parentCompare;
         }
         
         boolean isDirectory1 = o1.isDirectory();
         if(isDirectory1 != o2.isDirectory())
         {
            return isDirectory1 ? -1 : 1;
         }
         
         return pathFragments1.get(size1 - 1).compareTo(pathFragments2.get(size2 - 1));
      }
   };
   
   public static final Comparator<FileInfo> PATH_COMPARATOR = new Comparator<FileInfo>()
   {
      public int compare(FileInfo o1, FileInfo o2)
      {
         return PATH_INFO_COMPARATOR.compare(o1.getPathInfo(), o2.getPathInfo());
      }
   };
}
