import StandardPageLayout from "../../components/common/PageLayout/StandardPageLayout.jsx";
import JoinContent from "./JoinContent.jsx";

const Join = () => {
  return (
    <StandardPageLayout
      photoPosition="left"
      imgSrc="/images/scribble/9.webp"
      title="Get Involved"
      content={JoinContent}
    />
  );
};

export default Join;
