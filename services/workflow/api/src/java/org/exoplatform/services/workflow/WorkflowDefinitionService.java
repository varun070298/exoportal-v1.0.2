package org.exoplatform.services.workflow;

import org.jbpm.Assembler;
import org.jbpm.JpdlException;

import java.util.List;
import java.util.jar.JarInputStream;
import java.io.IOException;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 9 mai 2004
 *
 * interface abstracted from JBPM
 */
public interface WorkflowDefinitionService {

  public void deployProcessArchive(JarInputStream processArchiveStream)
      throws JpdlException, IOException;

  public void close();

}
