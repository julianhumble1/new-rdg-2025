import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";
import { getBaseUrl } from "./baseUrl";

const baseUrl = getBaseUrl();

export default class PerformanceService {
  static addNewPerformance = async (
    productionId,
    venueId,
    festivalId,
    performanceTime,
    description,
    standardPrice,
    concessionPrice,
    boxOffice,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        `${baseUrl}/performances`,
        {
          productionId: productionId,
          venueId: venueId,
          festivalId: festivalId,
          time: performanceTime,
          description: description.trim(),
          standardPrice: standardPrice.trim(),
          concessionPrice: concessionPrice.trim(),
          boxOffice: boxOffice.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(`Successfully created ${response.data.performance.id}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static deletePerformance = async (performanceId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.delete(
        `${baseUrl}/performances/${performanceId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success("Successfully deleted performance.");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getPerformanceById = async (performanceId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.get(
        `${baseUrl}/performances/${performanceId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static updatePerformance = async (
    performanceId,
    productionId,
    venueId,
    festivalId,
    performanceTime,
    description,
    standardPrice,
    concessionPrice,
    boxOffice,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `${baseUrl}/performances/${performanceId}`,
        {
          productionId: productionId,
          venueId: venueId,
          festivalId: festivalId,
          time: performanceTime,
          description: description.trim(),
          standardPrice: standardPrice.trim(),
          concessionPrice: concessionPrice.trim(),
          boxOffice: boxOffice.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(
        `Successfully updated performance ${response.data.performance.id}`,
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };
}
