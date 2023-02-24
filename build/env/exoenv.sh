BASE_DIRECTORY=/cygdrive/f/java

JAVA_HOME=/cygdrive/c/j2sdk1.4.2_05
SVN_HOME='/cygdrive/c/Program Files/Subversion/bin'
#JAVA_HOME=/cygdrive/c/jdk1.5.0
JIKES_HOME=$BASE_DIRECTORY/jikes
MAVEN_HOME=$BASE_DIRECTORY/maven
ANT_HOME=$BASE_DIRECTORY/ant
GROOVY_HOME=$BASE_DIRECTORY/groovy

PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$MAVEN_HOME/bin:$JIKES_HOME/bin:$PATH:$SVN_HOME:$GROOVY_HOME/bin

JAVA_OPTS="-server -Xms128m -Xmx256m" 
#MAVEN_OPTS="-Xms128m -Xmx256m -Dexoplatform.src.path=h:/java/projects/exoplatform"

CVS_RSH=ssh

CVSROOT=:ext:tuan08@cvs.sourceforge.net:/cvsroot/exo

export JAVA_OPTS JAVA_HOME MAVEN_HOME ANT_HOME  CVS_RSH CVSROOT

alias ls="ls --color"
alias back='cd  $OLDPWD'
alias tomcatlog="tail -n 20 $TOMCAT_HOME/logs/catalina.out"
alias sql="mysql -t -u dev -pdev dev"
alias vibuild="vi /cygdrive/c/Documents\ and\ Settings/Tuan\ Nguyen/build.properties"
