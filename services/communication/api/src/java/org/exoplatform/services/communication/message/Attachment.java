package org.exoplatform.services.communication.message;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 2 sept. 2004
 */
public interface Attachment {

  public String getName();
  public void   setName(String name) ;

  public byte[] getContent(); 
  public void   setContent(byte[] content) ;
}
