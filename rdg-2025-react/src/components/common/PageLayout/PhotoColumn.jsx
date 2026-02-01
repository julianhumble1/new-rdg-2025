import Socials from "../../modals/Socials.jsx";
import { useState } from "react";
import CustomSpinner from "../CustomSpinner.jsx";

const PhotoColumn = ({ imgSrc }) => {
  const [loaded, setLoaded] = useState(false);

  return (
    <div className="flex-1 flex flex-col justify-start">
      {!loaded && <CustomSpinner />}

      <img
        src={imgSrc}
        alt=""
        loading="eager"
        decoding="async"
        fetchPriority="high"
        onLoad={() => setLoaded(true)}
        className={`w-full h-auto transition-opacity duration-300 ${
          loaded ? "opacity-100" : "opacity-0"
        }`}
      />

      <div className="md:hidden">
        <div className="flex justify-center">Find us on socials:</div>
        <Socials />
      </div>
    </div>
  );
};

export default PhotoColumn;
