export const DEFAULT_RADIUS_METERS = 1000;

export const INITIAL_LOCATIONS = [
  {
    id: 1,
    name: "측정 지점 1",
    address: "서울특별시 중구 세종대로 110",
    lon: 126.9779692,
    lat: 37.5662952,
    measurement: {
      maxVal: 89,
      minVal: 37,
      avgVal: 58,
    },
  },
  {
    id: 2,
    name: "측정 지점 2",
    address: "서울특별시 중구 덕수궁길 15",
    lon: 126.97529,
    lat: 37.56593,
    measurement: {
      maxVal: 72,
      minVal: 31,
      avgVal: 49,
    },
  },
  {
    id: 3,
    name: "측정 지점 3",
    address: "서울특별시 중구 무교로 21",
    lon: 126.97962,
    lat: 37.56729,
    measurement: {
      maxVal: 66,
      minVal: 29,
      avgVal: 44,
    },
  },
  {
    id: 4,
    name: "측정 지점 4",
    address: "서울특별시 중구 청계천로 40",
    lon: 126.98198,
    lat: 37.5692,
    measurement: {
      maxVal: 93,
      minVal: 41,
      avgVal: 61,
    },
  },
  {
    id: 5,
    name: "측정 지점 5",
    address: "서울특별시 중구 명동길 74",
    lon: 126.98485,
    lat: 37.56362,
    measurement: {
      maxVal: 78,
      minVal: 35,
      avgVal: 52,
    },
  },
];

const MOCK_ADDRESS_BOOK = [
  {
    name: "서울시청",
    address: "서울특별시 중구 세종대로 110",
    lon: 126.9779692,
    lat: 37.5662952,
  },
  {
    name: "덕수궁",
    address: "서울특별시 중구 덕수궁길 15",
    lon: 126.97529,
    lat: 37.56593,
  },
  {
    name: "무교동 측정 포인트",
    address: "서울특별시 중구 무교로 21",
    lon: 126.97962,
    lat: 37.56729,
  },
  {
    name: "청계천 측정 포인트",
    address: "서울특별시 중구 청계천로 40",
    lon: 126.98198,
    lat: 37.5692,
  },
  {
    name: "명동 측정 포인트",
    address: "서울특별시 중구 명동길 74",
    lon: 126.98485,
    lat: 37.56362,
  },
  {
    name: "서울도서관",
    address: "서울특별시 중구 세종대로 110",
    lon: 126.97765,
    lat: 37.56667,
  },
];

function normalize(value) {
  return String(value ?? "")
    .toLowerCase()
    .replace(/\s+/g, "")
    .trim();
}

export function resolveMockAddress(input) {
  const keyword = normalize(input);

  if (!keyword) return null;

  return (
    MOCK_ADDRESS_BOOK.find((item) => {
      const address = normalize(item.address);
      const name = normalize(item.name);

      return (
        address.includes(keyword) ||
        keyword.includes(address) ||
        name.includes(keyword)
      );
    }) ?? null
  );
}