# build-msi.ps1
# Requires WiX Toolset v4

if (-not (Get-Command "wix" -ErrorAction SilentlyContinue)) {
    Write-Error "WiX Toolset v4 'wix' command not found. Please install it with: dotnet tool install --global wix"
    exit 1
}

$appDir = "output-app\xdman"
$jreDir = "$appDir\jre"
$script:rootPath = (Get-Item $jreDir).FullName
$script:componentIds = @()

# Update JSON files with absolute paths for default installation
Write-Host "Updating Native Messaging JSON files..."
$chromeJson = "$appDir\native-messaging\xdm_chrome.native_host.json"
$ffJson = "$appDir\native-messaging\xdmff.native_host.json"

if (Test-Path $chromeJson) {
    $content = Get-Content $chromeJson -Raw | ConvertFrom-Json
    $content.path = "C:\Program Files\XDM\native-messaging\xdm_chrome.native_host.exe"
    $content | ConvertTo-Json | Out-File $chromeJson -Encoding utf8
}

if (Test-Path $ffJson) {
    $content = Get-Content $ffJson -Raw | ConvertFrom-Json
    $content.path = "C:\Program Files\XDM\native-messaging\xdmff.native_host.exe"
    $content | ConvertTo-Json | Out-File $ffJson -Encoding utf8
}

function Get-SafeId {
    param($path)
    $rel = $path.Replace($script:rootPath, "").TrimStart("\")
    $hash = [System.Security.Cryptography.MD5]::Create().ComputeHash([System.Text.Encoding]::UTF8.GetBytes($rel.ToLower()))
    $guid = [System.Guid]::new($hash).ToString("N")
    return "id_$guid"
}

function Harvest-Folder-Recursive {
    param($path, $id)
    $xml = ""
    $items = Get-ChildItem -Path $path
    
    foreach ($item in $items | Where-Object { -not $_.PSIsContainer }) {
        $fileId = Get-SafeId -path $item.FullName
        $xml += "        <Component Id='$fileId' Guid='*'><File Source='$($item.FullName)' KeyPath='yes' /></Component>`n"
        $script:componentIds += $fileId
    }
    
    foreach ($item in $items | Where-Object { $_.PSIsContainer }) {
        $subDirId = Get-SafeId -path $item.FullName
        $xml += "        <Directory Id='$subDirId' Name='$($item.Name)'>`n"
        $xml += Harvest-Folder-Recursive -path $item.FullName -id $subDirId
        $xml += "        </Directory>`n"
    }
    return $xml
}

Write-Host "Harvesting JRE from $jreDir..."
$jreTreeXml = Harvest-Folder-Recursive -path $script:rootPath -id "JreFolder"

$xml = "<?xml version='1.0' encoding='UTF-8'?>`n<Wix xmlns='http://wixtoolset.org/schemas/v4/wxs'>`n  <Fragment>`n"
$xml += "    <DirectoryRef Id='JreFolder'>`n"
$xml += $jreTreeXml
$xml += "    </DirectoryRef>`n"
$xml += "    <ComponentGroup Id='JreHarvestComponents'>`n"
foreach ($cId in $script:componentIds) {
    $xml += "      <ComponentRef Id='$cId' />`n"
}
$xml += "    </ComponentGroup>`n  </Fragment>`n</Wix>"

$xml | Out-File -FilePath "jre-harvest.wxs" -Encoding utf8

Write-Host "Building MSI..."
wix build xdman.wxs jre-harvest.wxs -o xdman-v26.04.msi -ext WixToolset.UI.wixext

if ($LASTEXITCODE -eq 0) {
    Write-Host "Successfully built xdman-v26.04.msi"
} else {
    Write-Error "Failed to build MSI"
    exit $LASTEXITCODE
}
