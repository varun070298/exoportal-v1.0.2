/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.idgenerator;

import java.io.Serializable;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 14, 2004
 * @version $Id: IDGeneratorService.java,v 1.1 2004/10/14 23:31:46 tuan08 Exp $
 */
public interface IDGeneratorService {
  public Serializable generateID(Object o) ;
  public String generateStringID(Object o)  ; 
  public long   generateLongID(Object o)  ; 
}