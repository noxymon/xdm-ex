# Chinese file name present incorrectly (#688)

[Chinese file name present incorrectly](https://github.com/subhra74/xdm/issues/688#top)

[JihanLoong](https://github.com/JihanLoong)
[on Oct 19, 2021](https://github.com/subhra74/xdm/issues/688#issue-1030524790)
https://user-images.githubusercontent.com/39987088/137949501-bb4b2870-0212-4440-b9c8-21a1861a84be.pngScreenshots The site is here: [https://downloads.freemdict.com/%E5%B0%9A%E6%9C%AA%E6%95%B4%E7%90%86/%E5%85%B1%E4%BA%AB2020.5.11/content/3_chinese/4_%E5%A4%A7%E9%83%A8%E5%A4%B4/%E6%BC%A2%E8%AA%9E%E6%88%90%E8%AA%9E%E6%BA%90%E6%B5%81%E5%A4%A7%E8%BE%AD%E5%85%B8/](https://github.com/subhra74/xdm/issues/url) Describe the bug When downloading a file with a Chinese name, the name presented in XDM's popup is incorrect. It presents "Mojibake", a garbled code.
To Reproduce
1. Download a file with a Chinese name. 下载一个以中文命名的文件。
2. XDM will capture this download and show a popup. XDM接管下载，并弹出下载窗口。
3. The file name will be presented as a "Mojibake", or garbled code. 文件名将显示为乱码。
Expected behavior Present file name correctly in Chinese. 正确显示中文文件名。
Please complete the following information:
- OS: Windows10
- Browser: Firefox
- XDM addon Version: 2.2
- XDM Version: Version 7.2.11 with Java AdoptOpenJDK 11.0.6 on Windows 10
Thank you!

- 
            