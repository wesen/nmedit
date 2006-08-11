#!/bin/sh

# this script requires imagemagick and inkscape
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

map="png/module-icons.slice"

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
  
  echo "# 16px*16px dimension" > $map;
  echo "slice.hrz 16px" >> $map;
  echo "slice.vrt 16px" >> $map;
  mosaicfiles=""
  vy="0"
  
  for tileset in $tiles
  do
     
    files=""
    vx="0"

    if [ ! -d "png/$tileset" ]
    then
      # create dir: example png/inout_set
      mkdir "png/$tileset"
    fi
    
    echo -e "\n# $tileset" >> $map

    echo -e "- $tileset \c"
    
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
      target="png/$tileset/$key.png"
      files="$files $target"
      inkscape $tile --export-png $target >/dev/null
      echo -e ".\c"
    done 
    if [ $vx -gt 0 ]
    then
      echo " ($vx images)"
      montage -background Transparent $files -mode Concatenate -tile x1 png/$tileset.png
      mosaicfiles="$mosaicfiles png/$tileset.png"
      let "vy=$vy+1"
    fi
  done
  
  if [ $vy -gt 0 ]
  then
    echo "- completing mosaic ($vy sets)"
    montage -background Transparent $mosaicfiles -mode Concatenate -tile 1x png/module-icons.png
  else
    echo "- completing mosaic (no images)"
  fi
fi
echo "done."
