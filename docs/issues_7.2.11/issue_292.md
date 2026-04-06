# No audio for video files on bilibili (#292)

[No audio for video files on bilibili](https://github.com/subhra74/xdm/issues/292#top)
[enhancementNew feature or request](https://github.com/subhra74/xdm/issues?q=state%3Aopen%20label%3A%22enhancement%22)

[arifer612](https://github.com/arifer612)
[on Aug 8, 2020](https://github.com/subhra74/xdm/issues/292#issue-675528605)
PLEASE DO NOT JUST SAY "It does not work, or something not working etc." Provide enough relevant details so that the issue can be analyzed and reproduced easily
Describe the bug Tried to use XDM to download videos on bilibili. The video stream gets downloaded without a problem, but no audio stream was downloaded. There were no issues with YouTube or Twitter. I expended all the formats that were provided in the DOWNLOAD VIDEO popup. After the download, I played each video to test, but there was no audio. I checked the codecs on MediaInfo and it only showed the video codec with no audio.
I then thought of just downloading the audio itself and ffmpeg-ing them together after it's done. But when I chose to convert the videos to 320kpbs mp3 in the covert dropdown list, the result was that it could not complete the download. I got an error that said, "Failed to append/convert file parts, please check if the drive is full or write protected".
I then restarted the whole process while on sudo, only to get the same result.
To Reproduce I will list out the URLs I attempted to download from, some of them are randomly picked from the front page add randomness to my picks :
- [https://www.bilibili.com/video/BV1rC4y1b7sP/](https://www.bilibili.com/video/BV1rC4y1b7sP/)
- [https://www.bilibili.com/video/BV1RK411J7f4](https://www.bilibili.com/video/BV1RK411J7f4)
- [https://www.bilibili.com/video/av668714615/](https://www.bilibili.com/video/av668714615/)
- [https://www.bilibili.com/video/BV1v7411e7AE/?spm_id_from=333.788.videocard.8](https://www.bilibili.com/video/BV1v7411e7AE/?spm_id_from=333.788.videocard.8)
[https://www.bilibili.com/video/BV1rC4y1b7sP/](https://www.bilibili.com/video/BV1rC4y1b7sP/)
[https://www.bilibili.com/video/BV1RK411J7f4](https://www.bilibili.com/video/BV1RK411J7f4)
[https://www.bilibili.com/video/av668714615/](https://www.bilibili.com/video/av668714615/)
[https://www.bilibili.com/video/BV1v7411e7AE/?spm_id_from=333.788.videocard.8](https://www.bilibili.com/video/BV1v7411e7AE/?spm_id_from=333.788.videocard.8)
Expected behavior I expect that:
1. Bilibili videos can be downloaded with the audio (I can do so using youtube-dl on Linux, albeit at a much slower speed, and IDM on Windows)
2. Converting audio from Bilibili videos should not be a problem (since it does so for YouTube and Twitter videos)
Screenshots [Screenshot of the error that pops up when converting audio](https://user-images.githubusercontent.com/46054733/89712084-1f951180-d9c1-11ea-9907-45287520ad4c.png)
please complete the following information:
- OS: elementary OS 5.1.6
- Browser: Chrome 84.0.4147.105 (Official Build) (64-bit)
- XDM addon Version 2.1
- XDM Version 7.2.11
Generated log using below method
[xdmLogs@arifer612.txt](https://github.com/subhra74/xdm/files/5045322/xdmLogs%40arifer612.txt)
Additional context None

[enhancementNew feature or request](https://github.com/subhra74/xdm/issues?q=state%3Aopen%20label%3A%22enhancement%22)

- 
            