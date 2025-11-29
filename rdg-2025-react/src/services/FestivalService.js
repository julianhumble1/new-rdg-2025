import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";
import { getBaseUrl } from "./baseUrl";

const baseUrl = getBaseUrl();

export default class FestivalService {
  static createNewFestival = async (
    name,
    venueId,
    year,
    month,
    description,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        `${baseUrl}/festivals`,
        {
          name: name.trim(),
          venueId: venueId,
          year: year,
          month: month,
          description: description.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(`Successfully created ${response.data.festival.name}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getAllFestivals = async () => {
    const token = Cookies.get("token");

    try {
      const response = await axios.get(`${baseUrl}/festivals`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getFestivalById = async (festivalId) => {
    try {
      const response = await axios.get(`${baseUrl}/festivals/${festivalId}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static deleteFestivalById = async (festivalId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.delete(
        `${baseUrl}/festivals/${festivalId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success("Successfully deleted festival.");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static updateFestival = async (
    festivalId,
    name,
    venueId,
    year,
    month,
    description,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `${baseUrl}/festivals/${festivalId}`,
        {
          name: name.trim(),
          venueId: venueId,
          year: year,
          month: month,
          description: description.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(`Successfully updated ${response.data.festival.name}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };
}
