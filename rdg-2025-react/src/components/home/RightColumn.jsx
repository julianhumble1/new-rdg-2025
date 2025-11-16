import Socials from "../modals/Socials.jsx";

const RightColumn = () => {
  return (
    <div className="flex-1 flex flex-col p-6 gap-3">
      <div className="text-[50px] text-rdg-blue tracking-wider">We are RDG</div>
      <div>
        Runnymede Drama Group is one of the most lively and successful community
        theatre groups in the area.
      </div>
      <div>
        Rehearsing at our dedicated studio in Chertsey in Surrey, we stage at
        least five productions each year at venues including The Riverhouse Barn
        and Cecil Hepworth Playhouse in Walton, and the Rhoda McGaw Theatre in
        Woking.
      </div>
      <div className="hidden md:inline">
        <div className="text-center">Find us on socials:</div>
        <Socials />
      </div>
    </div>
  );
};

export default RightColumn;
