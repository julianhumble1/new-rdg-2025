import { useEffect, useState } from "react";
import NotFound from "./NotFound.jsx";
import CloudinaryService from "../../services/CloudinaryService.js";

const CloudinaryImage = ({ idNumber, folder }) => {
  const [url, setUrl] = useState("");
  const [exists, setExists] = useState(false);
  const [connection, setConnection] = useState(true);

  useEffect(() => {
    const getUrl = async () => {
      try {
        const response = await CloudinaryService.getUrl(idNumber, folder);
        setUrl(response.data.url);
        setExists(true);
      } catch (e) {
        if (e.status === 404) {
          setExists(false);
        } else {
          setConnection(false);
        }
      }
    };
    getUrl();
  });

  return (
    <div className="w-full h-full">
      {connection &&
        (exists ? (
          <img src={url} className="w-full h-full object-cover object-center" />
        ) : (
          <NotFound />
        ))}
    </div>
  );
};

export default CloudinaryImage;
