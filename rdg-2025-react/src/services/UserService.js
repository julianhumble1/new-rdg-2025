import axios from "axios";
import Cookies from "js-cookie";
import { getBaseUrl } from "./baseUrl";
import { toast } from "react-toastify";

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

  static getAllUsers = async () => {
    try {
      const token = Cookies.get("token");
      const response = await axios.get(`${baseUrl}/users`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response;
    } catch (error) {
      toast.error(error.message);
      throw new Error(error);
    }
  };

  static updatePassword = async (newPassword, userId) => {
    try {
      const token = Cookies.get("token");
      const response = await axios.patch(
        `${baseUrl}/auth/${userId}/reset-password`,
        {
          newPassword,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      return response;
    } catch (error) {
      toast.error(error.message);
      throw new Error(error);
    }
  };

  static createUser = async (name, email, role, password) => {
    const token = Cookies.get("token");
    try {
      const response = await axios.post(
        `${baseUrl}/auth/signup`,
        {
          name,
          username: email,
          email,
          role: [role],
          password,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e);
    }
  };

  static updateOwnPassword = async (newPassword) => {
    try {
      const token = Cookies.get("token");
      const response = await axios.patch(
        `${baseUrl}/auth/me/reset-password`,
        {
          newPassword,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      return response;
    } catch (error) {
      toast.error(error.message);
      throw new Error(error);
    }
  };
}
