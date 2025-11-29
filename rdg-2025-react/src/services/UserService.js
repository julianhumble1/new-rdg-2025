import axios from "axios";
import Cookies from "js-cookie";
import { getBaseUrl } from "./baseUrl";

const baseUrl = getBaseUrl();

export default class UserService {
  static login = async (username, password) => {
    try {
      const response = await axios.post(`${baseUrl}/auth/signin`, {
        username: username,
        password: password,
      });
      return response.data;
    } catch (error) {
      throw new Error(error.message);
    }
  };

  static checkUser = async () => {
    try {
      const token = Cookies.get("token");
      const response = await axios.get(`${baseUrl}/checkAuth/user`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response;
    } catch (error) {
      throw new Error(error);
    }
  };

  static checkAdmin = async () => {
    try {
      const token = Cookies.get("token");
      const response = await axios.get(`${baseUrl}/checkAuth/admin`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response;
    } catch (error) {
      throw new Error(error);
    }
  };
}
