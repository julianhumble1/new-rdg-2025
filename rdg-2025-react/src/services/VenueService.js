import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";

export default class VenueService {
  static createNewVenue = async (name, address, town, postcode, notes, url) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        "http://localhost:8080/venues",
        {
          name: name.trim(),
          address: address.trim(),
          town: town.trim(),
          postcode: postcode.trim(),
          notes: notes.trim(),
          url: url.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(`Successfully created ${response.data.venue.name}.`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message);
    }
  };

  static getAllVenues = async () => {
    const token = Cookies.get("token");

    try {
      const response = await axios.get("http://localhost:8080/venues", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message);
    }
  };

  static deleteVenue = async (venueId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.delete(
        `http://localhost:8080/venues/${venueId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success("Successfully deleted venue.");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message);
    }
  };

  static getVenueById = async (venueId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/venues/${venueId}`,
      );
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message);
    }
  };

  static updateVenue = async (
    venueId,
    name,
    address,
    town,
    postcode,
    notes,
    url,
  ) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `http://localhost:8080/venues/${venueId}`,
        {
          name: name.trim(),
          address: address.trim(),
          town: town.trim(),
          postcode: postcode.trim(),
          notes: notes.trim(),
          url: url.trim(),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );

      toast.success(`Successfully updated ${response.data.venue.name}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message);
    }
  };
}
