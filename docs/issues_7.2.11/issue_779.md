# Browser monitor is adding a duplicate entry instead of just one entry (#779)

[Browser monitor is adding a duplicate entry instead of just one entry](https://github.com/subhra74/xdm/issues/779#top)

[JadianRadiator](https://github.com/JadianRadiator)
[on Feb 5, 2022](https://github.com/subhra74/xdm/issues/779#issue-1124995331)

[#768](https://github.com/subhra74/xdm/discussions/768)
PLEASE DO NOT JUST SAY "It does not work, or something not working etc." Provide enough relevent details so that the issue can be analyzed and reproduced easily
Describe the bug A clear and concise description of what the bug is. Browser monitor is adding a duplicate entry instead of just one entry.
To Reproduce Steps to reproduce the behavior: Install browser monitor in Firefox. Add "MP4" to the auto-interrupt format list. Click the download button for a video.
Expected behavior A clear and concise description of what you expected to happen. Only adding the download once.
Screenshots If applicable, add screenshots to help explain your problem.
please complete the following information:
- OS: [e.g. Linux/Windows]
PopOS 20.04
- Browser [e.g. chrome, Firefox]
Firefox
- XDM addon Version [e.g. 2.0]
2.2
- XDM Version [e.g. 7.2.0]
Version 7.2.11 with Java Eclipse OpenJ9 11.0.6 on Linux
Generated log using below method
- [https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
[XDMan.log](https://github.com/subhra74/xdm/files/8008550/XDMan.log)
[https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
[XDMan.log](https://github.com/subhra74/xdm/files/8008550/XDMan.log)
Additional context Add any other context about the problem here. The adult-only site in the log is the only site I've tried so far. And I've been looking for a download manager that does a better job at downloading and interrupting downloads from it. Because the way they pseudo-drm downloads can block a lot of download managers' download interruption. Edit: The downloading in and of itself isn't behind their pseudo-drm. How you get the download link is what's behind the pseudo-drm.

- 
            