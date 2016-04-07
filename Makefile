all:
	./join-tuts examples/HttpSocket.java examples/SocketChannelHttp.java > notes.md > notes.md
	./convert-markdown notes.md # Remove this line if not converting to pdf
