import LeftColumn from "./LeftColumn.jsx";
import RightColumn from "./RightColumn.jsx";

const AltHome = () => {
  return (
    <div className="max-w-[1440px] mx-auto flex flex-1 w-full md:flex-row flex-col-reverse">
      <LeftColumn />
      <RightColumn />
    </div>
  );
};

export default AltHome;
