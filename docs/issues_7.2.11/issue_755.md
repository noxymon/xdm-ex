# Set Download folder on windows not respected (#755)

[Set Download folder on windows not respected](https://github.com/subhra74/xdm/issues/755#top)

[richard-muvirimi](https://github.com/richard-muvirimi)
[on Jan 4, 2022](https://github.com/subhra74/xdm/issues/755#issue-1093352885)
Describe the bug In the application settings, there is an option to set the download folder of choice, it should go without reason that the auto folder selection based on file type should pick this folder as a parent folder, but somehow it reverts to the default windows download folder. I have two drives and like to download onto the other drive without the windows system but xdm will automatically pick the downloads folder in C drive and ignore the set downloads folder. Even without changing the path in settings the app does not seem to retrieve the download path from the system and seems to be using a hardcoded path
To Reproduce Steps to reproduce the behavior:
1. Select a downloads folder in settings
2. Download a file that will auto pick a folder based on extension eg zip => compressed
3. click on change path
4. will be pointing to the default download path instead of selected
Expected behavior All downloads should be saved in the selected folder even those that are sorted based on file type
please complete the following information:
- OS: Windows 10
- Browser Chrome Version 96.0.4664.110 (Official Build) (64-bit)
- XDM addon Version 2.1
- XDM Version 7.2.11 with Java AdoptOpenJDK 11.0.6 on Windows 10
Additional context I think this is only a matter of the app using a hardcoded path for downloads, should retrieve the relevant download path from the system and respect the user set-path for downloads

- 
            