import StandardPageLayout from "../common/PageLayout/StandardPageLayout.jsx";
import HomeContent from "./HomeContent.jsx";
import InstaFeed from "./InstaFeed.jsx";

const AltHome = () => {
  return (
    <StandardPageLayout
      imgSrc="/images/scribble/1.webp"
      title="We are RDG"
      content={HomeContent}
      extraComponent={<InstaFeed />}
      extraComponentSide="left"
    />
  );
};

export default AltHome;
