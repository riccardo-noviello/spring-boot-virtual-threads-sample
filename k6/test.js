import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 50 },  // ramp up to 50 users over 30s
    { duration: '1m', target: 50 },   // hold 50 users for 1 minute
    { duration: '30s', target: 0 },   // ramp down
  ],
};

export default function () {
  const res = http.get('http://localhost:8080/customers');
  
  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(1);
}

