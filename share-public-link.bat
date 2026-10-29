@echo off
setlocal
title Share KARE Canteen Public URL (Powered by Cloudflare)
echo ================================================================
echo   KARE Smart Canteen - Public Link Generator
echo   Kalasalingam Academy of Research and Education
echo ================================================================
echo.
echo [Option 1 - Same WiFi / Hotspot]:
echo Anyone connected to your WiFi or Hotspot can open:
echo 👉 http://10.2.12.4:8080
echo.
echo [Option 2 - Worldwide Internet (Mobile Data / 4G / 5G)]:
echo Starting Cloudflare Tunnel...
echo Look for the "https://....trycloudflare.com" link below:
echo ================================================================
echo.

"%~dp0.tools\cloudflared.exe" tunnel --url http://localhost:8080
pause
