import java.net.*;
import java.io.*;

// #start-page#

/*#start-text#

# HTTP Socket Client #
The following program sends a GET request to a server in order to retrieve the contents of a web page. This involves three main steps:
1. Create the socket to the web server
2. Send the GET request to the web server using the socket's output stream.
3. Read the response from the server into a file 

#end-text# */

/* #start-text#

We specify the server/destination address using a URL object, and then we can get the host name using `site.getHost()`, which in this case will return `"www.cs.bham.ac.uk"`.

#end-text# */ 

// #start-ignore#
public class HttpSocket {
  public static void main (String[] args) throws IOException {
// #end-ignore#

    URL site = new URL("http://www.cs.bham.ac.uk/");
    int port = 80;

    Socket server = new Socket(site.getHost(), port);

/* #start-text#
We can save the input/output streams now for later use. Notice that we use a PrintWriter for the output stream (with auto-flushing) because we will create our GET request message using a String. The InputStream is an input stream of bytes, since we want to read bytes straight from the stream into a file.
#end-text# */

    PrintWriter out = new PrintWriter(server.getOutputStream(), true);
    InputStream in = server.getInputStream();

/* #start-text#
We write the request message in a string, and then use the PrintWriter to println the request in the output stream.
#end-text# */

    String request = String.format(
        "GET %s HTTP/1.1%n"
        + "User-Agent: HTTPGrab%n"
        + "Accept: text/*%n"
        + "Connection: close%n"
        + "Host: %s"
        + "%n%n"
        , "/", site.getHost()
        );
    out.println(request);

/* #start-text#
Create a new destination file for the data and use the `fileStream` object to write the bytes to the file.
#end-text# */


    File outputFile = new File("web/http-socket.html");
    OutputStream fileStream = new FileOutputStream(outputFile);

/* #start-text#
We read bytes from the socket into `buffer` until no more bytes are sent (indicated by `in.read(buffer) == -1`). We write to the file using `fileStream`'s `write` method.
#end-text# */

    int numBytesSent;
    byte[] buffer = new byte[1024]; 

    while ((numBytesSent = in.read(buffer)) != -1) {
      fileStream.write(buffer, 0, numBytesSent);
    }

/* #start-text#
Finally we close IO streams. First we close the file stream and the socket's IO streams, then we close the socket. 
#end-text# */


    fileStream.close();
    in.close();
    out.close();
    server.close();

// #end-page#
  }
}
