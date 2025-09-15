import axios from "axios";
import Cookies from "js-cookie";

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
        "http://localhost:8080/performances",
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
      return response;
    } catch (e) {
      if (e.response.status === 500) {
        throw new Error("Internal server error");
      } else if (e.response.status === 404) {
        throw new Error("No Production/Venue/Festival with this id.");
      } else if (e.response.status === 400) {
        throw new Error("Bad Request: Details not in expected format.");
      } else if (e.response.status === 401 || e.response.status === 403) {
        throw new Error("Failed to authenticate as administrator.");
      }
    }
  };

  static deletePerformance = async (performanceId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.delete(
        `http://localhost:8080/performances/${performanceId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      return response;
    } catch (e) {
      if (e.response.status === 500) {
        throw new Error("Internal server error");
      } else if (e.response.status === 404) {
        throw new Error("No Performance with this id.");
      } else if (e.response.status === 400) {
        throw new Error("Bad Request: Details not in expected format.");
      } else if (e.response.status === 401 || e.response.status === 403) {
        throw new Error("Failed to authenticate as administrator.");
      }
    }
  };

  static getPerformanceById = async (performanceId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.get(
        `http://localhost:8080/performances/${performanceId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      return response;
    } catch (e) {
      if (e.response.status === 500) {
        throw new Error("Internal server error");
      } else if (e.response.status === 404) {
        throw new Error("No Performance with this id.");
      }
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
        `http://localhost:8080/performances/${performanceId}`,
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
      return response;
    } catch (e) {
      if (e.response.status === 500) {
        throw new Error("Internal server error");
      } else if (e.response.status === 404) {
        throw new Error("No Production/Venue/Festival with this id.");
      } else if (e.response.status === 400) {
        throw new Error("Bad Request: Details not in expected format.");
      } else if (e.response.status === 401 || e.response.status === 403) {
        throw new Error("Failed to authenticate as administrator.");
      }
    }
  };
}
