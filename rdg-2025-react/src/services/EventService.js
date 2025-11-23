import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";
import { getBaseUrl } from "./baseUrl";

const baseUrl = getBaseUrl();

export default class EventService {
  static addNewEvent = async (name, description, dateTime, venue) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        `${baseUrl}/events`,
        {
          name: name.trim(),
          description: description.trim(),
          dateTime,
          venueId: venue,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(`Successfully created ${response.data.event.name}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getEventById = async (eventId) => {
    try {
      const response = await axios.get(`${baseUrl}/events/${eventId}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getAllEvents = async () => {
    try {
      const response = await axios.get(`${baseUrl}/events`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static updateEvent = async (eventId, name, description, dateTime, venue) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.patch(
        `${baseUrl}/events/${eventId}`,
        {
          name: name.trim(),
          description: description.trim(),
          dateTime,
          venueId: venue,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      );
      toast.success(`Successfully updated ${response.data.event.name}`);
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static deleteEvent = async (eventId) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.delete(`${baseUrl}/events/${eventId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      toast.success("Successfully deleted event.");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };
}
