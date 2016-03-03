#!/bin/bash
cd src/main/webapp/images
for sz in 16 24 32 48 64;
do
  inkscape -z -e ${sz}x${sz}.png -w ${sz} -h ${sz} logo.svg
done