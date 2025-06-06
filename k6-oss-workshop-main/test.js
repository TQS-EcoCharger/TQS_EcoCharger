import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomItem } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

export const options = {
  stages: [
    { duration: '30s', target: 50 },
    { duration: '1m', target: 500 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_failed: ['rate<0.05'],
    checks: ['rate>0.95'],
  },
};

const BASE_URL = 'http://localhost:5000';

const users = [
  { email: 'afonso@gmail.com', password: 'pass', userId: 1 },
  { email: 'ricardo.antunes2002@gmail.com', password: 'banana', userId: 2 },
];

const VALID_CHARGING_POINTS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

export default function () {
  const user = randomItem(users);

  const loginPayload = JSON.stringify({
    email: user.email,
    password: user.password,
  });

  const loginRes = http.post(`${BASE_URL}/api/auth/login`, loginPayload, {
    headers: { 'Content-Type': 'application/json' },
  });

  check(loginRes, {
    'login succeeded': (r) => r.status === 200,
    'token received': (r) => !!r.json('token'),
  });

  const token = loginRes.json('token');
  if (!token) return;

const now = new Date();
const start = new Date(now);

start.setDate(now.getDate() + (__VU - 1));

const slot = __ITER % 24; 
const baseHour = 8 + Math.floor(slot / 2); 
const baseMinute = (slot % 2) * 30; 

start.setHours(baseHour, baseMinute, 0, 0);
const end = new Date(start.getTime() + 30 * 60 * 1000); 


const chargingPointId = VALID_CHARGING_POINTS[(__VU + __ITER) % VALID_CHARGING_POINTS.length];

const reservation = {
  userId: user.userId,
  chargingPointId,
  startTime: start.toISOString(),
  endTime: end.toISOString(),
};

const reservationPayload = JSON.stringify(reservation);

  const reservationRes = http.post(`${BASE_URL}/api/v1/reservation`, reservationPayload, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
  });

  check(reservationRes, {
    'reservation created': (r) => r.status === 200 || r.status === 201,
  });

  sleep(1);
}
