#!/bin/bash
netstat -tlnupaon 2>/dev/null | grep 8080 | grep LISTEN && echo Juddy is running || echo Juddy is not running;
