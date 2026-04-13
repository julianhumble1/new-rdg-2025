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
      const response = await axios.delete(`${baseUrl}/people/${personId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
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
      const response = await axios.get(`${baseUrl}/people/${personId}`, {
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
}