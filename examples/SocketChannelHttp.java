import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

// #start-page#

/* #start-text#
# HTTP SocketChannel Client #

This class uses some `java.nio` classes which make the transfer of data more efficient etc.

#end-text# */

// #start-ignore#
public class SocketChannelHttp {
  public static void main (String[] args) throws IOException {
// #end-ignore#

/* #start-text#

Instead of using a `java.net.Socket` object, we use a `SocketChannel` object. We 'open up' a socket channel using `SocketChannel.open()`, then connect to a chosen remote address using `SocketChannel.connect(remoteAddress)`.

#end-text# */
    URL site = new URL("http://www.cs.bham.ac.uk/");
    int port = 80;

    SocketAddress remoteAddress = new InetSocketAddress(site.getHost(), port);
    SocketChannel server = SocketChannel.open();
    server.connect(remoteAddress);

/* #start-text#
        
Next, we send the request to the server using the `write` method in `SocketChannel`. This takes a byte buffer as argument - to do this we convert the request to a byte array using `request.getBytes()` then create a `ByteBuffer` from this using `ByteBuffer.wrap`.
       
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

    final ByteBuffer outBuffer = ByteBuffer.wrap(request.getBytes());
    server.write(outBuffer); 

/* #start-text#
       
We can similarly use channels to write to files. FileOutputStream provides a method to get the FileChannel.
       
#end-text# */
    File outputFile = new File("web/socket-channel-http.html");
    FileOutputStream fileStream = new FileOutputStream(outputFile);
    FileChannel fileChannel = fileStream.getChannel();

/* #start-text#
        
`SocketChannel` reads bytes into a ByteBuffer, so we need to allocate a new one using `ByteBuffer.allocate`. After reading bytes into the `ByteBuffer` object, we need to flip from reading to writing, then write the contents of the buffer to the file, then clear the buffer for the next set of data.
       
#end-text# */
    int numBytesSent;
    ByteBuffer inBuffer = ByteBuffer.allocate(1 << 13);
    while ((numBytesSent = server.read(inBuffer)) != -1) {
      inBuffer.flip();
      fileChannel.write(inBuffer);
      inBuffer.clear();
    }

/* #start-text#
        
Finally, close the streams: the file channel, then the file stream, then the socket. 
       
#end-text# */
    fileChannel.close();
    fileStream.close();
    server.close();

// #end-page#
  }
}
