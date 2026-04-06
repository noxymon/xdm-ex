# XDM's "Do not capture downloads from sites" is broken (with a * filter) (#369)

[XDM's "Do not capture downloads from sites" is broken (with a * filter)](https://github.com/subhra74/xdm/issues/369#top)

[infyProductions](https://github.com/infyProductions)
[on Oct 17, 2020](https://github.com/subhra74/xdm/issues/369#issue-723865542)
PLEASE DO NOT JUST SAY "It does not work, or something not working etc." Provide enough relevant details so that the issue can be analyzed and reproduced easily
Describe the bug A clear and concise description of what the bug is. -When I go to a website (like gmanetwork.com), XDM prompts me to download files which is heavily dependent on the site for resources to load. I tried including a filter (like *.gmanetwork.com), but the resources that I need to read on that site doesn't load anymore without disabling the extension through Extensity.
To Reproduce Steps to reproduce the behavior:
1. Browser monitoring, click View Settings
2. Scroll all the way down until you find the label that says: "Do not automatically capture downloads from below websites"
3. Add the URL you want to exclude (e.g. *.gmanetwork.com)
4. Return to the browser of choice (example: Microsoft Edge Chromium) and load your webpage
https://user-images.githubusercontent.com/31158494/96354976-4aad8700-110f-11eb-97e3-216fa224034b.pnghttps://user-images.githubusercontent.com/31158494/96355172-48e4c300-1111-11eb-8693-4c74f307af6b.pngExpected behavior A clear and concise description of what you expected to happen. The page should work fine without any missing HTML elements on the website (like this): Screenshots If applicable, add screenshots to help explain your problem. This is from gmanetwork.com please complete the following information:
- OS: Windows 10 update 20H1
- Browser: Microsoft Edge Chromium and Google Chrome
- XDM addon Version: 2.1 for Edge Chromium, 2.1 for Google Chrome
- XDM Version: 7.2.11
Generated log using below method
- [https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
[ main ] loading...
[ main ] 11.0.6 10.0
[ main ] Loading config...
[ main ] Creating folders
[ main ] starting monitoring...
[ main ] Init app
[ main ] Loading fonts
en
[ main ] Loading language en
[ main ] Context initialized
checking for app update
[ Thread-0 ] Context initialized
[ Thread-1 ] instance starting...
[ Thread-1 ] instance started.
[ Thread-1 ] Lock acquired...
[ AWT-EventQueue-0 ] showing main window.
New session
New session
Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png
Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png
Error setting Dock icon
Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/bg_nav.png
[ AWT-EventQueue-0 ] List changed
[ Thread-0 ] manifest download response: 200
Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png
7.2.11 7.2.11
[ Thread-3 ] [ Thread-2 ] java.io.IOException: Unexpected EOF while reading header line
at xdman.util.NetUtils.readLine(NetUtils.java:18)
at xdman.monitoring.Request.read(Request.java:16)
at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485)
at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516)
at java.base/java.lang.Thread.run(Unknown Source)
java.io.IOException: Unexpected EOF while reading header line
at xdman.util.NetUtils.readLine(NetUtils.java:18)
at xdman.monitoring.Request.read(Request.java:16)
at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485)
at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516)
at java.base/java.lang.Thread.run(Unknown Source)
New session
[ Thread-4 ] url=[https://images.gmanetwork.com/res22/js/btstrap/btstrap_min_e10-viewport_js.gz](https://images.gmanetwork.com/res22/js/btstrap/btstrap_min_e10-viewport_js.gz)
file=/res22/js/btstrap/btstrap_min_e10-viewport_js.gz
req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43
req=DNT:1
req=Accept:/
res=status:304
res=last-modified:Wed, 06 Apr 2016 11:40:21 GMT
res=server:AmazonS3
res=date:Sat, 17 Oct 2020 23:06:33 GMT
res=cache-control:max-age=120
res=x-cache:Hit from cloudfront
res=via:1.1 4013c12e717de874ba5c50b51c78eecf.cloudfront.net (CloudFront)
res=x-amz-cf-pop:MNL50-C1
res=x-amz-cf-id:iuPV6uq0BgcAv7RrI-3CrQxtggq3vq0-xK0uCEd8IkVxkqjSfCBTQg==
res=age:52
res=content-type:text/javascript
res=content-length:8618
res=content-encoding:gzip

res=etag:"2266bd8d5c0a99c405f3b63cdac76983"
res=tabId:9
res=realUA:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43
[https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
[https://images.gmanetwork.com/res22/js/btstrap/btstrap_min_e10-viewport_js.gz](https://images.gmanetwork.com/res22/js/btstrap/btstrap_min_e10-viewport_js.gz)
New session req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43 req=DNT:1 req=Accept:/ [ Thread-5 ] sending 204... [ Thread-5 ] Response set for 204 [ Thread-4 ] Response set Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png [ Thread-4 ] url=[https://data2.gmanetwork.com/gno/widgets/banners.gz](https://data2.gmanetwork.com/gno/widgets/banners.gz) file=/gno/widgets/banners.gz req=Accept:application/json, text/javascript, /; q=0.01 req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43 req=DNT:1 res=status:200 res=content-type:application/json res=content-length:307 res=access-control-allow-origin:* res=access-control-allow-methods:GET, POST, HEAD res=access-control-expose-headers:ETag res=access-control-max-age:3000 res=content-encoding:gzip res=last-modified:Fri, 17 Apr 2020 09:17:23 GMT res=server:AmazonS3 res=date:Sat, 17 Oct 2020 22:56:26 GMT res=etag:"044e0393e110fdd60f6934894a52c95d" res=vary:Origin,Access-Control-Request-Headers,Access-Control-Request-Method res=x-cache:Hit from cloudfront res=via:1.1 04c903fb7ff202382015a84c82dc37c3.cloudfront.net (CloudFront) res=x-amz-cf-pop:MNL50-C1 res=x-amz-cf-id:0i1vGr3EHAscwrKQYN3hOYr6xkKadX3sXKRm-DpOpnM3-2V5jub8RQ== res=age:66 res=tabId:9 res=realUA:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43
req=Accept:application/json, text/javascript, /; q=0.01 req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43 req=DNT:1 [ Thread-4 ] Response set Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png [ Thread-4 ] url=[https://data2.gmanetwork.com/gno/embeds/51.gz?id=51&container=body](https://data2.gmanetwork.com/gno/embeds/51.gz?id=51&container=body) file=/gno/embeds/51.gz req=Accept:application/json, text/javascript, /; q=0.01 req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43 req=DNT:1 res=status:200 res=content-type:text/plain res=content-length:119 res=access-control-allow-origin:* res=access-control-allow-methods:GET, POST, HEAD res=access-control-expose-headers:ETag res=access-control-max-age:3000 res=content-encoding:gzip res=last-modified:Thu, 17 Sep 2020 09:23:17 GMT res=server:AmazonS3 res=date:Sat, 17 Oct 2020 22:56:24 GMT res=etag:"4c7f0390cf4dbd23883e3c64e3982006" res=vary:Origin,Access-Control-Request-Headers,Access-Control-Request-Method res=x-cache:Hit from cloudfront res=via:1.1 04c903fb7ff202382015a84c82dc37c3.cloudfront.net (CloudFront) res=x-amz-cf-pop:MNL50-C1 res=x-amz-cf-id:s2HtY9me7XOBamD1cNYV7z7DCiFttRkTYUFgSudAtF7b3QX6nTOYfg== res=age:67 res=tabId:9 res=realUA:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43

req=Accept:application/json, text/javascript, /; q=0.01 req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43 req=DNT:1 [ Thread-4 ] Response set Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png New session [ Thread-6 ] sending 204... [ Thread-6 ] Response set for 204 [ Thread-6 ] sending 204... [ Thread-6 ] Response set for 204 [ Thread-4 ] url=[https://data2.gmanetwork.com/gno/authors/authors.gz](https://data2.gmanetwork.com/gno/authors/authors.gz) file=/gno/authors/authors.gz req=Accept:application/json, text/javascript, /; q=0.01 req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43 req=DNT:1 res=status:304 res=access-control-allow-origin:* res=access-control-allow-methods:GET, POST, HEAD res=access-control-expose-headers:ETag res=access-control-max-age:3000 res=last-modified:Thu, 07 Nov 2019 09:29:02 GMT res=server:AmazonS3 res=date:Sat, 17 Oct 2020 23:05:45 GMT res=cache-control:max-age=120 res=vary:Origin,Access-Control-Request-Headers,Access-Control-Request-Method res=x-cache:Hit from cloudfront res=via:1.1 3aa8e90e2b200eda85bd40b1e40b26d9.cloudfront.net (CloudFront) res=x-amz-cf-pop:MNL50-C1 res=x-amz-cf-id:mzGDkDYqMKASpwJl8pKIKwFnSC9iDdAZqny3iAxsn_Scj26YSG-7Qw== res=age:102 res=content-type:text/plain res=content-length:3777 res=content-encoding:gzip res=etag:"0f22b49eb29debd969aa3cceee175a83" res=tabId:9 res=realUA:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43
[ Thread-5 ] url=[https://data2.gmanetwork.com/gno/widgets/at_a_glance_lists/home.gz](https://data2.gmanetwork.com/gno/widgets/at_a_glance_lists/home.gz) file=/gno/widgets/at_a_glance_lists/home.gz req=Accept:application/json, text/javascript, /; q=0.01 req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43 req=DNT:1 res=status:304 res=date:Sat, 17 Oct 2020 23:07:26 GMT res=access-control-allow-origin:* res=access-control-allow-methods:GET, POST, HEAD res=access-control-expose-headers:ETag res=access-control-max-age:3000 res=server:AmazonS3 res=vary:Origin,Access-Control-Request-Headers,Access-Control-Request-Method res=x-cache:Hit from cloudfront res=via:1.1 3aa8e90e2b200eda85bd40b1e40b26d9.cloudfront.net (CloudFront) res=x-amz-cf-pop:MNL50-C1 res=x-amz-cf-id:-ea6eCAyDk6Iig7glTLAmiyUudPR7zMpbVZvI6Ch7vcn6mN-EhJM2A== res=age:26 res=content-type:application/json res=content-length:3865 res=content-encoding:gzip res=last-modified:Sat, 17 Oct 2020 22:45:55 GMT res=etag:"2c98473b52fcf27d9c4349a09ee687b1" res=tabId:9 res=realUA:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43

req=Accept:application/json, text/javascript, /; q=0.01 req=Accept:application/json, text/javascript, /; q=0.01 req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43 req=User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43 req=DNT:1 req=DNT:1 Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png [ Thread-4 ] Response set [ Thread-6 ] sending 204... [ Thread-5 ] Response set [ Thread-6 ] Response set for 204 [ Thread-6 ] sending 204... [ Thread-6 ] Response set for 204 Loading image from url: jar:file:/C:/Program%20Files%20(x86)/XDM/xdman.jar!/icons/xxhdpi/icon.png [ Thread-5 ] [ Thread-6 ] [ Thread-4 ] java.io.IOException: Unexpected EOF while reading header line at xdman.util.NetUtils.readLine(NetUtils.java:18) at xdman.monitoring.Request.read(Request.java:16) at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485) at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516) at java.base/java.lang.Thread.run(Unknown Source) java.io.IOException: Unexpected EOF while reading header line at xdman.util.NetUtils.readLine(NetUtils.java:18) at xdman.monitoring.Request.read(Request.java:16) at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485) at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516) at java.base/java.lang.Thread.run(Unknown Source) java.io.IOException: Unexpected EOF while reading header line at xdman.util.NetUtils.readLine(NetUtils.java:18) at xdman.monitoring.Request.read(Request.java:16) at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485) at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516) at java.base/java.lang.Thread.run(Unknown Source) New session [ Thread-7 ] java.io.IOException: Unexpected EOF while reading header line at xdman.util.NetUtils.readLine(NetUtils.java:18) at xdman.monitoring.Request.read(Request.java:16) at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485) at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516) at java.base/java.lang.Thread.run(Unknown Source) New session [ Thread-8 ] java.io.IOException: Unexpected EOF while reading header line at xdman.util.NetUtils.readLine(NetUtils.java:18) at xdman.monitoring.Request.read(Request.java:16) at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485) at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516) at java.base/java.lang.Thread.run(Unknown Source) New session [ Thread-9 ] java.io.IOException: Unexpected EOF while reading header line at xdman.util.NetUtils.readLine(NetUtils.java:18) at xdman.monitoring.Request.read(Request.java:16) at xdman.monitoring.MonitoringSession.serviceRequest(MonitoringSession.java:485) at xdman.monitoring.MonitoringSession.run(MonitoringSession.java:516) at java.base/java.lang.Thread.run(Unknown Source) New session
Additional context Add any other context about the problem here.

- 
            