# XDM tries to download "components" from non-existent URL (#761)

[XDM tries to download "components" from non-existent URL](https://github.com/subhra74/xdm/issues/761#top)

[token0](https://github.com/token0)
[on Jan 14, 2022](https://github.com/subhra74/xdm/issues/761#issue-1103594910)
Describe the bug XDM tries to download "components" from [http://xdman.sourceforge.net/components/linux64.zip](http://xdman.sourceforge.net/components/linux64.zip) which is apparently a dead link
To Reproduce Steps to reproduce the behavior:
1. Build XDM from source
2. Set it up, install a browser extension
3. Try to download something, so XDM asks to download its components
4. Get an error
- OS: Linux
- Browser Vivaldi
- XDM addon Version [e.g. 2.0]
- XDM Version 7.2.11 built from git source
Generated log using below method

```
[ Thread-2 ] Response set [ AWT-EventQueue-0 ] Closed [ AWT-EventQueue-0 ] derive hls metadata [ AWT-EventQueue-0 ] dash metdata ? false http://xdman.sourceforge.net/components/linux64.zip [ AWT-EventQueue-0 ] creating folder /home/user/.xdman/temp/4bd6db79-5e55-4b48-a85c-803a327651a6 [ AWT-EventQueue-0 ] File opened 35125803-0541-4526-9b08-1b3a64360ce3 Headers all: [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Connecting to: http://xdman.sourceforge.net/components/linux64.zip null [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Initating connection [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Creating new socket [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Tcp RWin: 65536 Loading image from url: jar:file:/home/user/xdm/app/target/xdman.jar!/icons/xxhdpi/icon.png [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Sending request: GET /components/linux64.zip HTTP/1.1 Cookie: Range: bytes=0- host: xdman.sourceforge.net [ 35125803-0541-4526-9b08-1b3a64360ce3 ] HTTP/1.1 404 Not Found [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Server: nginx Date: Fri, 14 Jan 2022 12:37:47 GMT Content-Type: text/html Content-Length: 1688 Connection: keep-alive Vary: Accept-Encoding Vary: Host X-From: sfp-web-2 [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Content-Encoding: null [ 35125803-0541-4526-9b08-1b3a64360ce3 ] 35125803-0541-4526-9b08-1b3a64360ce3: 404 [ 35125803-0541-4526-9b08-1b3a64360ce3 ] 35125803-0541-4526-9b08-1b3a64360ce3 notifying failure xdman.downloaders.http.HttpChannel@741c692c [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Deleting metadata for 4bd6db79-5e55-4b48-a85c-803a327651a6 [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Deleted manifest 4bd6db79-5e55-4b48-a85c-803a327651a6 false [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Deleted tmp file 4bd6db79-5e55-4b48-a85c-803a327651a6 true [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Deleted tmp folder 4bd6db79-5e55-4b48-a85c-803a327651a6 true [ 35125803-0541-4526-9b08-1b3a64360ce3 ] failed
```


```
[ Thread-2 ] Response set [ AWT-EventQueue-0 ] Closed [ AWT-EventQueue-0 ] derive hls metadata [ AWT-EventQueue-0 ] dash metdata ? false http://xdman.sourceforge.net/components/linux64.zip [ AWT-EventQueue-0 ] creating folder /home/user/.xdman/temp/4bd6db79-5e55-4b48-a85c-803a327651a6 [ AWT-EventQueue-0 ] File opened 35125803-0541-4526-9b08-1b3a64360ce3 Headers all: [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Connecting to: http://xdman.sourceforge.net/components/linux64.zip null [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Initating connection [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Creating new socket [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Tcp RWin: 65536 Loading image from url: jar:file:/home/user/xdm/app/target/xdman.jar!/icons/xxhdpi/icon.png [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Sending request: GET /components/linux64.zip HTTP/1.1 Cookie: Range: bytes=0- host: xdman.sourceforge.net [ 35125803-0541-4526-9b08-1b3a64360ce3 ] HTTP/1.1 404 Not Found [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Server: nginx Date: Fri, 14 Jan 2022 12:37:47 GMT Content-Type: text/html Content-Length: 1688 Connection: keep-alive Vary: Accept-Encoding Vary: Host X-From: sfp-web-2 [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Content-Encoding: null [ 35125803-0541-4526-9b08-1b3a64360ce3 ] 35125803-0541-4526-9b08-1b3a64360ce3: 404 [ 35125803-0541-4526-9b08-1b3a64360ce3 ] 35125803-0541-4526-9b08-1b3a64360ce3 notifying failure xdman.downloaders.http.HttpChannel@741c692c [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Deleting metadata for 4bd6db79-5e55-4b48-a85c-803a327651a6 [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Deleted manifest 4bd6db79-5e55-4b48-a85c-803a327651a6 false [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Deleted tmp file 4bd6db79-5e55-4b48-a85c-803a327651a6 true [ 35125803-0541-4526-9b08-1b3a64360ce3 ] Deleted tmp folder 4bd6db79-5e55-4b48-a85c-803a327651a6 true [ 35125803-0541-4526-9b08-1b3a64360ce3 ] failed
```

- 
            