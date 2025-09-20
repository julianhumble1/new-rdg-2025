import { XMarkIcon } from "@heroicons/react/16/solid";
import { useEffect, useRef, useState } from "react";
import CloudinaryService from "../../services/CloudinaryService.js";

const SingleImageSelect = ({ position }) => {
  const [selectedImg, setSelectedImage] = useState(null);
  const [previewUrl, setPreviewUrl] = useState(null);

  const [feedback, setFeedback] = useState(null);

  //   const firstRender = useRef(true);

  const handleFileChange = async (e) => {
    const file = e.target.files?.[0];
    if (file) {
      setSelectedImage(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewUrl(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  useEffect(() => {
    // don't run on mount when no image is selected
    if (!selectedImg) return;
      
    const upload = async () => {
      try {
        await CloudinaryService.uploadImage(selectedImg, position, "home")
      } catch (e) {
        setFeedback(e.message);
      }
    }

    upload();
  }, [selectedImg, position]);

  return (
    <div className="bg-slate-200 rounded p-2 m-2 shadow flex flex-col h-80">
      <div className="text-center font-bold">Image {position}</div>
      <div>{feedback}</div>

      {/* label acts as clickable/hoverable area that triggers the hidden file input */}
      <label className="border border-black border-dashed mx-auto text-center w-5/6 h-5/6 my-auto relative overflow-hidden rounded group cursor-pointer">
        {previewUrl ? (
          <img
            src={previewUrl}
            alt="Selected"
            className="h-full w-full object-cover object-center mx-auto"
          />
        ) : (
          <div className="flex flex-col justify-center h-full">
            <div className="font-semibold my-auto group-hover:opacity-0">
              No Image Selected
            </div>
          </div>
        )}

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
      {previewUrl && (
        <div className="flex justify-center">
          <button
            onClick={() => {
              setPreviewUrl(null);
            }}
            className="w-fit text-md p-2 font-bold italic hover:opacity-50 transition my-auto"
          >
            Clear Selection
            <XMarkIcon className="h-6 w-6 inline" />
          </button>
        </div>
      )}
    </div>
  );
};

export default SingleImageSelect;
