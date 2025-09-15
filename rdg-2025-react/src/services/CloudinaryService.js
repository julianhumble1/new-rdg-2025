import axios from "axios";
import Cookies from "js-cookie";

export default class CloudinaryService {
  static uploadImage = async (formData) => {
    for (let [key, value] of formData.entries()) {
      console.log(`${key}: ${value}`);
    }
    try {
      const response = await axios.post(
        "https://api.cloudinary.com/v1_1/dbher59sh/image/upload",
        formData,
      );
      return response;
    } catch (e) {
      throw new Error(e.message);
    }
  };

  static getSignature = async (imageId, uploadPreset) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.get(
        "http://localhost:8080/cloudinary/generate",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            publicId: imageId,
            uploadPreset: uploadPreset,
          },
        },
      );
      return response;
    } catch (e) {
      throw new Error(e.message);
    }
  };
}
