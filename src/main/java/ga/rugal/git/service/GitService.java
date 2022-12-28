package ga.rugal.git.service;

import java.io.IOException;
import java.util.Collection;
import javax.inject.Inject;

import ga.rugal.git.dao.impl.CompressiveFileFilter;
import ga.rugal.git.dao.impl.FlatFileFilter;
import ga.rugal.git.dto.ProblematicFile;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * Git service.
 *
 * @author Rugal Bernstein
 */
@Slf4j
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GitService {

  CompressiveFileFilter compressive;

  FlatFileFilter flat;

  @Inject
  public GitService(final CompressiveFileFilter compressive,
                    final FlatFileFilter flat) {
    this.compressive = compressive;
    this.flat = flat;
  }

  /**
   * Find large file in repository, of which size is larger than given threshold, also take
   * compression into account.
   *
   * @param sizeInByte filter size threshold
   * @param isCompress if the target file is in compressed
   * @return some large file that match requirement
   * @throws IOException unable to read from repository
   */
  public Collection<ProblematicFile> findLargeFile(final int sizeInByte, final boolean isCompress)
    throws IOException {
    return isCompress
           ? this.compressive.filter(sizeInByte)
           : this.flat.filter(sizeInByte);
  }

  /**
   * Find large file in repository without compression, of which size is larger than given
   * threshold.
   *
   * @param sizeInByte filter size threshold
   * @return some large file that match requirement
   * @throws IOException unable to read from repository
   */
  public Collection<ProblematicFile> findLargeFile(final int sizeInByte)
    throws IOException {
    return this.findLargeFile(sizeInByte, false);
  }
}
