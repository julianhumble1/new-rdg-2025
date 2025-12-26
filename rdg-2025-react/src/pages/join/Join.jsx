import StandardPageLayout from "../../components/common/PageLayout/StandardPageLayout.jsx";
import JoinContent from "./JoinContent.jsx";

const Join = () => {
  return (
    <StandardPageLayout
      photoPosition="left"
      imgSrc="/images/scribble/6.png"
      title="Join Us"
      content={JoinContent}
    />
  );
};

export default Join;
