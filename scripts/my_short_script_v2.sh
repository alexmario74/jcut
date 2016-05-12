#!/bin/bash

function help {
    echo "Utilizza lo script come segue:"
    echo ""
    echo "\$ $1 <data_file_name>"
    echo ""
    echo "<data_file_name>: il nome del file csv presente nella cartella data"
    echo ""
    echo ""
}

if [ "$#" -ne 1 ]; then
    help $0;
    exit 0
fi

data=“../data"

cut -f 3,5 -d , ${data}/$1
