# XDM increases the video length of streams with .ts extension (#669)

[XDM increases the video length of streams with .ts extension](https://github.com/subhra74/xdm/issues/669#top)

[Flavored-37860](https://github.com/Flavored-37860)
[on Oct 10, 2021](https://github.com/subhra74/xdm/issues/669#issue-1021851629)
When you download a video stream with .ts extension, the downloaded video is stretched to greater duration. For example if the video on website is 2:05 in duration, the downloaded video will be 2:09 in duration. The longer the video, the greater the increase in duration. If you transcode that stretched video with Handbrake, the transcoded file skips audio at places.
To Reproduce: Open this URL [https://www.dailymotion.com/video/x84j2oa](https://www.dailymotion.com/video/x84j2oa) From extension icon in browser, select the quality to download Note that the video's duration on Dailymotion page is 07:45 The file downloaded with XDM will have duration of 07:56 That's an 11 seconds increase. Even when you download with the youtube-dl inside XDM the problem remains.
But if you download the same video with youtube-dl Command-line-interface, at the end of the download it says [FixupM3u8] Fixing malformed AAC bitstream & the duration is fixed.
OS: Windows Browser: Vivaldi XDM addon Version: 2.1 XDM Version: 7.2.11

- 
            