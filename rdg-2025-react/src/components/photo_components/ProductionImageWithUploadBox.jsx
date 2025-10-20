import { useEffect, useState } from "react";
import CloudinaryService from "../../services/CloudinaryService.js";
import CloudinaryImage from "../common/CloudinaryImage.jsx";
import Cookies from "js-cookie";

const ProductionImageWithUploadBox = ({ productionId }) => {
  const [selectedImg, setSelectedImage] = useState(null);
  const [uploadedUrl, setUploadedUrl] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    // Check if user is admin
    const roleFromCookies = Cookies.get("role");
    setIsAdmin(roleFromCookies === "ROLE_ADMIN");
  }, []);

  const handleFileChange = async (e) => {
    const file = e.target.files?.[0];
    if (file) {
      setSelectedImage(file);
    }
  };

  useEffect(() => {
    // don't run on mount when no image is selected
    if (!selectedImg) return;

    const upload = async () => {
      try {
        const response = await CloudinaryService.uploadImage(
          selectedImg,
          productionId,
          "production",
        );
        setUploadedUrl(response.data.secureUrl);
      } catch (e) {
        return;
      }
    };

    upload();
  }, [selectedImg, productionId]);

  return (
    <div className="relative">
      {/* Image display */}
      <div className="relative overflow-hidden rounded aspect-[3/4] max-w-sm mx-auto">
        <CloudinaryImage
          folder="production"
          idNumber={productionId}
          newUploadedUrl={uploadedUrl}
        />

        {/* Admin hover overlay - only show for admins */}
        {isAdmin && (
          <label className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-40 text-white opacity-0 hover:opacity-100 transition cursor-pointer">
            <div className="text-center">
              <div className="font-semibold">Click to select an image</div>
              <div className="text-sm mt-1">Portrait images work best</div>
            </div>
            {/* hidden file input inside label so clicks open file picker */}
            <input
              type="file"
              accept="image/*"
              onChange={handleFileChange}
              className="hidden"
            />
          </label>
        )}
      </div>
    </div>
  );
};

export default ProductionImageWithUploadBox;