import Cookies from "js-cookie";
import axios from "axios";
import { toast } from "react-toastify";
import { getBaseUrl } from "./baseUrl";

const baseUrl = getBaseUrl();

export default class PersonService {
  static addNewPerson = async (
    firstName,
    lastName,
    summary,
    homePhone,
    mobilePhone,
    addressStreet,
    addressTown,
    addressPostcode,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        `${baseUrl}/people`,
        {
          firstName: firstName.trim(),
          lastName: lastName.trim(),
          summary: summary.trim(),
          homePhone: homePhone.trim(),
          mobilePhone: mobilePhone.trim(),
          addressStreet: addressStreet.trim(),
          addressTown: addressTown.trim(),
          addressPostcode: addressPostcode.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(
        `Successfully created ${response.data.person.firstName} ${response.data.person.lastName}`,
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getAllPeople = async () => {
    const token = Cookies.get("token");

    try {
      const response = await axios.get(`${baseUrl}/people`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response;
    } catch (e) {
      toast.error(e);
      throw new Error(e.message, e);
    }
  };

  static deletePersonById = async (personId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.delete(
        `${baseUrl}/people/${personId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success("Successfully deleted person.");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getPersonById = async (personId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.get(
        `${baseUrl}/people/${personId}`,
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

  static updatePerson = async (
    personId,
    firstName,
    lastName,
    summary,
    homePhone,
    mobilePhone,
    addressStreet,
    addressTown,
    addressPostcode,
    imageId,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `${baseUrl}/people/${personId}`,
        {
          firstName: firstName.trim(),
          lastName: lastName.trim(),
          summary: summary.trim(),
          homePhone: homePhone.trim(),
          mobilePhone: mobilePhone.trim(),
          addressStreet: addressStreet.trim(),
          addressTown: addressTown.trim(),
          addressPostcode: addressPostcode.trim(),
          imageId: imageId,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(
        `Successfully updated ${response.data.person.firstName} ${response.data.person.lastName}`,
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static updatePersonWithImage = async (personData, imageId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `${baseUrl}/people/${personData.id}`,
        {
          firstName: personData.firstName.trim(),
          lastName: personData.lastName.trim(),
          summary: personData.summary.trim(),
          homePhone: personData.homePhone.trim(),
          mobilePhone: personData.mobilePhone.trim(),
          addressStreet: personData.addressStreet.trim(),
          addressTown: personData.addressTown.trim(),
          addressPostcode: personData.addressPostcode.trim(),
          imageId: imageId,
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
      } else if (e.response.status === 409) {
        throw new Error("Person with this name already exists.");
      } else if (e.response.status === 404) {
        throw new Error("No Person with this id.");
      } else if (e.response.status === 400) {
        throw new Error("Bad Request: Details not in expected format.");
      } else if (e.response.status === 401 || e.response.status === 403) {
        throw new Error("Failed to authenticate as administrator.");
      }
    }
  };
}
