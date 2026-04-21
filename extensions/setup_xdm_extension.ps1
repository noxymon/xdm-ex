# XDM Extension Setup Script for Windows
# Run this script as Administrator to ensure Registry keys and Junctions can be set.

# Use PSScriptRoot to make the script independent of the current working directory
$scriptDir = $PSScriptRoot
if (-not $scriptDir) { $scriptDir = Get-Location } # Fallback for older PS versions

$nativeMessagingDir = "$scriptDir\native-messaging"
$hostManifestPath = "$nativeMessagingDir\xdm_chrome.native_host.json"

Write-Host "--- XDM Setup ---" -ForegroundColor Cyan

# 1. Verify existence of the manifest file
if (-not (Test-Path $hostManifestPath)) {
    Write-Host "Error: $hostManifestPath not found. Creating a new one..." -ForegroundColor Yellow
    $defaultJson = @{
        name = "xdm_chrome.native_host"
        description = "Native messaging host for Xtreme Download Manager (Chrome)"
        path = ""
        type = "stdio"
        allowed_origins = @()
    }
    $defaultJson | ConvertTo-Json | Set-Content $hostManifestPath
}

# 2. Ask for Extension ID
Write-Host "Please enter your Chrome Extension ID (found in chrome://extensions):"
$extensionId = Read-Host
if (-not $extensionId) {
    Write-Error "Extension ID is required."
    exit
}

# 3. Update the Host Manifest JSON using a robust approach
Write-Host "Updating $hostManifestPath..."
try {
    $json = Get-Content $hostManifestPath -Raw | ConvertFrom-Json
    
    # Ensure properties exist by casting to a dictionary if necessary or using Add-Member
    # However, setting them directly on a PSCustomObject usually works if they exist in the file.
    # To be safe, we'll recreate the object.
    $updatedJson = @{
        name = "xdm_chrome.native_host"
        description = "Native messaging host for Xtreme Download Manager (Chrome)"
        path = "$nativeMessagingDir\native.exe"
        type = "stdio"
        allowed_origins = @("chrome-extension://$extensionId/")
    }
    
    $updatedJson | ConvertTo-Json | Set-Content $hostManifestPath
    Write-Host "Manifest updated successfully." -ForegroundColor Green
} catch {
    Write-Error "Failed to update JSON: $($_.Exception.Message)"
    exit
}

# 4. Register the Native Messaging Host in Registry
Write-Host "Registering in Windows Registry..."
$regPath = "HKCU:\Software\Google\Chrome\NativeMessagingHosts\xdm_chrome.native_host"
try {
    if (-not (Test-Path $regPath)) {
        New-Item -Path $regPath -Force | Out-Null
    }
    Set-ItemProperty -Path $regPath -Name "(Default)" -Value "$hostManifestPath"
    Write-Host "Registry entry set." -ForegroundColor Green
} catch {
    Write-Error "Failed to set Registry key: $($_.Exception.Message). Try running as Administrator."
    exit
}

# 5. Handle JRE junction
$jrePath = "$nativeMessagingDir\jre"
if (-not (Test-Path $jrePath)) {
    Write-Host "The native host needs a JRE to launch XDM."
    Write-Host "Please enter the path to your existing Java/JDK folder (e.g., C:\Program Files\Java\jdk-17):"
    $userJrePath = Read-Host
    if ($userJrePath -and (Test-Path $userJrePath)) {
        Write-Host "Creating junction link to JRE..."
        cmd /c mklink /J "$jrePath" "$userJrePath"
        Write-Host "JRE link created." -ForegroundColor Green
    } else {
        Write-Warning "No valid JRE path provided. You will need to manually create a 'jre' folder in $nativeMessagingDir"
    }
} else {
    Write-Host "JRE folder already exists."
}

Write-Host "`nSetup Complete! Please reload your extension in Chrome." -ForegroundColor Green
