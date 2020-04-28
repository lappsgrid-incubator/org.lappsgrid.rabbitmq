#!/usr/bin/env bash

echo $1
echo ${!1}

travis encrypt -r lappsgrid-incubator/org.lappsgrid.rabbitmq $1=${!1} --add

