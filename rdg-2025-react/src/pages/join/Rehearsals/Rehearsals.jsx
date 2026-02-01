import StandardPageLayout from "../../../components/common/PageLayout/StandardPageLayout.jsx";
import RehearsalsContent from "./RehearsalsContent.jsx";

const Rehearsals = () => {
  return (
    <StandardPageLayout
      imgSrc="/images/scribble/11.webp"
      title="Rehearsals & Auditions"
      content={RehearsalsContent}
    />
  );
};

export default Rehearsals;
