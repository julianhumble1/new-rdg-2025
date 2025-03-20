import axios from "axios";

export default class CloudinaryService {

    static uploadImage = async (formData) => {
        for (let [key, value] of formData.entries()) {
            console.log(`${key}: ${value}`);
        }
        try {
            const response = await axios.post(
                "https://api.cloudinary.com/v1_1/dbher59sh/image/upload",
                formData
            );
            return response
        } catch (e) {
            throw new Error(e.message)
        }
    }

}