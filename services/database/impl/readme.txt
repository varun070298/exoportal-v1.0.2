@version $Id: readme.txt,v 1.1 2004/08/29 21:47:57 benjmestrallet Exp $

Services notes.
===============

exo.services.xml.resolving.impl.xmlcommons.XmlCommonsResolvingServiceImpl
-------------------------------------------------------------------------
Based on catalogs - see  catalog/CatalogManager.properties -> catalogs property
Example of catalog - exo-catalog.xml - OASIS XML format.
NOTE: this service can use filesystem file ONLY as catalog.

exo.services.xml.resolving.impl.simple.SimpleResolvingServiceImpl
-----------------------------------------------------------------
Currently used for internal eXo XML-resolving tasks as use directory (/dtd)
inside jar as local dtds source.
To add new dtd just copy it to this dir and rebuild service jar.
In code:
service.setDtdName(dtdName); // - dtdName - dtd file name
EntityResolver resolver = service.getEntityResolver();

NOTE: In both services - if creation of EntityResolver failed - parser will
try to use systemId for Data Type Description.