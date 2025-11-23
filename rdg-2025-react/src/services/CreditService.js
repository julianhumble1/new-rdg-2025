import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";
import { getBaseUrl } from "./baseUrl";

const baseUrl = getBaseUrl();

export default class CreditService {
  static addNewCredit = async (name, type, productionId, personId, summary) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        `${baseUrl}/credits`,
        {
          name: name.trim(),
          type: type,
          productionId: productionId,
          personId: personId,
          summary: summary.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(
        `Successfully created ${response.data.credit.type} ${response.data.credit.name}`,
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getCreditById = async (creditId) => {
    try {
      const response = await axios.get(`${baseUrl}/credits/${creditId}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static updateCredit = async (
    creditId,
    name,
    type,
    productionId,
    personId,
    summary,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `${baseUrl}/credits/${creditId}`,
        {
          name: name.trim(),
          type: type,
          productionId: productionId,
          personId: personId,
          summary: summary.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(
        `Successfully updated ${response.data.credit.type} ${response.data.credit.name}`,
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static deleteCreditById = async (creditId) => {
    const token = Cookies.get("token");
    try {
      const response = await axios.delete(`${baseUrl}/credits/${creditId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      toast.success("Successfully deleted credit.");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };
}
