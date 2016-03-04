#!/bin/bash
cd src/main/webapp/images
for n in logo repo branch;
do
  pushd $n
    for sz in 16 24 32 48 64;
    do
      inkscape -z -e ${sz}x${sz}.png -w ${sz} -h ${sz} image.svg
    done
  popd
done
