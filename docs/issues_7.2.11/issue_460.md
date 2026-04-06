# [Critical] XDM Firefox add-on create more download confirmation windows (#460)

[[Critical] XDM Firefox add-on create more download confirmation windows](https://github.com/subhra74/xdm/issues/460#top)

[mhdzumair](https://github.com/mhdzumair)
[on Jan 18, 2021](https://github.com/subhra74/xdm/issues/460#issue-788344181)
Recently new Firefox xdm extension create more download windows exactly 18 windows when I hit a download button on a site this not only for that site I faced the issue in another website too. So I don't think it was just website issue. And bad impact is my machine get freeze for several minutes when all these download dialog window came out. Please fix this issue as soon as possible.
https://user-images.githubusercontent.com/44891939/104929934-90a60f80-59ca-11eb-98a0-2ee34974aa86.pngLook this Screenshots here
System Information
- OS: Debian buster (10)
- Browser: Firefox,  Version 78.6.1esr (64-bit)
- XDM addon Version: 2.2
- XDM Version: Version 7.2.11 with Java Eclipse OpenJ9 11.0.6 on Linux
Generated log using below method I read that logs printed in my terminal. The browser extension send so many request to xdm core like I said 18 times. I think the problem is with the new extension

- 
            