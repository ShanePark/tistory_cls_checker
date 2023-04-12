if [ $# -lt 2 ]; then
  echo "Usage: $0 <blog_address> <number_of_iterations>"
  exit 1
fi

blog_address=$1
number_of_iterations=$2

# Create the results directory if it does not exist
mkdir -p results

# Disable exit-on-error behavior
set +e

function run_lighthouse() {
  local URL=$1
  echo "Running Lighthouse audit for $URL"
  lighthouse "$URL" --preset=desktop --quiet --chrome-flags="--headless" --output=html --output-path="results/report_$i.html"
}

for i in $(seq 1 "$number_of_iterations"); do
  URL="$blog_address/$i"
  run_lighthouse "$URL" &

  # Limit the number of background jobs to the number of available CPU cores
  while [ "$(jobs | wc -l)" -ge "$(nproc)" ]; do
    sleep 1
  done
done

# Wait for all remaining background jobs to finish
wait
