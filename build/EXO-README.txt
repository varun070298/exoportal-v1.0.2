1) How to run tomcat

 * simply go to exo-tomcat/bin and click on startup.bat
 * If you use bash shell, run ./startup.sh or ./run.sh. You may need to change mode (chmode +x *.sh) of all the .sh files.

2) How to run jetty

 * use command: java -Djetty.server=org.mortbay.jetty.plus.Server -Djava.security.auth.login.config=etc/login.conf  -Djava.io.tmpdir=work -jar start.jar

2) How to deploy ear file

 * unzip the exoplatform-ear-XYZ.zip, You will find the exo.ear dir and exo-jboss, exo-jonas dir, The exo-jboss contains the the patched files that need to configure your jboss and jonas like datasource, login-config.xml.
   
 * copy the exo.ear directory to your jboss deploy dir
 
 * Patch your jboss with the files in exo-jboss dir, or configure datasource and 
   login-config.xml by your self.
 
 * copy exo.ear to the autoload dir

 * Patch your jonas with the files in the exo-jonas dir

 We tested with jboss version 3.2.6.

 We tested with the jonas version  4.2.3.

3)How to access the exo portal

  use http://localhost:8080/portal/faces/public/exo (or  replace exo with an username)
  use http://localhost:8080/portal/
  use http://localhost:8080/portal/bookmark.jsp

  Be carefull, JOnAS port is the 9000 and not 8080.

4) how to access the demo portal (after you deployed it using the sample distribution)

  use http://localhost:8080/ic3 and you can use default patient/exo to login the demo portal
  
5) To build the exoplatform from the src
   
Go to our www.exoplatform.com and go to communities page then wiki page, look for the build instruction page or use http://www.exoplatform.com/xwiki/bin/view/Main/buildinstructions
   
For more documentation and latest updated news, please visit our website www.exoplatform.com.  If you have more questions.
