import StandardPageLayout from "../common/PageLayout/StandardPageLayout.jsx";
import HomeContent from "./HomeContent.jsx";

const AltHome = () => {
  return (
    <StandardPageLayout
      imgSrc="/images/scribble/1.png"
      title="We are RDG"
      content={HomeContent}
    />
  );
};

export default AltHome;
