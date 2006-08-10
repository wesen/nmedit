#!/bin/sh

# this script requires imagemagick
# usage:
#   mosaic       - create mosaic
#   mosaic clean - remove generated files

# references:
#   http://www.cit.gu.edu.au/~anthony/graphics/imagick6/mosaics/

export tiles="audio_set inout_set"
export map="mosaic/map.slice"

echo "Mosaic generator, Copyright (C) 2006 Christian Schneider"

if [ "$1" == "clean" ]
then
  echo "[cleaning]"
  if [ -d "mosaic" ]
  then
    rm -r "mosaic"
  fi
elif [ -n "$1" ]
then
  echo "unknown parameter: $param"
  echo "mosaic       - create mosaic"
  echo "mosaic clean - remove generated files"
else
  echo "[mosaic]"
  if [ ! -d "mosaic" ]
  then
    mkdir "mosaic"
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
    for tile in $tileset/*.svg
    do
      key=$tile
      key=${key/"$tileset"/}
      key=${key/.svg/}
      key=${key:1}
      echo "$key    $vx $vy" >> $map
      let "vx=$vx+1"
      files="$files $tile"
    done
    if [ $vy > 0 ]
    then
      echo "- $tileset"
    fi
    montage -background Transparent $files -mode Concatenate -tile x1 mosaic/$tileset.png
    mosaicfiles="$mosaicfiles mosaic/$tileset.png"
    let "vy=$vy+1"
  done
  echo "- completing mosaic"
  montage -background Transparent $mosaicfiles -mode Concatenate -tile 1x mosaic/mosaic.png
fi
echo "done."
