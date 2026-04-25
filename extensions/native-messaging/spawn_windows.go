package main

import (
	"os"
	"path/filepath"
	"syscall"
)

//SpawnProcess creates a process
func SpawnProcess() {

	// log,err:=os.Create("C:\\Users\\subhro\\Documents\\go-projects\\native-messaging\\log2.txt")
	// log.WriteString("Going to spawn process\n")

	var sI syscall.StartupInfo
	var pI syscall.ProcessInformation

	//argv := syscall.StringToUTF16Ptr("c:\\windows\\system32\\notepad.exe")

	flags := uint32(syscall.CREATE_UNICODE_ENVIRONMENT)
	flags |= CreateBreakAwayFromJob
	flags |= CreateNewProcessGroup
	flags |= CreateNoWindow

	path, err := os.Executable()
	if err != nil {
		return
	}

	dir := filepath.Dir(path)
	parentDir := filepath.Dir(dir)

	jrePath := filepath.Join(dir, "jre", "bin", "javaw.exe")
	jarPath := filepath.Join(dir, "xdman.jar")

	if _, err := os.Stat(jrePath); err != nil {
		jrePath = filepath.Join(parentDir, "jre", "bin", "javaw.exe")
	}

	if _, err := os.Stat(jarPath); err != nil {
		jarPath = filepath.Join(parentDir, "xdman.jar")
	}

	syscall.CreateProcess(
		nil,
		syscall.StringToUTF16Ptr("\""+jrePath+"\" -jar \""+jarPath+"\" -m"),
		nil,
		nil,
		false,
		flags,
		nil,
		nil,
		&sI,
		&pI)
	
}