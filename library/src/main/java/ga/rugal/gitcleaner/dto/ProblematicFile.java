package ga.rugal.gitcleaner.dto;

import javax.annotation.Nullable;

/**
 *
 * @author Rugal Bernstein
 */
public record ProblematicFile(String blobId, String path, long size, @Nullable String commitId) {

  public ProblematicFile(String blobId, String path, long size) {
    this(blobId, path, size, null);
  }
}
