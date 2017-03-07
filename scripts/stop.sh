#!/bin/bash
pid=`ps aux | grep homeapp-server | awk '{print $2}'`
kill -9 $pid