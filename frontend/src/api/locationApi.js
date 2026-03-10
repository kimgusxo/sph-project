import axiosInstance from "./axiosInstance";

/**
 * 위치 생성
 * POST /api/locations
 *
 * 주소 생성: { address }
 * 좌표 생성: { x, y }
 */
export async function createLocation(payload) {
  const response = await axiosInstance.post("/api/locations", payload);
  return response.data;
}

/**
 * 반경 조회 - 주소 기준
 * GET /api/locations/around?address=...
 */
export async function searchAroundByAddress(address) {
  const response = await axiosInstance.get("/api/locations/around", {
    params: { address },
  });
  return response.data;
}

/**
 * 반경 조회 - 좌표 기준
 * GET /api/locations/around?x=...&y=...
 */
export async function searchAroundByCoordinate(x, y) {
  const response = await axiosInstance.get("/api/locations/around", {
    params: { x, y },
  });
  return response.data;
}