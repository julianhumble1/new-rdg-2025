import StandardPageLayout from "../../../components/common/PageLayout/StandardPageLayout.jsx";
import LegalContent from "./LegalContent.jsx";

const Legal = () => {
  return (
    <StandardPageLayout
      photoPosition="left"
      imgSrc="/images/scribble/5.png"
      title="Legal & Governance"
      content={LegalContent}
    />
  );
};

export default Legal;
