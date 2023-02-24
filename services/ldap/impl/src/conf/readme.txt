Basic instructions for configuring ldap support:

1. Install OpenLDAP
2. Configure slapd.conf
	a. Uncomment the following schemas (the path may be different)
		include		"C:/dev/openldap/etc/schema/core.schema"
		include		"C:/dev/openldap/etc/schema/cosine.schema"
		include		"C:/dev/openldap/etc/schema/inetorgperson.schema"
	b.Change the following entries to these
		suffix		"dc=exoplatform,dc=org"
		rootdn		"cn=Manager,dc=exoplatform,dc=org"
		rootpw		secret
3. start OpenLDAP in debug mode: ./slapd -d 1
4. initialize structure in ldap
	a. ./ldapadd -f init.ldif -D cn=Manager,dc=exoplatform,dc=org -w secret
5. stop openldap and restart in non-debug mode.

That's it...
		
