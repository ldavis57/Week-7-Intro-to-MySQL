# Get Procede exe file
$path="\\SP1-REINS\d$\SunGard iWorks\ProCede 2014\*.exe"
$command='Get-ChildItem -Path ' +  $path + ' -rec | select-object FullName, LastWriteTime | sort descending '
$outfile="c:\temp\ProcedeFiles.txt"

"Outfile: $outfile" | Out-File $outfile
"PowerShell Script: $command" | Out-File $outfile -Append
"Script gets a list of all exe files in the Procede file folders, sorts them by last time written, and displays the full name and last write time in descending order." | Out-File $outfile -Append
Get-ChildItem -Path $path -rec | select-object FullName, LastWriteTime | sort descending | Out-File $outfile -Append
Write-Host "" | Out-File $outfile -Append
Write-Host "" | Out-File $outfile -Append
$command=""
$outfile=""





# Get Last Written User file
$path="\\sp1-versara1\d$\Applications\VersaraProduction\*.exe"
#$command='Get-ChildItem -Path ' +  $path + ' -rec | select-object FullName, LastWriteTime | sort LastWriteTime | select -Last 1'
$command='Get-ChildItem -Path ' +  $path + ' -rec | select-object FullName, LastWriteTime | sort descending'
$outfile="c:\temp\VersaraFiles.txt"

"Outfile: $outfile" | Out-File $outfile
"PowerShell Script: $command" | Out-File $outfile -Append
"Script gets a list of all exe files in the Versara file folders, sorts them by last time written, and displays the full name and last write time in descending order." | Out-File $outfile -Append
Get-ChildItem -Path $path -rec | select-object FullName, LastWriteTime | sort descending | Out-File $outfile -Append
$command=""
Write-Output "" | Out-File $outfile -Append
Write-Host "" | Out-File $outfile -Append


# Get Procede dll files
$path="\\sp1-versara1\d$\Applications\VersaraProduction\*.dll"
$command='Get-ChildItem -Path ' +  $path + ' -rec | select-object FullName, LastWriteTime | sort descending '

"PowerShell Script: $command" | Out-File $outfile -Append
"Script gets a list of all dll files in the Versara file folders, sorts them by last time written, and displays the full name and last write time in descending order." | Out-File $outfile -Append
Get-ChildItem -Path $path -rec | select-object FullName, LastWriteTime | sort descending | Out-File $outfile -Append
$command=""
$outfile=""
