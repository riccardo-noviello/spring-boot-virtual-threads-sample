docker run --network="host" -i --rm -v "$PWD":/scripts grafana/k6 run /scripts/test.js
