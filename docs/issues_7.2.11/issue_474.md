# Can't change language on deepin(Linux) (#474)

[Can't change language on deepin(Linux)](https://github.com/subhra74/xdm/issues/474#top)

[Glucy-2](https://github.com/Glucy-2)
[on Jan 28, 2021](https://github.com/subhra74/xdm/issues/474#issue-795784055)
https://user-images.githubusercontent.com/53287753/106108234-ef197d80-6182-11eb-9d17-b90c76e7d015.pngWhen I selected language to Chinese simplified and clicked 确定(OK), and I can't find where to thoroughly close XDM. If I close it with taskkill or restart the computer, It's still Default(English).

```
taskkill
```

- OS: Linux([deepin](https://www.deepin.org/en/))
- Browser: [EDGE](https://www.microsoftedgeinsider.com/)
- https://user-images.githubusercontent.com/53287753/106109451-859a6e80-6184-11eb-8d8a-9ef1cc7bd66f.pngXDM Version: 7.2.11

[deepin](https://www.deepin.org/en/)
[EDGE](https://www.microsoftedgeinsider.com/)
log:

```
main ] loading... [ main ] 11.0.6 5.4.70-amd64-desktop [ main ] Loading config... [ main ] Creating folders [ main ] starting monitoring... [ main ] Init app [ main ] Loading fonts en [ main ] Loading language en [ main ] Context initialized [ Thread-4 ] instance starting... [ Thread-4 ] instance started. [ AWT-EventQueue-0 ] showing main window. [ Thread-4 ] Lock acquired... Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/icon.png Error setting Dock icon Loading image from url: jar:file:/opt/xdman/xdman.jar!/icons/xxhdpi/bg_nav.png [ AWT-EventQueue-0 ] List changed New session checking for app update [ Thread-3 ] manifest download response: 200 7.2.11 7.2.11
```

- 
            