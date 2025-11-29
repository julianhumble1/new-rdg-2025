import Socials from "../../modals/Socials.jsx";

const PhotoColumn = ({ imgSrc }) => {
  return (
    <div className="flex-1 flex flex-col justify-end">
      <div className="flex justify-center">
        <img src={imgSrc} className="w-full h-full" />
      </div>
      <div className="md:hidden">
        <div className="flex justify-center">Find us on socials:</div>
        <Socials />
      </div>
    </div>
  );
};

export default PhotoColumn;
