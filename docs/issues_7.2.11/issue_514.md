# Can not see the menu button with the wrong scale setting (#514)

[Can not see the menu button with the wrong scale setting](https://github.com/subhra74/xdm/issues/514#top)
[bugSomething isn't working](https://github.com/subhra74/xdm/issues?q=state%3Aopen%20label%3A%22bug%22)

[lyh458](https://github.com/lyh458)
[on Mar 24, 2021](https://github.com/subhra74/xdm/issues/514#issue-839339286)
Describe the bug
- Can not see all the menu buttons of main GUI if wrong scale setting is set.
- User config would not be removed with command sudo ./uninstall.sh.

```
sudo ./uninstall.sh
```

To Reproduce Steps to reproduce the behavior:
1. Go to the main GUI
2. Click on 'Tools'
3. set the scale (sorry, I can not remember the detailed steps, because I can not see the main GUI completely after this wrong setting.)
4. Restart XDM, bug happens.
Expected behavior
- A method to handle the wrong scale setting, with a preview to ensure the setting is right? Or can restore the setting with command line.
- Completely uninstall supported (remove the user config when uninstall the software).
https://camo.githubusercontent.com/a010dbebd22a68002a9dde9acdbc4c30a8d504575189ca7aac8114449b785758/68747470733a2f2f692e6c6f6c692e6e65742f323032312f30332f31392f576578753345666b68426f726a54382e706e67Screenshots
please complete the following information:
- OS: Ubuntu 18.04
- Browser: MS Edge
- XDM addon Version: 2.1
- XDM Version: 7.2.11
- screen resolution: 3840*2160
- scale setting in ubuntu: 200%
- scale setting in xdm: 250%
Additional context I think this problem is caused by my wrong scale setting in xdm(250%, too large), I have tried to uninstall xdm and reinstall again, but it seen that the config file of xdm would not be deleted after running the command sudo ./uninstall.sh. I think this problem could be solved if I can deleted my config file, so could you tell me how can I delete it or how to restore to defalut setting? BTW, I think this issue is similar to issue [#98](https://github.com/subhra74/xdm/issues/98) .

```
sudo ./uninstall.sh
```

Thanks!

[bugSomething isn't working](https://github.com/subhra74/xdm/issues?q=state%3Aopen%20label%3A%22bug%22)

- 
            