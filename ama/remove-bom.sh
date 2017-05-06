find . -type f -exec sed -i -e '1s/^\xEF\xBB\xBF//' {} \;
