import axios from "axios";

const UNKNOWN_ERROR_MESSAGE = "알 수 없는 오류가 발생했습니다.";

export function getApiErrorMessage(error) {
  if (!error) return UNKNOWN_ERROR_MESSAGE;

  if (axios.isAxiosError(error)) {
    const serverMessage = error.response?.data?.message;

    if (typeof serverMessage === "string" && serverMessage.trim()) {
      return serverMessage;
    }

    if (typeof error.message === "string" && error.message.trim()) {
      return error.message;
    }

    return UNKNOWN_ERROR_MESSAGE;
  }

  if (error instanceof Error && error.message.trim()) {
    return error.message;
  }

  return UNKNOWN_ERROR_MESSAGE;
}