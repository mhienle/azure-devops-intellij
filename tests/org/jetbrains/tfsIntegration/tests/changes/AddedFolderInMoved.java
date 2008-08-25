/*
 * Copyright 2000-2008 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.tfsIntegration.tests.changes;

import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@SuppressWarnings({"HardCodedStringLiteral"})
public class AddedFolderInMoved extends ChangeTestCase {
  private FilePath myOriginalParentFolder;
  private FilePath myMovedParentFolder;
  private FilePath myAddedFolderInOriginalFolder;
  private FilePath myAddedFolderInMovedFolder;

  private FilePath mySubfolder1;
  private FilePath mySubfolder2;

  protected void preparePaths() {
    mySubfolder1 = getChildPath(mySandboxRoot, "Subfolder1");
    mySubfolder2 = getChildPath(mySubfolder1, "Subfolder2");

    final String folderName = "Folder";
    myOriginalParentFolder = getChildPath(mySandboxRoot, folderName);
    myMovedParentFolder = getChildPath(mySubfolder2, folderName);

    final String filename = "AddedFolder";
    myAddedFolderInOriginalFolder = getChildPath(myOriginalParentFolder, filename);
    myAddedFolderInMovedFolder = getChildPath(myMovedParentFolder, filename);
  }

  protected void checkParentChangesPendingChildRolledBack() throws VcsException {
    getChanges().assertTotalItems(2);
    getChanges().assertRenamedOrMoved(myOriginalParentFolder, myMovedParentFolder);
    getChanges().assertUnversioned(myAddedFolderInMovedFolder);

    assertFolder(mySandboxRoot, 1);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 1);
    assertFolder(myMovedParentFolder, 1);
    assertFolder(myAddedFolderInMovedFolder, 0);
  }

  protected void checkChildChangePendingParentRolledBack() throws VcsException {
    getChanges().assertTotalItems(1);
    getChanges().assertScheduledForAddition(myAddedFolderInOriginalFolder);

    assertFolder(mySandboxRoot, 2);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 0);
    assertFolder(myOriginalParentFolder, 1);
    assertFolder(myAddedFolderInOriginalFolder, 0);
  }

  protected void checkParentAndChildChangesPending() throws VcsException {
    getChanges().assertTotalItems(2);
    getChanges().assertRenamedOrMoved(myOriginalParentFolder, myMovedParentFolder);
    getChanges().assertScheduledForAddition(myAddedFolderInMovedFolder);

    assertFolder(mySandboxRoot, 1);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 1);
    assertFolder(myMovedParentFolder, 1);
    assertFolder(myAddedFolderInMovedFolder, 0);
  }

  protected void checkOriginalStateAfterRollbackParentChild() throws VcsException {
    getChanges().assertTotalItems(1);
    getChanges().assertUnversioned(myAddedFolderInOriginalFolder);

    assertFolder(mySandboxRoot, 2);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 0);
    assertFolder(myOriginalParentFolder, 1);
    assertFolder(myAddedFolderInOriginalFolder, 0);
  }

  protected void checkOriginalStateAfterUpdate() throws VcsException {
    getChanges().assertTotalItems(0);

    assertFolder(mySandboxRoot, 2);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 0);
    assertFolder(myOriginalParentFolder, 0);
  }

  protected void checkParentChangesCommittedChildPending() throws VcsException {
    getChanges().assertTotalItems(1);
    getChanges().assertScheduledForAddition(myAddedFolderInMovedFolder);

    assertFolder(mySandboxRoot, 1);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 1);
    assertFolder(myMovedParentFolder, 1);
    assertFolder(myAddedFolderInMovedFolder, 0);
  }

  protected void checkChildChangeCommittedParentPending() throws VcsException {
    getChanges().assertTotalItems(1);
    getChanges().assertRenamedOrMoved(myOriginalParentFolder, myMovedParentFolder);

    assertFolder(mySandboxRoot, 1);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 1);
    assertFolder(myMovedParentFolder, 1);
    assertFolder(myAddedFolderInMovedFolder, 0);
  }

  protected void checkParentChangesPending() throws VcsException {
    getChanges().assertTotalItems(1);
    getChanges().assertRenamedOrMoved(myOriginalParentFolder, myMovedParentFolder);

    assertFolder(mySandboxRoot, 1);
    assertFolder(myMovedParentFolder, 0);
  }

  protected void checkChildChangePending() throws VcsException {
    getChanges().assertTotalItems(1);
    getChanges().assertScheduledForAddition(myAddedFolderInOriginalFolder);

    assertFolder(mySandboxRoot, 2);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 0);
    assertFolder(myOriginalParentFolder, 1);
    assertFolder(myAddedFolderInOriginalFolder, 0);
  }

  protected void checkParentChangesCommitted() throws VcsException {
    getChanges().assertTotalItems(0);

    assertFolder(mySandboxRoot, 1);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 1);
    assertFolder(myMovedParentFolder, 0);
  }

  protected void checkChildChangeCommitted() throws VcsException {
    getChanges().assertTotalItems(0);

    assertFolder(mySandboxRoot, 2);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 0);
    assertFolder(myOriginalParentFolder, 1);
    assertFolder(myAddedFolderInOriginalFolder, 0);
  }

  protected void checkParentAndChildChangesCommitted() throws VcsException {
    getChanges().assertTotalItems(0);

    assertFolder(mySandboxRoot, 1);
    assertFolder(mySubfolder1, 1);
    assertFolder(mySubfolder2, 1);
    assertFolder(myMovedParentFolder, 1);
    assertFolder(myAddedFolderInMovedFolder, 0);
  }

  protected void makeOriginalState() throws VcsException {
    createDirInCommand(myOriginalParentFolder);
    createDirInCommand(mySubfolder1);
    createDirInCommand(mySubfolder2);
  }

  protected void makeParentChanges() throws VcsException {
    moveFileInCommand(myOriginalParentFolder, mySubfolder2);
  }

  protected void makeChildChange(ParentChangesState parentChangesState) throws VcsException {
    FilePath folder = parentChangesState == ParentChangesState.NotDone ? myAddedFolderInOriginalFolder : myAddedFolderInMovedFolder;
    if (folder.getIOFile().exists()) {
      scheduleForAddition(folder);
    }
    else {
      createDirInCommand(folder);
    }
  }

  protected Collection<Change> getPendingParentChanges() throws VcsException {
    final Change change = getChanges().getMoveChange(myOriginalParentFolder, myMovedParentFolder);
    return change != null ? Collections.singletonList(change) : Collections.<Change>emptyList();
  }

  protected Change getPendingChildChange(ParentChangesState parentChangesState) throws VcsException {
    return getChanges()
      .getAddChange(parentChangesState == ParentChangesState.NotDone ? myAddedFolderInOriginalFolder : myAddedFolderInMovedFolder);
  }

  @Test
  public void testPendingAndRollback() throws VcsException, IOException {
    super.testPendingAndRollback();
  }

  @Test
  public void testCommitParentThenChildChanges() throws VcsException, IOException {
    super.testCommitParentThenChildChanges();
  }

  @Test
  public void testCommitChildThenParentChanges() throws VcsException, IOException {
    super.testCommitChildThenParentChanges();
  }

  @Test
  public void testCommitParentChangesChildPending() throws VcsException, IOException {
    super.testCommitParentChangesChildPending();
  }

  // don't test this, see remark 1 in AddedFolderInRenamed
  //@Test
  //public void testCommitChildChangesParentPending() throws VcsException, IOException {
  //  super.testCommitChildChangesParentPending();
  //}

}