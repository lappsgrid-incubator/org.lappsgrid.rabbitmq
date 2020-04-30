#!/usr/bin/env bash

key=$1
if [[ -n "$2" ]] ; then
	value=$2
else
	value=${!1}
fi

travis encrypt -r lappsgrid-incubator/org.lappsgrid.rabbitmq $key=$value --add
