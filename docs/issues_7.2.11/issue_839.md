# On linux : "Failed to add parts of the file, check if the Disk is "Full" or "Write Protected"... (#839)

[On linux : "Failed to add parts of the file, check if the Disk is "Full" or "Write Protected"...](https://github.com/subhra74/xdm/issues/839#top)

[EmilioFr-7](https://github.com/EmilioFr-7)
[on May 30, 2022](https://github.com/subhra74/xdm/issues/839#issue-1252609939)
Hello Subra... I have this same problem again, after reinstalling "Xdman" on a new System "Mx21.01", based on "Debiian Bullseye 11" and "KDE 5.20"... After installing Xdm, everything was going great & everything was working well... Then just after this, now, on the new Video downloads from Youtube, it starts again, & we have the same type of problems again, when I thought this was all settled a long time ago... Still this same Error at the end of the Full Download, saying : *** "Failed to add parts of the file, check if the Disk is "Full" or "Write Protected"... (* Translated from the French interface...)
And in the "Temp" folder of ".xdm" (in the "Hidden files" of the Home part of the main User), we can see the different folders with specific names, & inside each other, the different parts of each video that we tried to Download, & after, that they are converted & finalized before the final Failure, and the error message that appears at this time... It could almost make you think that the "Conversion" & the Assembly of each part of the files cannot be done...
Well, I don't know what I can do to solve this... So, thank you for your advice & help, please...
- Details & Results of the Log below...
OS : Linux "Mx 21.01" based on Debian Bullseye 11. KDE Plasma Version: 5.20.5 KDE Frameworks Version: 5.78.0 Qt Version: 5.15.2 Kernel Version: 5.10.0-14-amd64 OS Type: 64-bit Processors: 8 × Intel® Core™ i7-7700HQ CPU @ 2.80GHz Memory: 31.2 Gio of RAM Graphics Processor: Mesa Intel® HD Graphics 630 And Nvidia GTX 1060.
Browser : Firefox 100.0.2 64 bits. ** Addon Xdm : V 2.2 du 25 Mai 2022. *** Xdm Version : 7.2.11
https://user-images.githubusercontent.com/63238738/170983499-5659c1ac-6f52-4a88-8c2d-64cad5c96fcb.pngLog Result : blue-corner-9@Blue-Wave-9:~ $ cd /opt/xdman blue-corner-9@Blue-Wave-9:/opt/xdman $ jre/bin/java -jar xdman.jar [ main ] loading... [ main ] 11.0.6 5.10.0-14-amd64 [ main ] Loading config... [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts fr [ main ] Loading language fr [ main ] Context initialized [ Thread-4 ] instance starting... [ Thread-4 ] instance started. [ AWT-EventQueue-0 ] showing main window. [ Thread-4 ] Lock acquired... Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/icon.png Error setting Dock icon Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/bg_nav.png [ AWT-EventQueue-0 ] List changed Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/icon_linux.png checking for app update [ Thread-3 ] manifest download response: 200 7.2.11 7.2.11 New session blue-corner-9@Blue-Wave-9:/opt/xdman $

```
blue-corner-9@Blue-Wave-9:~ $ cd /opt/xdman blue-corner-9@Blue-Wave-9:/opt/xdman $ jre/bin/java -jar xdman.jar [ main ] loading... [ main ] 11.0.6 5.10.0-14-amd64 [ main ] Loading config... [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts fr [ main ] Loading language fr [ main ] Context initialized [ Thread-4 ] instance starting... [ Thread-4 ] instance started. [ AWT-EventQueue-0 ] showing main window. [ Thread-4 ] Lock acquired... Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/icon.png Error setting Dock icon Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/bg_nav.png [ AWT-EventQueue-0 ] List changed Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/icon_linux.png checking for app update [ Thread-3 ] manifest download response: 200 7.2.11 7.2.11 New session blue-corner-9@Blue-Wave-9:/opt/xdman $
```

Thanks a lot for your help...
Emilio (France)...

- 
            