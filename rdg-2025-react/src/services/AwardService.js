import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";
import { getBaseUrl } from "./baseUrl.js";

const baseUrl = getBaseUrl();

export default class AwardService {
  static createNewAward = async (name, productionId, personId, festivalId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        `${baseUrl}/awards`,
        {
          name: name.trim(),
          productionId,
          personId,
          festivalId,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(`Successfully created ${response.data.award.name}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getAwardById = async (awardId) => {
    try {
      const response = await axios.get(`${baseUrl}/awards/${awardId}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static updateAward = async (
    awardId,
    name,
    productionId,
    personId,
    festivalId,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `${baseUrl}/awards/${awardId}`,
        {
          name: name.trim(),
          productionId,
          personId,
          festivalId,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      console.log(`Successfully updated ${response.data.award.name}`);
      toast.success(`Successfully updated ${response.data.award.name}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static deleteAward = async (awardId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.delete(`${baseUrl}/awards/${awardId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      toast.success("Successfully deleted award.");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };
}
