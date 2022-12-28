package ga.rugal.git.dao;

import java.io.IOException;
import java.util.Collection;

import ga.rugal.git.dto.ProblematicFile;

/**
 * Filter for getting problematic files that are larger than given threshold.
 *
 * @author Rugal Bernstein
 */
public interface FileFilter {

  /**
   * Filter file by given threshold.
   *
   * @param sizeInByte the size to filter with
   * @return list of matching problematic files
   * @throws IOException unable to read from repository
   */
  Collection<ProblematicFile> filter(int sizeInByte) throws IOException;
}
