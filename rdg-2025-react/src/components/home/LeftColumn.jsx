import Socials from "../modals/Socials.jsx";

const LeftColumn = () => {
  return (
    <div className="flex-1 flex flex-col justify-end">
      <div className="flex justify-center">
        <img src="/images/Homepage Image Group.svg"  className="w-full h-full"/>
      </div>
      <div className="md:hidden">
        <div className="flex justify-center">Find us on socials:</div>
        <Socials />
      </div>
    </div>
  );
};

export default LeftColumn;
