# Using Chrome the browser monitoring ever on do not allows download activation on "site" or "click" (#439)

[Using Chrome the browser monitoring ever on do not allows download activation on "site" or "click"](https://github.com/subhra74/xdm/issues/439#top)

[Trebly](https://github.com/Trebly)
[on Dec 23, 2020](https://github.com/subhra74/xdm/issues/439#issue-773878851)
Hello,
System context I am running XDM 7.2.11 on Windows 10 20H2. I am using Chrome Version 87.0.4280.88 (Build officiel) (64 bits) The addon is : Xtreme Download Manager 2.1
Actions done I had first not defined any option, but
- XDM was cutting the pdf viewer access (download and viewer so after some test of download, I have chosen the activation for the current tab by a click on the addon icon (when this option is chosen the icon displayed is the addon-locked(disabled)-icon
- XDM was displaying always the direct access "button" permanently into first screen at right down into the notification zone which became not accessible, (the XDM button was always on top). I click "close" and it will never reappear, and I don't know how to get it back but moving it into another place or get it into windows quick launcher.
So the problem is that I am unable to activate the download for the current tab by the click:
at same time:
- When I click I get the message : "XDM is disabled"...
- The Chrome extension pop-up tells: "this site is accessible to XDM"
- XDM windows tels: "browser monitoring : ON"
- Addon is activated
- If I checks the options > Settings > browser monitoring - view settings and click Google Chrome "install addon" I can access to the addon page of chrome in which the option offered is "uninstall" (because it is already)
- If I select again the option : "all" I get back a normal behavior.
- After a download The "DOWNLOAD VIDEO" (short access) appears again.
Current situation So for now XDM is not functional for me. I have checked the windows XDM manager alone using "file > download video": it seems unable to download anything 1- Give the url (copied from the page) 2- "Find". I get a panel named "Download video" which is empty.
I checked if changing the option "Click - urls - all" to "all sites": this finally resets the unique access to the download .
Note : XDM addon has never function with the option "click" or "sites" (last download before changing the option for chrome) on 12/02/2020 - cf. mylist of downloads)
Steps to reproduce:
1. Install (XDM windows and addon for chrome) and activate
2. Select option on addon: "onclick" or "for défined sites" I have checked the 2 options.
3. You can see that the button of the addon (extension in French) changes to "extension disabled"
4. When you click anyway on the extension button the addon is never activated and displays the corresponding message (which proposes to install XDM)
5. When you right click you can access to the options, then
6. if you choose "All" the access to the addon become possible (you need obviously to reload the current page to find it into the download list)
My conclusion: The button with options "on-click" or "for defined sites" is not functional and locks the access anyway, the lonely option functioning is "all sites". Then you need to disable it after each download to keep other downloads tools activated on other site.
I propose two enhancements:
- clear the short button after download is performed
- add button: open short download list into the main panel and for the addon (avoid duplicated downloads by verifying the list)
- add  and message  and ? into the panel "download video" when the url searched is not found or fails.
- Allow to move the "short list" and/or don't maintain it on top upon the windows messages and displays.
Best regards Trebly

- 
            