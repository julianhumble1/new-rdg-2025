import { useEffect, useState } from "react";
import NotFound from "./NotFound.jsx";
import CloudinaryService from "../../services/CloudinaryService.js";
import CustomSpinner from "./CustomSpinner.jsx";

const CloudinaryImage = ({ idNumber, folder, newUploadedUrl, fallbackSrc }) => {
  const [url, setUrl] = useState("");
  const [exists, setExists] = useState(true);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getUrl = async () => {
      if (newUploadedUrl) {
        setUrl(newUploadedUrl);
        return;
      }
      try {
        const response = await CloudinaryService.getUrl(idNumber, folder);
        setUrl(response.data.url);
        setExists(true);
      } catch (e) {
        if (e.status === 404) {
          setExists(false);
        }
      } finally {
        setLoading(false);
      }
    };
    getUrl();
  }, [newUploadedUrl, folder, idNumber]);

  return (
    <div className="w-full h-full">
      {!loading ? (
        exists ? (
          <img src={url} className="w-full h-full object-contain object-center" />
        ) : fallbackSrc ? (
          <div className="w-full h-full flex items-center justify-center bg-gray-100">
            <img src={fallbackSrc} className="max-h-full max-w-full" />
          </div>
        ) : (
          <NotFound />
        )
      ) : (
        <CustomSpinner />
      )}
    </div>
  );
};

export default CloudinaryImage;
