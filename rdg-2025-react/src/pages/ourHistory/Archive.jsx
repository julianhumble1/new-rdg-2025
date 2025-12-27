import StandardPageLayout from "../../components/common/PageLayout/StandardPageLayout.jsx";
import ArchiveContent from "./ArchiveContent.jsx";

const Archive = () => {
  return (
    <StandardPageLayout
      title="Our History"
      imgSrc="/images/scribble/3.png"
      content={ArchiveContent}
      photoPosition="right"
    />
  );
};

export default Archive;
