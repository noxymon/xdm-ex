# Streaming Detection Issue in XDM 8.* (#1151)

[Streaming Detection Issue in XDM 8.*](https://github.com/subhra74/xdm/issues/1151#top)

[amkstone](https://github.com/amkstone)
[on Sep 17, 2023](https://github.com/subhra74/xdm/issues/1151#issue-1899792002)
Describe the bug Since version 8.** of the XDM Downloader, the streaming behavior is no longer detected, especially when trying to download videos from Streamtape. The issue occurs when playing a Streamtape video. Streaming is detected for the first video, but not for subsequent videos.
To Reproduce Steps to reproduce the behavior: Open any Streamtape video. Click on 'Play'. Streaming is detected for the first video, but not for subsequent ones
Expected behavior In version 7.2.11, the streaming behavior was expected to be detected and function correctly. In version 8.**, especially when attempting to download videos from Streamtape, the streaming behavior should be reliably detected as it was before.
please complete the following information:
- OS: Linux
- Brave, Chrome, Firefox
- Working fine with
- XDM addon Version 2.0
- XDM Version: 7.2.11
Not working the new version
- XDM Version 8.0.29
Additional context I have found that in version 7.2.11 of the XDM Downloader, the streaming behavior is reliably detected with no issues. It would be great if this behavior could be restored in version 8.**.

- 
            