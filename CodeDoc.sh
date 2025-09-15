#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 <output_file.pdf>"
    exit 1
fi

OUTPUT_PDF="$1"

echo "Finding all .java files and generating PDF..."
find . -name "*.java" -print0 | xargs -0 enscript --color -Ejava -o - | ps2pdf - "$OUTPUT_PDF"

if [ ${PIPESTATUS[0]} -eq 0 ]; then
    echo "PDF successfully created: $OUTPUT_PDF"
else
    echo "Failed to create PDF. Please check for errors above."
fi