import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("API Error:", error);

    if (error.response) {
      console.error("Status:", error.response.status);
      console.error("Response Data:", error.response.data);
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;