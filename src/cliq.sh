#!/bin/sh

export CLASSPATH=cliq.jar:../*:lib/miglayout-3.7.1.jar:.
java -Xmx256m com.salesforce.cliq.DataLoaderCliq $@

exit