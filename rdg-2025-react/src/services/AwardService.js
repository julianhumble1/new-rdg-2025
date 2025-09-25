import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";

export default class AwardService {
  static createNewAward = async (name, productionId, personId, festivalId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        "http://localhost:8080/awards",
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
      const response = await axios.get(
        `http://localhost:8080/awards/${awardId}`,
      );
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
        `http://localhost:8080/awards/${awardId}`,
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
      const response = await axios.delete(
        `http://localhost:8080/awards/${awardId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success("Successfully deleted award.");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };
}
