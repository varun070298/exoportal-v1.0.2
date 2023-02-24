/***************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform;

import javax.xml.transform.Result;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: PipeTransformer.java 565 2005-01-25 12:48:13Z kravchuk $
 */

public interface PipeTransformer extends AbstractTransformer {
    Result getTransformerAsResult();
}
