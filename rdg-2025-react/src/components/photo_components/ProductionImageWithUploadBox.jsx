import { AdvancedImage } from "@cloudinary/react";
import { PhotoIcon } from "@heroicons/react/16/solid";
import { FileInput } from "flowbite-react";
import { useState } from "react";
import CloudinaryService from "../../services/CloudinaryService.js";
import ProductionService from "../../services/ProductionService.js";
import { Slide, ToastContainer, toast } from "react-toastify";

const ProductionImageWithUploadBox = ({
  image,
  productionData,
  fetchProductionData,
}) => {
  const [imageUpload, setImageUpload] = useState(false);

  const [file, setFile] = useState(null);

  const [imageSuccessMessage, setImageSuccessMessage] = useState("");
  const [imageErrorMessage, setImageErrorMessage] = useState("");

  const [productionSuccessMessage, setProductionSuccessMessage] = useState("");
  const [productionErrorMessage, setProductionErrorMessage] = useState("");

  const notifyPromise = () =>
    toast.promise(handleSubmitImage, {
      pending: "Uploading image...",
      success: imageSuccessMessage,
      error: imageErrorMessage,
    });

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    setFile(selectedFile);
  };

  const handleSubmitImage = async () => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("upload_preset", "flyers");
    formData.append("public_id", productionData.id);
    const signatureResponse = await getCloudinaryProductionSignature(
      productionData.id,
    );
    const { timestamp, signature, apiKey } = signatureResponse.data;
    formData.append("signature", signature);
    formData.append("api_key", apiKey);
    formData.append("timestamp", timestamp);
    const imageResponse = await submitImageToCloudinary(formData);
    if (imageResponse) {
      await updateProductionImageId(imageResponse.data.public_id);
    }
    setImageUpload(false);
    fetchProductionData();
  };

  const getCloudinaryProductionSignature = async () => {
    try {
      const response = await CloudinaryService.getSignature(
        productionData.id,
        "flyers",
      );
      console.log(response);
      return response;
    } catch (e) {
      setImageErrorMessage(e.message);
    }
  };

  const submitImageToCloudinary = async (formData) => {
    try {
      const response = await CloudinaryService.uploadImage(formData);
      setImageSuccessMessage("Successfully uploaded image!");
      return response;
    } catch (e) {
      setImageErrorMessage(e.message);
    }
  };

  const updateProductionImageId = async (imageId) => {
    try {
      const response = await ProductionService.updateProductionWithImage(
        productionData,
        imageId,
      );
      console.log(response);
      setProductionSuccessMessage("Successfully saved image id to database");
    } catch (e) {
      setProductionErrorMessage(e.message);
    }
  };

  return (
    <div className="border-slate-700 rounded-lg w-fit p-3 pb-0 text-center">
      <AdvancedImage
        cldImg={image}
        className="max-w-48 max-h-48 rounded border-4 border-white"
      />
      <button
        className="font-bold text-sm hover:underline w-fit pt-2"
        onClick={() => setImageUpload(!imageUpload)}
      >
        <div className="flex">
          <PhotoIcon className="mr-2 h-5 w-5" />
          <div>Upload image</div>
        </div>
      </button>
      {imageUpload && (
        <div>
          <FileInput onChange={handleFileChange} sizing="sm" />
          <button
            className={`font-bold text-sm hover:underline ${!file && "cursor-not-allowed"}`}
            onClick={notifyPromise}
          >
            Upload
          </button>
        </div>
      )}
      <ToastContainer
        position="bottom-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick={false}
        rtl={false}
        pauseOnFocusLoss
        pauseOnHover
        theme="light"
        transition={Slide}
      />
    </div>
  );
};

export default ProductionImageWithUploadBox;
