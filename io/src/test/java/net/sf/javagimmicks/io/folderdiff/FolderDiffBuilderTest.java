package net.sf.javagimmicks.io.folderdiff;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import net.sf.javagimmicks.io.FileUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FolderDiffBuilderTest
{
   @Rule
   public TemporaryFolder _sourceFolder = new TemporaryFolder();

   @Rule
   public TemporaryFolder _targetFolder = new TemporaryFolder();

   private FolderDiffBuilder _builder;

   @Before
   public void setup() throws IllegalArgumentException, IOException
   {
      final File sourceFolder = _sourceFolder.getRoot();
      FileUtils.unzip(getClass().getResourceAsStream("source.zip"), sourceFolder);

      final File targetFolder = _targetFolder.getRoot();
      FileUtils.unzip(getClass().getResourceAsStream("target.zip"), targetFolder);

      _builder = new FolderDiffBuilder(sourceFolder, targetFolder);
   }

   @After
   public void tearDown()
   {
      _builder = null;
   }

   @Test
   public void testWithChecksum()
   {
      _builder.setCompareSize(true);
      _builder.setCompareChecksum(true);

      final FolderDiff diff = _builder.buildFolderDiff();

      final SortedSet<PathInfo> equal = diff.getEqual();
      assertPathInfosEqual(equal, new String[][] {
            { "DifferentFolder", "EqualFile.txt" },
            { "DifferentFolder" },
            { "EqualFolder", "EqualFile.txt" },
            { "EqualFolder" },
            { "EqualFile.txt" },
      });

      final SortedSet<PathInfo> different = diff.getDifferent();
      assertPathInfosEqual(different, new String[][] {
            { "DifferentFolder", "DifferentFile.txt" },
            { "DifferentFile.txt" },
      });

      final SortedSet<PathInfo> sourceOnly = diff.getSourceOnly();
      assertPathInfosEqual(sourceOnly, new String[][] {
            { "SourceOnlyFolder", "EqualFile.txt" },
            { "SourceOnlyFolder" },
            { "SourceOnlyFile.txt" },
      });

      final SortedSet<PathInfo> targetOnly = diff.getTargetOnly();
      assertPathInfosEqual(targetOnly, new String[][] {
            { "TargetOnlyFolder", "EqualFile.txt" },
            { "TargetOnlyFolder" },
            { "TargetOnlyFile.txt" },
      });
   }

   @Test
   public void testWithoutChecksum()
   {
      _builder.setCompareSize(true);
      _builder.setCompareChecksum(false);

      final FolderDiff diff = _builder.buildFolderDiff();

      final SortedSet<PathInfo> equal = diff.getEqual();
      assertPathInfosEqual(equal, new String[][] {
            { "DifferentFolder", "DifferentFile.txt" },
            { "DifferentFolder", "EqualFile.txt" },
            { "DifferentFolder" },
            { "EqualFolder", "EqualFile.txt" },
            { "EqualFolder" },
            { "DifferentFile.txt" },
            { "EqualFile.txt" },
      });

      final SortedSet<PathInfo> different = diff.getDifferent();
      assertPathInfosEqual(different, new String[0][]);

      final SortedSet<PathInfo> sourceOnly = diff.getSourceOnly();
      assertPathInfosEqual(sourceOnly, new String[][] {
            { "SourceOnlyFolder", "EqualFile.txt" },
            { "SourceOnlyFolder" },
            { "SourceOnlyFile.txt" },
      });

      final SortedSet<PathInfo> targetOnly = diff.getTargetOnly();
      assertPathInfosEqual(targetOnly, new String[][] {
            { "TargetOnlyFolder", "EqualFile.txt" },
            { "TargetOnlyFolder" },
            { "TargetOnlyFile.txt" },
      });
   }

   private static void assertPathInfosEqual(final SortedSet<PathInfo> actual, final String[][] expected)
   {
      Assert.assertNotNull("Expected paths are null!", expected);
      Assert.assertEquals(String.format("Path info size does not match! Actual PathInfos: %1$s", actual),
            expected.length, actual.size());

      for (final String[] currentExpectedArray : expected)
      {
         boolean found = false;

         final List<String> currentExpected = Arrays.asList(currentExpectedArray);

         for (final PathInfo currentActualPathInfo : actual)
         {
            if (currentExpected.equals(currentActualPathInfo.getPathFragments()))
            {
               found = true;
               break;
            }
         }

         if (!found)
         {
            Assert.fail(String.format("Expected PathInfo '%1$s' not contained in actual PathInfo set '%2$s'!",
                  currentExpected, actual));
         }
      }
   }
}
