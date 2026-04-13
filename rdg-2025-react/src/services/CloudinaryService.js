import axios from "axios";
import Cookies from "js-cookie";
import { toast } from "react-toastify";
import { getBaseUrl } from "./baseUrl.js";

const baseFolder = import.meta.env.VITE_CLOUDINARY_BASE_FOLDER;

const baseUrl = getBaseUrl();

export default class CloudinaryService {
  static uploadImageToCloudinary = async (formData) => {
    try {
      const response = await axios.post(
        "https://api.cloudinary.com/v1_1/dbher59sh/image/upload",
        formData,
      );
      return response;
    } catch (e) {
      throw new Error(e.message, e);
    }
  };

  static uploadImage = async (image, idNumber, preset) => {
    const publicId = `${baseFolder}_${preset}_${idNumber}`;

    try {
      const signatureResponse = await this.getSignature(
        publicId,
        preset,
        `${baseFolder}/${preset}`,
      );

      const { signature, apiKey, timestamp } = signatureResponse.data;

      const formData = new FormData();
      formData.append("file", image);
      formData.append("upload_preset", preset);
      formData.append("asset_folder", `${baseFolder}/${preset}`);
      formData.append("public_id", publicId);
      formData.append("eager", ["c_fill,w_1600,h_900,g_auto/f_auto/q_auto"]);

      formData.append("signature", signature);
      formData.append("api_key", apiKey);
      formData.append("timestamp", timestamp);

      const response = await this.uploadImageToCloudinary(formData);
      toast.success("Successfully uploaded image");
      return response;
    } catch (e) {
      toast.error(e.message);
      throw new Error(e.message, e);
    }
  };

  static getSignature = async (imageId, uploadPreset, folder) => {
    const token = Cookies.get("token");

    try {
      const response = await axios.get(`${baseUrl}/cloudinary/generate`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: {
          publicId: imageId,
          uploadPreset: uploadPreset,
          folder: folder,
        },
      });
      return response;
    } catch (e) {
      throw new Error(e.message);
    }
  };

  static getUrl = async (idNumber, preset) => {
    const publicId = `${baseFolder}_${preset}_${idNumber}`;
    const response = await axios.get(`${baseUrl}/cloudinary/url`, {
      params: {
        publicId,
      },
    });
    return response;
  };
}
