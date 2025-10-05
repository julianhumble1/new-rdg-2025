import { useEffect, useState } from "react";
import NotFound from "./NotFound.jsx";
import CloudinaryService from "../../services/CloudinaryService.js";
import CustomSpinner from "./CustomSpinner.jsx";

const CloudinaryImage = ({ idNumber, folder, newUploadedUrl }) => {
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
  });

  return (
    <div className="w-full h-full">
      {!loading ? (
        exists ? (
          <img src={url} className="w-full h-full object-cover object-center" />
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
