import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";

export default class ProductionService {
  static createNewProduction = async (
    name,
    venueId,
    author,
    description,
    auditionDate,
    sundowners,
    notConfirmed,
    flyerFile,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        "http://localhost:8080/productions",
        {
          name: name.trim(),
          venueId: venueId,
          author: author.trim(),
          description: description.trim(),
          auditionDate: auditionDate,
          sundowners: sundowners,
          notConfirmed: notConfirmed,
          flyerFile: flyerFile.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(`Successfully created ${response.data.production.name}.`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getAllProductions = async () => {
    try {
      const response = await axios.get("http://localhost:8080/productions");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getFutureProductions = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/productions/future",
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getProductionById = async (productionId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/productions/${productionId}`,
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static updateProduction = async (
    productionId,
    name,
    venueId,
    author,
    description,
    auditionDate,
    sundowners,
    notConfirmed,
    flyerFile,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `http://localhost:8080/productions/${productionId}`,
        {
          name: name.trim(),
          venueId: venueId,
          author: author.trim(),
          description: description.trim(),
          auditionDate: auditionDate,
          sundowners: sundowners,
          notConfirmed: notConfirmed,
          flyerFile: flyerFile.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(`Successfully updated ${response.data.production.name}.`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static updateProductionWithImage = async (productionData, imageId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `http://localhost:8080/productions/${productionData.id}`,
        {
          name: productionData.name.trim(),
          venueId: productionData.venueId,
          author: productionData.author.trim(),
          description: productionData.description.trim(),
          auditionDate: productionData.auditionDate,
          sundowners: productionData.sundowners,
          notConfirmed: productionData.notConfirmed,
          flyerFile: imageId,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      return response;
    } catch (e) {
      if (e.response.status === 500) {
        throw new Error("Internal Server Error.");
      } else if (e.response.status === 404) {
        throw new Error("No Production/Venue with this id.");
      } else if (e.response.status === 409) {
        throw new Error("Production with this name already exists.");
      } else if (e.response.status === 401 || e.response.status === 403) {
        throw new Error("Failed to authenticate as administrator.");
      }
    }
  };

  static deleteProduction = async (productionId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.delete(
        `http://localhost:8080/productions/${productionId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success("Successfully deleted production.");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };
}
