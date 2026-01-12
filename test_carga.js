import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  vus: 10, 
  duration: '30s',
};

export default function () {
  const url = 'https://cloudlibrary.up.railway.app/';
  const res = http.get(url);

  check(res, {
    'status es 200': (r) => r.status === 200,
    'carga correcta': (r) => r.body.includes('CloudLibrary'),
  });

  sleep(1);
}