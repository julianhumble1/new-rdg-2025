import StandardPageLayout from "../../../components/common/PageLayout/StandardPageLayout.jsx";
import OurVenuesContent from "./OurVenuesContent.jsx";

const OurVenues = () => {
  return (
    <StandardPageLayout
      photoPosition="left"
      imgSrc="/images/scribble/3.webp"
      title="Our Venues"
      content={OurVenuesContent}
    />
  );
};

export default OurVenues;
