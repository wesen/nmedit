#!/bin/sh

# this script requires imagemagick
# usage:
#   mosaic       - create mosaic
#   mosaic clean - remove generated files

# references:
#   http://www.cit.gu.edu.au/~anthony/graphics/imagick6/mosaics/

echo "Mosaic generator, Copyright (C) 2006 Christian Schneider"


tiles=""

for tset in svg/*
do
  tset=${tset:4}
  if [ $tset != "CVS" ]
  then
    tiles="$tiles $tset"
  fi
done

export map="png/map.slice"

if [ "$1" == "clean" ]
then
  echo "[cleaning]"
  if [ -d "png" ]
  then
    rm -r "png"
  fi
elif [ -n "$1" ]
then
  echo "unknown parameter: $param"
  echo "mosaic       - create mosaic"
  echo "mosaic clean - remove generated files"
else
  echo "[mosaic]"
  if [ ! -d "png" ]
  then
    mkdir "png"
    touch $map
  fi
  echo "" > $map;
  mosaicfiles=""
  vy="0"
  for tileset in $tiles
  do
    files=""
    vx="0"
    echo "# $tileset" >> $map
    for tile in svg/$tileset/*.svg
    do
      key=$tile
      #remove svg/
      key=${key:4}
      #remove path name
      key=${key/"$tileset"/}
      #remove /
      key=${key:1}
      # remove file extension
      key=${key/.svg/}
      echo "$key    $vx $vy" >> $map
      let "vx=$vx+1"
      files="$files $tile"
    done
    if [ $vy > 0 ]
    then
      echo "- $tileset"
    fi
    montage -background Transparent $files -mode Concatenate -tile x1 png/$tileset.png
    mosaicfiles="$mosaicfiles png/$tileset.png"
    let "vy=$vy+1"
  done
  echo "- completing mosaic"
  montage -background Transparent $mosaicfiles -mode Concatenate -tile 1x png/mosaic.png
fi
echo "done."
