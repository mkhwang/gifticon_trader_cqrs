import http from 'k6/http';
import { sleep, check } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";


const brands = [1,2,3,4,5,6,7,8,9,10];
const keywords = ['아메리카노', '버거', '디저트', '커피', '빙수'];
const tags = ['커피', '간식', '음료', '패스트푸드', '디저트'];

export const options = {
    vus: 100,
    duration: '20s',
};

export default function () {
    const brand = brands[Math.floor(Math.random() * brands.length)];
    const search = keywords[Math.floor(Math.random() * keywords.length)];
    const tag = tags[Math.floor(Math.random() * tags.length)];
    const minPrice = Math.floor(Math.random() * 5000);
    const maxPrice = minPrice + Math.floor(Math.random() * 10000);

    const url = `http://localhost:8080/api/gifticon` +
        `?minPrice=${minPrice}` +
        `&maxPrice=${maxPrice}` +
        `&brand=${encodeURIComponent(brand)}` +
        `&search=${encodeURIComponent(search)}` +
        `&tags=${encodeURIComponent(tag)}`;

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