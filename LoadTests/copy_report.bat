echo %~dp0

rmdir /Q /S %~dp0TestResults
cd .\target\gatling

for /F "delims=" %%f in ('dir /B /S /AD %~1-*') do (
  mkdir "%~dp0TestResults\%%~nxf" 2>NUL
  xcopy /S /E /C "%%~ff" "%~dp0TestResults\"
)