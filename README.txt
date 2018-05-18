EECS 325
Project 1: Proxy
Author: Shannon Stork
---
IMPORTANT:
The proxy is uploaded to the linux lab under home_directory/project1/src within home/scs94; in other words, the full path is home/scs94/home_directory/project1/src.
Once in this directory, you can access all three project files- Proxyd, ProxyThread, and Cache.
They should already be compiled, but compiling again with javac will not hurt.
The only file you need to run to execute the program is Proxyd; read instructions below.
---
Operation instructions:
Command line
1. Navigate to the proper directory.
2. Compile the program using javac.
3. Run by typing "java proxy" and optionally  "-port" followed by another port number to use instead of 5040.
4. Access the web through your browser of choice configured to use the proxy.
Compiler
1. Run the program.
2. Access the web through your browser of choice configured to use the proxy.
---
Browsers used for testing: Chrome, IE
Sites tested: google.com, case.edu, case.edu/admissions, msn.com, cnn.com, cluster41
Except for cnn.com, which threw exceptions before eventually loading, all sites passed testing with no errors.
I got "good job" on cluster41.
---, 
Residual bugs: cnn.com throws connection reset errors, which cause problems accessing the site.