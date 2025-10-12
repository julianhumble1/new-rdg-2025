import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";

export default class EventService {
  static addNewEvent = async (name, description, dateTime) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.post(
        "http://localhost:8080/events",
        {
          name: name.trim(),
          description: description.trim(),
          dateTime,
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
}
