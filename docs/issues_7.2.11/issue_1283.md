# Downloads don't begin, but work fine in Transmission (#1283)

[Downloads don't begin, but work fine in Transmission](https://github.com/subhra74/xdm/issues/1283#top)

[eiger3970](https://github.com/eiger3970)
[on Apr 22, 2025](https://github.com/subhra74/xdm/issues/1283#issue-3010718676)

[#768](https://github.com/subhra74/xdm/discussions/768)
PLEASE DO NOT JUST SAY "It does not work, or something not working etc." Provide enough relevent details so that the issue can be analyzed and reproduced easily
Describe the bug Downloads don' begin, however Transmission downloads and completes quickly. Was expecting XDM to be faster than Transmission.
To Reproduce Steps to reproduce the behavior:
1. Paste Magnet link URL into XDM
2. Await auto start
3. XDM window shows downloading, but nothing happens
Expected behavior Download should begin and should be faster than Transmission
Screenshots Not needed
please complete the following information:
- OS: Raspberry Pi 5 Desktop
- Browser Brave
- XDM Version 7.2.11 with Java Eclipse Adoptium 11.0.26 on Linux
Generated log using below method
- [https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
- $  cd /opt/xdman/
rapi5  raspberrypi  opt  xdman  $  ls
1.version  icon.png     uninstall.sh  xdman.desktop  youtube-dl
ffmpeg     native_host  xdman         xdman.jar
rapi5  raspberrypi  opt  xdman  $  jre/bin/java -jar xdman.jar
bash: jre/bin/java: No such file or directory
[https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
Additional context Add any other context about the problem here. Nope

- 
            