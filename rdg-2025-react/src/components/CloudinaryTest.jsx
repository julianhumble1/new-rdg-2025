import { AdvancedImage } from "@cloudinary/react";
import { auto } from "@cloudinary/url-gen/actions/resize";
import { Cloudinary } from "@cloudinary/url-gen/index";
import { autoGravity } from "@cloudinary/url-gen/qualifiers/gravity";
import { toast } from "react-toastify";

const CloudinaryTest = () => {
  const cld = new Cloudinary({ cloud: { cloudName: "dbher59sh" } });

  const img = cld
    .image("3")
    .format("auto")
    .quality("auto")
    .resize(auto().gravity(autoGravity()).width(500).height(500));

  return (
    <div className="flex justify-center gap-4">
      CloudinaryTest
      <AdvancedImage cldImg={img} />
    </div>
  );
};

export default CloudinaryTest;
