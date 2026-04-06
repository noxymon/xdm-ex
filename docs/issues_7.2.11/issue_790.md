# Many Youtube Videos Do Not Save Correctly (#790)

[Many Youtube Videos Do Not Save Correctly](https://github.com/subhra74/xdm/issues/790#top)

[sj365](https://github.com/sj365)
[on Feb 25, 2022](https://github.com/subhra74/xdm/issues/790#issue-1150703201)

[#768](https://github.com/subhra74/xdm/discussions/768)
2/25/22
Describe the bug
Upon accessing youtube.com url, XDM will sense the video and display available download resolutions. Clicking the desired resolution starts the download. Once download is completed, running the saved file will open a blank window which should play audio and video but it does not. The saved file may or may not play audio depending on the media player used. Updated Klite codec pack 16.8.3 with MPC will not play the audio or video of the saved file. VLC 3.0.16 will play audio but no video. Windows Movie & TV application will only play audio no video. Windows Media Player will play audio no video.
To Reproduce

[https://gardencollage.com/wander/gardens-parks/get-the-lead-out-how-to-test-your-soil-for-contaminants/](https://gardencollage.com/wander/gardens-parks/get-the-lead-out-how-to-test-your-soil-for-contaminants/) 480p - no audio/video
[https://www.youtube.com/watch?v=0cjaIwk0GfI&t=44s](https://www.youtube.com/watch?v=0cjaIwk0GfI&t=44s) 480p - no audio/video
[https://www.youtube.com/watch?v=vYT5xgjxUg0&t=4s](https://www.youtube.com/watch?v=vYT5xgjxUg0&t=4s) 720p - no audio/video, 360p - audio/video works
[https://www.youtube.com/watch?v=laBXwOdbOlw](https://www.youtube.com/watch?v=laBXwOdbOlw) 720p - audio/video works
[https://www.youtube.com/watch?v=PVm13bDtAY0](https://www.youtube.com/watch?v=PVm13bDtAY0) 720p, 1080p - no audio/video
[https://www.youtube.com/watch?v=i1t_hcnVX2U](https://www.youtube.com/watch?v=i1t_hcnVX2U) 480p - no audio/video
Access supplied youtube.com urls, XDM will sense the video and display available download resolutions. Clicking the desired resolution starts the download. Once download is completed, running the saved file will open a blank window which should play audio and video but it does not.
please complete the following information:
OS: [Windows 11 Home Version 21H2 22000.527 I9, 32gb Ram 3.3ghz, Windows 10 Pro 21H1 19043.1503 I7 16gb Ram 2.8ghz ] Browser [chrome 98.0.4758.102, Firefox 97.0.1] XDM addon Version [Firefox 2.2, Chrome 2.1] XDM Version [Windows 11 Home - 7.2.11] [Windows 10 Pro - 7.2.11] Generated log using below method [https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting](https://github.com/subhra74/xdm/wiki/Generate-log-for-troubleshooting)
Additional context [XDM BugReport.txt](https://github.com/subhra74/xdm/files/8143164/XDM.BugReport.txt)
**XDM also does not work correctly on CBS news video links - video only no audio [https://www.cbsnews.com/video/pilot-program-pays-some-new-moms-1000-per-month/](https://www.cbsnews.com/video/pilot-program-pays-some-new-moms-1000-per-month/)

- 
            