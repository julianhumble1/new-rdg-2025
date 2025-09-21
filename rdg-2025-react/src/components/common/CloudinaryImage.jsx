// import { useEffect, useState } from "react";

import { AdvancedImage } from "@cloudinary/react";
import { Cloudinary } from "@cloudinary/url-gen/index";
import { useEffect, useState } from "react";
import NotFound from "./NotFound.jsx";

// export default CloudinaryImage;
const baseFolder = import.meta.env.VITE_CLOUDINARY_BASE_FOLDER;

const CloudinaryImage = ({ idNumber, folder, version = "" }) => {
  const [exists, setExists] = useState(true);

  const cloudName = "dbher59sh";

  const cld = new Cloudinary({
    cloud: {
      cloudName,
    },
  });

  const myImage = cld.image(`${baseFolder}_${folder}_${idNumber}.jpg`);
  if (version) myImage.setVersion(version);

  useEffect(() => {
    const checkExists = async () => {
      const exists = await fetch(myImage.toURL(), { method: "HEAD" })
        .then((r) => r.ok)
        .catch(() => false);

      setExists(exists);
    };
    checkExists();
  });

  return (
    <div className="w-full h-full">
      {exists ? <AdvancedImage cldImg={myImage} /> : <NotFound />}
    </div>
  );
};

export default CloudinaryImage;
