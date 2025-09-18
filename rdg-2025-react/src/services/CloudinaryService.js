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

  static uploadHomeImage = async (image, position) => {
    try {
      const signatureResponse = await this.getSignature(
        position,
        "home",
        "dev/home",
      );

      const { signature, apiKey, timestamp } = signatureResponse.data;

      const formData = new FormData();
      formData.append("file", image);
      formData.append("upload_preset", "home");
      formData.append("asset_folder", "dev/home");
      formData.append("public_id", position);
      formData.append("signature", signature);
      formData.append("api_key", apiKey);
      formData.append("timestamp", timestamp);

      await this.uploadImage(formData);
    } catch (e) {
      throw new Error(e.message);
    }
  };

  static getSignature = async (imageId, uploadPreset, folder) => {
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
            folder: folder,
          },
        },
      );
      return response;
    } catch (e) {
      throw new Error(e.message);
    }
  };
}
