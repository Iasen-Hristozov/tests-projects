@echo off
set URLEnc=%1
echo %URLEnc%
set MP4=%2
echo %MP4%
set NameLat=%3
echo %NameLat%

@echo on
D:\Iasen\Documents\Tools\rtmpdump-2.4-git-010913-windows\rtmpdump.exe -v -r "rtmp://video.slavishow.com/slavishow/" -a "slavishow/" -f "WIN 13,0,0,214" -W "http://www.slavishow.com/content/themes/slavishow/swf/flowplayer.commercial-3.2.18.swf" -p "%URLEnc%" -y "slavishow/%MP4%" --tcUrl "rtmp://video.slavishow.com/slavishow/" -R --buffer 2000 -o "D:\%NameLat%.flv"