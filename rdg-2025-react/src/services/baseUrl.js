const port = import.meta.env.VITE_BACKEND_PORT;
const server = import.meta.env.VITE_BACKEND_SERVER;

export const getBaseUrl = () => {
  // compute base URL from env, ensure protocol present and avoid double-adding a port
  const defaultServer = "http://localhost";
  let serverValue = server || defaultServer;
  // add protocol if missing
  if (!/^https?:\/\//i.test(serverValue)) {
    serverValue = `http://${serverValue}`;
  }
  // append port only if provided and serverValue doesn't already include a port
  let baseUrl = serverValue;
  if (port && !serverValue.match(/:\d+(\/|$)/)) {
    baseUrl = `${serverValue}:${port}`;
  }
  return baseUrl;
};
