# 7.2.11 AdoptOpenJDK with Edge browser intergration seems broken (#404)

[7.2.11 AdoptOpenJDK with Edge browser intergration seems broken](https://github.com/subhra74/xdm/issues/404#top)

[nahoijtink](https://github.com/nahoijtink)
[on Nov 23, 2020](https://github.com/subhra74/xdm/issues/404#issue-748477104)
I gave a fairly clean install of Windows 10:
Edition Windows 10 Home Version 20H2 Installed on ‎14/‎11/‎2020 OS build 19042.630 Experience Windows Feature Experience Pack 120.2212.31.0
All Windows updates are applied.
I can use the 7.2.10 (windows .msi install) with the Edge plugin perfectly fine following the steps described in the install instructions.
However the 7.2.11 (windows .msi installer) version does not connect to the browser plugin 1.4 or 2.1. Either the plugin does not connect at all or the plugin does not properly communicate with XDM. I can't tell which is the case, It will not always have a red cross on it from the start, but clicking on it will turn it red either case.
I then installed JRE 8 and JDK 15. Downloaded the 7.2.11 Portable JAR, and that works fine with the Edge XDM browser plugin 1.4.
I exited the portable version and the started the .MSI installed executable and now that seems to work fine with the Edge XDM browser plugin 1.4.
I then tried plugin version 2.1, and now that seems to work fine too.
In conclusion, the included Java AdoptOpenJDK 11.0.6 and XDM 7.2.11 have some sort of problem communicating with the Edge Plugin 1.4 and 2.1 when no 'official' Java support (JRE/JDK) is installed on Windows 10.

- 
            