// ...existing code...
import { useEffect, useState } from "react";
import CloudinaryService from "../../services/CloudinaryService.js";
import CloudinaryImage from "../common/CloudinaryImage.jsx";

const SingleImageSelect = ({ position }) => {
  const [selectedImg, setSelectedImage] = useState(null);

  // store a preview URL (string) not the File object
  const [feedback, setFeedback] = useState(null);

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
        const resp = await CloudinaryService.uploadImage(
          selectedImg,
          position,
          "home",
        );
        setFeedback("Successfully uploaded image");
      } catch (e) {
        setFeedback(e.message);
      }
    };

    upload();
  }, [selectedImg, position]);

  return (
    <div className="bg-slate-200 rounded p-2 m-2 shadow flex flex-col h-80">
      <div className="text-center font-bold">Image {position}</div>
      <div>{feedback}</div>

      {/* label acts as clickable/hoverable area that triggers the hidden file input */}
      <label className="border border-black border-dashed mx-auto text-center w-5/6 h-5/6 my-auto relative overflow-hidden rounded group cursor-pointer">
        <CloudinaryImage folder="home" idNumber={position} />

        {/* hover overlay shown when user hovers the image area */}
        <div className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-40 text-white opacity-0 group-hover:opacity-100 transition">
          <div className="text-center">
            <div className="font-semibold">Click to select an image</div>
          </div>
        </div>

        {/* hidden file input inside label so clicks open file picker */}
        <input
          type="file"
          accept="image/*"
          onChange={handleFileChange}
          className="hidden"
        />
      </label>
    </div>
  );
};

export default SingleImageSelect;
// ...existing code...
