# java-stager
A PoC Java Stager which can download, compile, and execute a Java file in memory.

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

Assuming you have generated the same Jar file the stager will run if you execute this in the "java-stager" folder:

`java -cp target/JavaStager-0.1-initial.jar Stager`

As I was just doing a PoC I have not figured out how to make Maven do all the nice things to make this a simpler Jar to execute. But who knows you can probably run with the ball here. To test I uploaded the full "java-stager" folder to a VM (playing the victim) which isn't that mobile but was sufficient.

The above will call the "Stager" class which has a main method. You will be prompted with the usage as shown:

`Proper Usage is: java Stager <url>`

The "url" is the only parameter that is passed to Stager. 

Configure your "payload.java" file to service your needs. In my PoC the Stager will invoke the "revshell" method in the "payload" object. Before looking to exploit a system you will want to check that your payload compiles cleanly or it will fail on the victim. When you are happy you can host the ".java" file (uncompiled) on a web server. I have a blog post covering basic HTTP/HTTPS servers with python 2 and 3 available here:

https://cornerpirate.com/2016/12/16/simple-http-or-https-servers/

