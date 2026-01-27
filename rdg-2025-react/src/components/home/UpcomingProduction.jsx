import RedButton from "../common/RedButton.jsx";

const UpcomingProduction = () => {
  const openTicketLink = () => {
    window.open(
      "https://www.ticketsource.co.uk/the-sundowners",
      "_blank",
      "noopener",
    );
  };

  return (
    <>
      <div className="flex justify-between">
        <div className="text-rdg-red font-bold text-lg my-auto">
          Our Current Production
        </div>
        <RedButton onClick={openTicketLink}>Book Now</RedButton>
      </div>
      <img
        src="/images/beauty social adv2_.jpg"
        alt="xmas at barn"
        className="mx-8 hover:cursor-pointer"
        onClick={openTicketLink}
      />
    </>
  );
};

export default UpcomingProduction;
