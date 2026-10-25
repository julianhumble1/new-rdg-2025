import axios from "axios";
import Cookies from "js-cookie";

type ApiError = {};

const baseURL = import.meta.env.VITE_BACKEND_URL;

const client = axios.create({
  baseURL,
});

client.interceptors.request.use((config) => {
  const token = Cookies.get("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
