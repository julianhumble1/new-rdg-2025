const backendUrl = import.meta.env.VITE_BACKEND_URL;

export const getBaseUrl = () => {
  const baseUrl = backendUrl
  return baseUrl;
};
