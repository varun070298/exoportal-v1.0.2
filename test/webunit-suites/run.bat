rem You need to download or build exo-tomcat version.  Start tomcate before running this command

set CP=./asm-1.4.1.jar;./exoplatform.test.webunit-framework-1.0.jar
set CP=%CP%;./asm-util-1.4.1.jar;./groovy-1.0-beta-7.jar;./jtidy-4aug2000r7-dev.jar
set CP=%CP%;./commons-lang-2.0.jar;./httpunit-1.6.jar;.

java -cp %CP% org.exoplatform.test.web.WebLoadRunner  org/exoplatform/test/portal/Runner.groovy
