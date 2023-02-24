package org.exoplatform.services.threadpool.impl;

/**
 * $Id: ThreadFactoryIF.java,v 1.1.1.1 2004/03/05 21:56:48 benjmestrallet Exp $
 *
 * The contents of this file are subject to the ClickBlocks Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.clickblocks.org
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied, including, but not limited to, the implied warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement. See the License for the specific language
 * governing rights and limitations under the License.
 * 
 * ClickBlocks, the ClickBlocks logo and combinations thereof are
 * trademarks of ClickBlocks, LLC in the United States and other
 * countries.
 * 
 * The Initial Developer of the Original Code is ClickBlocks, LLC.
 * Portions created by ClickBlocks, LLC are Copyright (C) 2000.  
 * All Rights Reserved.
 *
 * Contributor(s): Mark Grand
 */

/**
 * This interface in implemented by objects that create Thread
 * objects.  Classes that create Thread objects through this
 * interface can be passed a ThreadFactoryIF object that
 * creates an instance of Thread or a subclass of Thread with
 * different properties.
 */
public interface ThreadFactoryIF {
    /**
     * Return a Thread that runs the given Runnable object.
     */
    public Thread createThread(Runnable r);
} // interface ThreadFactoryIF
