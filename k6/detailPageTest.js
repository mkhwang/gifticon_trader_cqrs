import http from 'k6/http';
import { sleep, check } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";


export const options = {
    vus: 100,
    duration: '20s',
};

export default function () {
    const id = Math.floor(Math.random() * 10000) + 1; // 1~10000 범위
    const url = `http://localhost:8080/api/gifticon/${id}`;

    const res = http.get(url);
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
    sleep(0.1);
}

export function handleSummary(data) {
    return {
        'summary.html': htmlReport(data),
    };
}