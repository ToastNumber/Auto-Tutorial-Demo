# HTTP Socket Client #
The following program sends a GET request to a server in order to retrieve the contents of a web page. This involves three main steps:
1. Create the socket to the web server
2. Send the GET request to the web server using the socket's output stream.
3. Read the response from the server into a file 

We specify the server/destination address using a URL object, and then we can get the host name using `site.getHost()`, which in this case will return `"www.cs.bham.ac.uk"`.

```java
URL site = new URL("http://www.cs.bham.ac.uk/");
int port = 80;

Socket server = new Socket(site.getHost(), port);

```

We can save the input/output streams now for later use. Notice that we use a PrintWriter for the output stream (with auto-flushing) because we will create our GET request message using a String. The InputStream is an input stream of bytes, since we want to read bytes straight from the stream into a file.

```java
PrintWriter out = new PrintWriter(server.getOutputStream(), true);
InputStream in = server.getInputStream();

```

We write the request message in a string, and then use the PrintWriter to println the request in the output stream.

```java
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

```

Create a new destination file for the data and use the `fileStream` object to write the bytes to the file.

```java
File outputFile = new File("web/http-socket.html");
OutputStream fileStream = new FileOutputStream(outputFile);

```

We read bytes from the socket into `buffer` until no more bytes are sent (indicated by `in.read(buffer) == -1`). We write to the file using `fileStream`'s `write` method.

```java
int numBytesSent;
byte[] buffer = new byte[1024]; 

while ((numBytesSent = in.read(buffer)) != -1) {
  fileStream.write(buffer, 0, numBytesSent);
}

```

Finally we close IO streams. First we close the file stream and the socket's IO streams, then we close the socket. 

```java
fileStream.close();
in.close();
out.close();
server.close();
```

# HTTP SocketChannel Client #

This class uses some `java.nio` classes which make the transfer of data more efficient etc.

Instead of using a `java.net.Socket` object, we use a `SocketChannel` object. We 'open up' a socket channel using `SocketChannel.open()`, then connect to a chosen remote address using `SocketChannel.connect(remoteAddress)`.

```java
URL site = new URL("http://www.cs.bham.ac.uk/");
int port = 80;

SocketAddress remoteAddress = new InetSocketAddress(site.getHost(), port);
SocketChannel server = SocketChannel.open();
server.connect(remoteAddress);

```

Next, we send the request to the server using the `write` method in `SocketChannel`. This takes a byte buffer as argument - to do this we convert the request to a byte array using `request.getBytes()` then create a `ByteBuffer` from this using `ByteBuffer.wrap`.

```java
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

```

We can similarly use channels to write to files. FileOutputStream provides a method to get the FileChannel.

```java
File outputFile = new File("web/socket-channel-http.html");
FileOutputStream fileStream = new FileOutputStream(outputFile);
FileChannel fileChannel = fileStream.getChannel();

```

`SocketChannel` reads bytes into a ByteBuffer, so we need to allocate a new one using `ByteBuffer.allocate`. After reading bytes into the `ByteBuffer` object, we need to flip from reading to writing, then write the contents of the buffer to the file, then clear the buffer for the next set of data.

```java
int numBytesSent;
ByteBuffer inBuffer = ByteBuffer.allocate(1 << 13);
while ((numBytesSent = server.read(inBuffer)) != -1) {
  inBuffer.flip();
  fileChannel.write(inBuffer);
  inBuffer.clear();
}

```

Finally, close the streams: the file channel, then the file stream, then the socket. 

```java
fileChannel.close();
fileStream.close();
server.close();
```

