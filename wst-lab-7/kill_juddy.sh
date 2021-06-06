#!/bin/bash
netstat -ano -p 2>/dev/null | grep 8080 | grep LISTEN | awk '{print $7}' | sed 's![/].*!!g' | xargs kill -9
echo "Juddy killed."
