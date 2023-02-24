package org.exoplatform.portlet.faces.lifecycle;

import java.util.*;
import javax.faces.FacesException;
import javax.faces.event.PhaseId;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogUtil;

import com.sun.faces.util.Util;
import com.sun.faces.lifecycle.* ;

public class ExoLifecycleFactory extends LifecycleFactory {
	protected static Log log_ = LogUtil.getLog("org.exoplatform.portal.faces") ;
  static final int FIRST_PHASE;
  static final int LAST_PHASE;
  protected HashMap lifecycleMap;

  static 
  {
    FIRST_PHASE = PhaseId.RESTORE_VIEW.getOrdinal();
    LAST_PHASE = PhaseId.RENDER_RESPONSE.getOrdinal();
  }

  static class LifecycleWrapper {
    Lifecycle instance;
    boolean created;

    LifecycleWrapper(Lifecycle newInstance, boolean newCreated) {
      instance = null;
      created = false;
      instance = newInstance;
      created = newCreated;
    }
  }


  public ExoLifecycleFactory() {
    lifecycleMap = null;
    lifecycleMap = new HashMap();
    lifecycleMap.put("DEFAULT", new LifecycleWrapper(new ExoLifecycle(), false));
  }

  boolean alreadyCreated(String lifecycleId) {
    LifecycleWrapper wrapper = (LifecycleWrapper)lifecycleMap.get(lifecycleId);
    return null != wrapper && wrapper.created;
  }

  Lifecycle verifyRegisterArgs(String lifecycleId, int phaseId, Phase phase) {
    String message = null;
    LifecycleWrapper wrapper = null;
    Lifecycle result = null;
    Object params[] = { lifecycleId };
    if(null == lifecycleId || null == phase)
      throw new NullPointerException("lifecycleId or phase is null");
    if(null == (wrapper = (LifecycleWrapper)lifecycleMap.get(lifecycleId))) {
      message = "lifecycle id is not found" ;
      throw new IllegalArgumentException(message);
    }
    result = wrapper.instance;
    Util.doAssert(null != result);
    if(alreadyCreated(lifecycleId)) {
      message = "LIFECYCLE ID ALREADY ADDED"  ;
      throw new IllegalStateException(message);
    }

    if(FIRST_PHASE > phaseId || phaseId > LAST_PHASE) {
      params = (new Object[] { Integer.toString(phaseId) });
      message = "PHASE ID OUT OF BOUNDS" ;
      throw new IllegalArgumentException(message);
    } else {
      return result;
    }
  }

  public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
    if(lifecycleId == null || lifecycle == null)
      throw new NullPointerException("NULL PARAMETERS ERROR");
    if(null != lifecycleMap.get(lifecycleId)) {
      Object params[] = { lifecycleId };
      String message = "LIFECYCLE ID ALREADY ADDED" ;
      throw new IllegalArgumentException(message);
    }
    lifecycleMap.put(lifecycleId, new LifecycleWrapper(lifecycle, false));
  }

  public Lifecycle getLifecycle(String lifecycleId) throws FacesException {
    Lifecycle result = null;
    LifecycleWrapper wrapper = null;
    if(null == lifecycleId)
      throw new NullPointerException("com.sun.faces.NULL_PARAMETERS_ERROR") ;
    try {
      wrapper = (LifecycleWrapper)lifecycleMap.get(lifecycleId);
      result = wrapper.instance;
      wrapper.created = true;
    } catch(Throwable e) {
      Object params[] = { lifecycleId };
      String message = "CANT CREATE LIFECYCLE ERROR" ;
      throw new FacesException(message);
    }
    return result;
  }

  public Iterator getLifecycleIds() {
    return lifecycleMap.keySet().iterator();
  }
}
