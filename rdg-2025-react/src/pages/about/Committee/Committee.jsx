import StandardPageLayout from "../../../components/common/PageLayout/StandardPageLayout.jsx";
import CommitteeContent from "./CommitteeContent.jsx";

const Committee = () => {
  return (
    <StandardPageLayout
      photoPosition="right"
      imgSrc="/images/scribble/4.png"
      title="Committee"
      content={CommitteeContent}
    />
  );
};

export default Committee;
