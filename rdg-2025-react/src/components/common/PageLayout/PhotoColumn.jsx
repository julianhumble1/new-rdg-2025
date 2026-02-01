import Socials from "../../modals/Socials.jsx";

const PhotoColumn = ({ imgSrc }) => {
  return (
    <div className="flex-1 flex flex-col justify-start">
      <img src={imgSrc} loading="eager" />

      <div className="md:hidden">
        <div className="flex justify-center">Find us on socials:</div>
        <Socials />
      </div>
    </div>
  );
};

export default PhotoColumn;
