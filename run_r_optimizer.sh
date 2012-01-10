#!/bin/bash

rm -rf public-build/

cd public/javascripts

echo "Running r.js"
node ../../r.js -o app.build.js

cd ../..

echo "DONE!"
