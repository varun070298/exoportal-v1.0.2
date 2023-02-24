/*******************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL All rights reserved. * Please look
 * at license.txt in info directory for more license detail. *
 ******************************************************************************/
package org.exoplatform.services.resources.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.commons.utils.MapResourceBundle;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.container.configuration.ValuesParam;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.database.DBObjectPageList;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.resources.*;

public class ResourceBundleServiceImpl implements ResourceBundleService {
	private static Object NO_SUCH_DATA = new Object();

	static private String[] MAPPING = {
			"org/exoplatform/services/resources/impl/ResourceBundleDescriptionImpl.hbm.xml",
			"org/exoplatform/services/resources/impl/ResourceBundleDataImpl.hbm.xml" };

	final static String[] DEFAULT_USER = { "admin", "demo", "default",
			Constants.DEFAUL_PORTAL_OWNER };

	private HibernateService hService_;

	private Log log_;

	private ExoCache cache_;

	private List persistedPackages_;

	private LocaleConfigService localeService_;

	public ResourceBundleServiceImpl(HibernateService service,
			LocaleConfigService localeService, LogService lservice,
			CacheService cService, ConfigurationManager confService)
			throws Exception {
		log_ = lservice.getLog("org.exoplatform.services.resources");
		cache_ = cService.getCacheInstance(getClass().getName());
		hService_ = service;
		localeService_ = localeService;
		hService_.addMappingFiles(MAPPING);
		ServiceConfiguration sconf = confService
				.getServiceConfiguration(ResourceBundleService.class);
		if (sconf != null) {
			ValuesParam param = sconf.getValuesParam("persisted.packages");
			persistedPackages_ = param.getValues();
		}
	}

	public ResourceBundle getResourceBundle(String name, Locale locale) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return getResourceBundle(name, locale, cl);
	}

	public ResourceBundle getResourceBundle(String name, Locale locale,
			ClassLoader cl) {
		if (!isPersistedResource(name))
			return ResourceBundle.getBundle(name, locale, cl);
		String id = name + "_" + locale.getLanguage();
		try {
			Object obj = cache_.get(id);
			if (obj != null) {
				if (obj == NO_SUCH_DATA)
					return null;
				return (ResourceBundle) obj;
			}
		} catch (Exception ex) {
		}

		try {
			ResourceBundle res = null;
			String rootId = name + "_"
					+ localeService_.getDefaultLocaleConfig().getLanguage();
			ResourceBundle parent = getResourceBundleFromDb(rootId, null);
			if (parent != null) {
				res = getResourceBundleFromDb(id, parent);
				if (res == null)
					res = parent;
				cache_.put(id, res);
				return res;
			}

			if (lookForDefaultResources(name)) {
				loadDefaultResourceBundles(name, cl);
			} else {
				cache_.put(id, NO_SUCH_DATA);
				return null;
			}

			parent = getResourceBundleFromDb(rootId, null);
			if (parent != null) {
				res = getResourceBundleFromDb(id, parent);
				if (res == null)
					res = parent;
				cache_.put(id, res);
				return res;
			}
			cache_.put(id, NO_SUCH_DATA);
		} catch (Exception ex) {
			log_.error("Error: " + id, ex);
		}
		return null;
	}

	public ResourceBundle getResourceBundle(String[] name, Locale locale) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return getResourceBundle(name, locale, cl);
	}

	public ResourceBundle getResourceBundle(String[] name, Locale locale, ClassLoader cl) {
		StringBuffer idBuf = new StringBuffer("merge:");
		for (int i = 0; i < name.length; i++) {
			idBuf.append(name[i]).append("_");
		}
		idBuf.append(locale.getLanguage());
		String id = idBuf.toString();
		try {
			ResourceBundle res = null;
			res = (ResourceBundle) cache_.get(id);
			if (res != null)
				return res;

			MapResourceBundle outputBundled = new MapResourceBundle(locale);
			for (int i = 0; i < name.length; i++) {
				ResourceBundle temp = getResourceBundle(name[i], locale, cl);
				if (temp != null)
					outputBundled.merge(temp);
			}
			outputBundled.resolveDependencies();
			cache_.put(id, outputBundled);
			return outputBundled;
		} catch (Exception ex) {
			log_.error("Cannot load and merge the bundle: " + id, ex);
		}
		return null;
	}

	public ResourceBundleData getResourceBundleData(String name)
			throws Exception {
		return (ResourceBundleDataImpl) hService_.findOne(
				ResourceBundleDataImpl.class, name);
	}

	public ResourceBundleData removeResourceBundleData(String id) {
		ResourceBundleDataImpl data = null;
		try {
			data = (ResourceBundleDataImpl) hService_.remove(
					ResourceBundleDataImpl.class, id);
			cache_.remove(data.getId());
		} catch (Exception ex) {
			log_.error("Remove Error: " + id, ex);
		}
		return data;
	}

	public PageList findResourceDescriptions(Query q) throws Exception {
		String name = q.getName();
		if (name == null || name.length() == 0)
			name = "%";
		ObjectQuery oq = new ObjectQuery(ResourceBundleDescriptionImpl.class);
		oq.addLIKE("name", name);
		oq.addLIKE("language", q.getLanguage());
		oq.setDescOrderBy("name");
		return new DBObjectPageList(hService_, oq);
	}

	public void saveResourceBundle(ResourceBundleData data) throws Exception {
		hService_.save(data);
		cache_.remove(data.getId());
	}

	private ResourceBundle getResourceBundleFromDb(String id,
			ResourceBundle parent) throws Exception {
		Session session = hService_.openSession();
		ResourceBundleData data = (ResourceBundleDataImpl) session.get(
				ResourceBundleDataImpl.class, id);
		if (data != null) {
			InputStream is = new ByteArrayInputStream(data.getData().getBytes());
			ResourceBundle res = new ExoResourceBundle(is, parent);
			return res;
		}
		return null;
	}

	private void loadDefaultResourceBundles(String baseName, ClassLoader cl) {
		String name = baseName.replace('.', '/');
		String fileName = null;
		try {
			Collection localeConfigs = localeService_.getLocalConfigs();
			String defaultLang = localeService_.getDefaultLocaleConfig()
					.getLanguage();
			for (Iterator iter = localeConfigs.iterator(); iter.hasNext();) {
				LocaleConfig localeConfig = (LocaleConfig) iter.next();
				String language = localeConfig.getLanguage();
				if (defaultLang.equals(language)) {
					fileName = name + ".properties";
				} else {
					fileName = name + "_" + language + ".properties";
				}
				URL url = cl.getResource(fileName);
				if (url != null) {
					InputStream is = url.openStream();
					byte buf[] = IOUtil.getStreamContentAsBytes(is);
					ResourceBundleDataImpl data = new ResourceBundleDataImpl();
					data.setName(baseName);
					data.setLanguage(language);
					data.setData(new String(buf, "UTF-8"));
					saveResourceBundle(data);
				}
			}
		} catch (Exception ex) {
			log_.error("Error while reading the file: " + fileName, ex);
		}
	}

	public ResourceBundleData createResourceBundleDataInstance() {
		return new ResourceBundleDataImpl();
	}

	private boolean lookForDefaultResources(String name) {
		if (name.startsWith("locale.users.")) {
			for (int i = 0; i < DEFAULT_USER.length; i++) {
				if (name.endsWith(DEFAULT_USER[i]))
					return true;
			}
			return false;
		}
		return true;
	}

	private boolean isPersistedResource(String name) {
		if (persistedPackages_ == null)
			return false;
		for (int i = 0; i < persistedPackages_.size(); i++) {
			String pack = (String) persistedPackages_.get(i);
			if (name.startsWith(pack))
				return true;
		}
		return false;
	}
}
