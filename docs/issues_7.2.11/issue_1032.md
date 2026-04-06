# .TS files can have audio/video sync issues (#1032)

[.TS files can have audio/video sync issues](https://github.com/subhra74/xdm/issues/1032#top)

[org0ne](https://github.com/org0ne)
[on Apr 26, 2023](https://github.com/subhra74/xdm/issues/1032#issue-1685363245)

[#768](https://github.com/subhra74/xdm/discussions/768)
Describe the bug XDM downloaded .TS files exhibit audio and video sync isues. As playback proceeds uninterrupted through the video, the audio/video gets progressively worse However, if the user seeks to a new location, the audio and video regain sync again - but will then continue to drift as before.
To Reproduce
1. Navigate to this site [https://supjav.com/203825.html](https://supjav.com/203825.html)
2. Ensure that you are using the 'TV' server (server choice is just below the video window)
3. Hit play on the video, then use XDM to download
4. (note that this is just one specific example - the problem persists on the majority of .TS files downloaded from this site)
5. After downloading, play the video and let it continue - the longer the video plays, the more out of sync it will be
6. Seek to a new location and note that the audio and video are now re-synced
7. Continue to let the video play and note that the audio and video will again drift out of sync
[https://supjav.com/203825.html](https://supjav.com/203825.html)
Expected behavior Audio and Video should retain perfect sync
please complete the following information:
- OS: Linux Ubuntu 20.04
- Browser - Firefox
- Version 7.2.11 with Java Eclipse OpenJ9 11.0.6 on Linux
Additional Info Note that any .mp4 file downloaded from the servers will not have any sync issues

- 
            