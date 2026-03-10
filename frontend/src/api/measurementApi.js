import axiosInstance from "./axiosInstance";

export async function createMeasurement(payload) {
  const response = await axiosInstance.post("/api/measurements", payload);
  return response.data;
}
