@echo off

echo Salesforce Data Loader Command Line Interface Quickstart (CLIq)
echo ===============================================================
echo  .oOOOo.  o       ooOoOOo        
echo .O     o  O          O           
echo o         o          o           
echo o         o          O           
echo o         O          o    .oOoO' 
echo O         O          O    O   o  
echo `o     .o o     .    O    o   O  
echo  `OoooO'  OOoOooO ooOOoOo `OoOo  
echo                               O  
echo                               `o 
echo ===============================================================

REM DL >=24 moved java to the Java dir
set JAVADIR=""
if exist "..\Java" (set JAVADIR=Java) else (set JAVADIR=_jvm)

echo Using %JAVADIR% to run CLIq...
..\%JAVADIR%\bin\java.exe -Xmx256m -Djava.dir=%JAVADIR% -cp cliq.jar;..\*;lib\miglayout-3.7.1.jar;. com.salesforce.cliq.DataLoaderCliq %1

pause