rm *.class > /dev/null
rm *.jar > /dev/null

javac ClipClient.java && 
javac ClipServer.java && 
jar cf clip.jar *.class &&
rm *.class > /dev/null
