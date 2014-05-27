# NO      - 512 x 512
# LDPI    - 36 x 36
# MDPI    - 48 x 48
# HDPI    - 72 x 72
# XHDPI   - 96 x 96
# XXHDPI  - 144 x 144
# XXXHDPI - 192 x 192

array_dpi=("drawable-ldpi" "drawable-mdpi" "drawable-hdpi" "drawable-xhdpi" "drawable-xxhdpi" "drawable-xxxhdpi")
array_size=("36x36"         "48x48"         "72x72"         "96x96"           "144x144"        "192x192")

for dir in ${array_dpi[*]}
do
    if [ ! -d "$dir" ]; then
        mkdir $dir
    fi
done

cd drawable

for i in *
do
    for (( j = 0; j <= $((${#array_dpi[*]} - 1)); ++j ))
    do
        convert $i -resize ${array_size[$j]} ../${array_dpi[$j]}/$i
    done
done
