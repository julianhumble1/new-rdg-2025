import StandardPageLayout from "../../../components/common/PageLayout/StandardPageLayout.jsx";
import MembershipContent from "./MembershipContent.jsx";

const Membership = () => {
  return (
    <StandardPageLayout
      imgSrc="/images/scribble/8.svg"
      title="Membership"
      photoPosition="right"
      content={MembershipContent}
    />
  );
};

export default Membership;
