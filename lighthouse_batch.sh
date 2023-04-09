if [ $# -lt 2 ]; then
  echo "Usage: $0 <blog_address> <number_of_iterations>"
  exit 1
fi

blog_address=$1
number_of_iterations=$2

# Create the results directory if it does not exist
mkdir -p results

for i in $(seq 1 "$number_of_iterations"); do
  URL="$blog_address/$i"
  echo "Running Lighthouse audit for $URL"
  lighthouse "$URL" --preset=desktop --quiet --chrome-flags="--headless" --output=html --output-path="results/report_$i.html"
done
